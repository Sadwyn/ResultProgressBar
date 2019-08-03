package com.sadwyn.resultprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.*
import android.widget.FrameLayout
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.result_progress_bar_layout.view.*

class ResultProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    protected var progressAnimationRes: Int = R.drawable.ic_loading
    protected var successAnimationRes: Int = R.drawable.ic_success
    protected var failureAnimationRes: Int = R.drawable.ic_fail
    protected var borderShape: Int = R.drawable.border
    protected var changeStateType: Int = 0
    protected var progressDrawableMargin: Int = resources.getDimensionPixelSize(R.dimen.default_inner_margin)
    protected var successDrawableMargin: Int = resources.getDimensionPixelSize(R.dimen.default_inner_margin)
    protected var failureDrawableMargin: Int = resources.getDimensionPixelSize(R.dimen.default_inner_margin)
    protected var progressSpeed: Int = 1000
    protected var interpolatorValue: Int = 0

    protected var currentAnimationRes: Int = 0
    protected val rotateAnimation = RotateAnimation(
        0F,
        360F,
        Animation.RELATIVE_TO_SELF,
        0.5F, Animation.RELATIVE_TO_SELF,
        0.5F
    )
    var listener: AnimationListener? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.result_progress_bar_layout, this, true)
        attrs?.let {
            getAttrs(context, it, defStyleAttr)
        }
        initialize()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    protected fun getAttrs(context: Context, it: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(it, R.styleable.ResultProgressBar, defStyleAttr, 0)
        progressAnimationRes =
            typedArray.getResourceId(R.styleable.ResultProgressBar_progressDrawable, R.drawable.ic_loading)
        failureAnimationRes =
            typedArray.getResourceId(R.styleable.ResultProgressBar_failureDrawable, R.drawable.ic_fail)
        successAnimationRes =
            typedArray.getResourceId(R.styleable.ResultProgressBar_successDrawable, R.drawable.ic_success)
        borderShape = typedArray.getResourceId(R.styleable.ResultProgressBar_borderShape, R.drawable.border)

        changeStateType = typedArray.getInteger(R.styleable.ResultProgressBar_changeStateType, 0)

        progressDrawableMargin = typedArray.getDimensionPixelSize(
            R.styleable.ResultProgressBar_progressDrawableMargin,
            resources.getDimensionPixelSize(R.dimen.default_inner_margin)
        )
        successDrawableMargin = typedArray.getDimensionPixelSize(
            R.styleable.ResultProgressBar_successDrawableMargin,
            resources.getDimensionPixelSize(R.dimen.default_inner_margin)
        )
        failureDrawableMargin = typedArray.getDimensionPixelSize(
            R.styleable.ResultProgressBar_failureDrawableMargin,
            resources.getDimensionPixelSize(R.dimen.default_inner_margin)
        )
        progressSpeed = typedArray.getInteger(R.styleable.ResultProgressBar_progressSpeed, 1000)
        interpolatorValue = typedArray.getInteger(R.styleable.ResultProgressBar_progressInterpolator, 0)

        typedArray.recycle()
    }

    protected fun initialize() {
        animatedView.setBackgroundResource(progressAnimationRes)
        root.setBackgroundResource(borderShape)
        val params = animatedView.layoutParams as LayoutParams
        params.setMargins(progressDrawableMargin)
        animatedView.requestLayout()
        rotateAnimation.repeatCount = Animation.INFINITE

        when (interpolatorValue) {
            0 -> rotateAnimation.interpolator = LinearInterpolator()
            1 -> rotateAnimation.interpolator = AccelerateInterpolator()
            2 -> rotateAnimation.interpolator = DecelerateInterpolator()
            3 -> rotateAnimation.interpolator = AccelerateDecelerateInterpolator()
            else -> rotateAnimation.interpolator = LinearInterpolator()
        }

        rotateAnimation.duration = progressSpeed.toLong()
        animatedView.startAnimation(rotateAnimation)
    }

    public fun reset() {
        val params = animatedView.layoutParams as LayoutParams
        animatedView.setBackgroundResource(progressAnimationRes)
        params.setMargins(progressDrawableMargin)
        animatedView.requestLayout()
        animatedView.startAnimation(rotateAnimation)
    }

    public fun success() {
        finishAnimation(true)
    }


    public fun failure() {
        finishAnimation(false)
    }

    protected fun finishAnimation(success: Boolean) {
        animatedView.animation?.cancel()
        currentAnimationRes = if (success) successAnimationRes else failureAnimationRes
        when (changeStateType) {
            0 -> flip()
            1 -> alpha()
            2 -> scale()
        }
    }

    protected fun flip() {
        root.cameraDistance = root.width * CAMERA_DISTANCE
        root.animate().withLayer()
            .rotationY(90F)
            .setDuration(progressSpeed.toLong())
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                root.rotationY = -90F
                updateMargins()
                root.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(progressSpeed.toLong()).withEndAction {
                        listener?.onAnimationResult(currentAnimationRes == successAnimationRes)
                    }
                    .start()
            }.start()
    }

    protected fun scale() {
        animatedView.animate().withLayer()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(progressSpeed.toLong())
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                updateMargins()
                animatedView.animate().withLayer()
                    .scaleX(1F)
                    .scaleY(1F)
                    .setDuration(progressSpeed.toLong()).withEndAction {
                        listener?.onAnimationResult(currentAnimationRes == successAnimationRes)
                    }
                    .start()
            }.start()
    }

    protected fun alpha() {
        animatedView.animate().withLayer()
            .alpha(0f)
            .setDuration(progressSpeed.toLong())
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                updateMargins()
                animatedView.animate().withLayer()
                    .alpha(1F)
                    .setDuration(progressSpeed.toLong()).withEndAction {
                        listener?.onAnimationResult(currentAnimationRes == successAnimationRes)
                    }
                    .start()
            }.start()
    }

    protected fun updateMargins() {
        val params = animatedView.layoutParams as LayoutParams
        if (currentAnimationRes == successAnimationRes) {
            params.setMargins(successDrawableMargin)
        } else {
            params.setMargins(failureDrawableMargin)
        }
        animatedView.requestLayout()
    }


    interface AnimationListener {
        fun onAnimationResult(success: Boolean)
    }

    companion object {
        const val CAMERA_DISTANCE = 100F
    }
}