package com.sadwyn.resultprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
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
    protected var progressSpeed: Int = 700

    private var currentAnimationRes: Int = 0
    private val rotateAnimation = RotateAnimation(
        0F,
        360F,
        Animation.RELATIVE_TO_SELF,
        0.5F, Animation.RELATIVE_TO_SELF,
        0.5F
    )


    init {
        LayoutInflater.from(context).inflate(R.layout.result_progress_bar_layout, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(it, R.styleable.ResultProgressBar, defStyleAttr, 0)
            progressAnimationRes =
                typedArray.getResourceId(R.styleable.ResultProgressBar_progressDrawable, R.drawable.ic_loading)
            failureAnimationRes =
                typedArray.getResourceId(R.styleable.ResultProgressBar_failureDrawable, R.drawable.ic_fail)
            successAnimationRes =
                typedArray.getResourceId(R.styleable.ResultProgressBar_successDrawable, R.drawable.ic_success)
            borderShape = typedArray.getResourceId(R.styleable.ResultProgressBar_borderShape, R.drawable.border)

            changeStateType = typedArray.getInt(R.styleable.ResultProgressBar_changeStateType, 0)

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


            progressSpeed = typedArray.getInteger(R.styleable.ResultProgressBar_progressSpeed, 700)

            typedArray.recycle()
        }


        animatedView.setBackgroundResource(progressAnimationRes)
        root.setBackgroundResource(borderShape)
        val params = animatedView.layoutParams as LayoutParams
        params.setMargins(progressDrawableMargin)
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.duration = progressSpeed.toLong()
        animatedView.startAnimation(rotateAnimation)
    }

    public fun reset() {
        val params = animatedView.layoutParams as LayoutParams

        animatedView.clearAnimation()
        root.clearAnimation()
        animatedView.setBackgroundResource(progressAnimationRes)
        animatedView.startAnimation(rotateAnimation)
        params.setMargins(progressDrawableMargin)
        animatedView.requestLayout()
    }

    public fun success() {
        finishAnimation(true)
    }


    public fun failure() {
        finishAnimation(false)
    }

    protected fun finishAnimation(success: Boolean) {
        currentAnimationRes = if (success) successAnimationRes else failureAnimationRes
        when (changeStateType) {
            0 -> flip()
            1 -> alpha()
            2 -> scale()
        }
    }

    private fun flip() {
        root.cameraDistance = root.width * 100F
        root.animate().withLayer()
            .rotationY(90F)
            .setDuration(300)
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                root.rotationY = -90F
                updateMargins()
                root.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(300).withEndAction { updateMargins() }
                    .start()
            }.start()
    }

    private fun scale() {
        animatedView.animate().withLayer()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(300)
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                updateMargins()
                animatedView.animate().withLayer()
                    .scaleX(1F)
                    .scaleY(1F)
                    .setDuration(300).withEndAction { updateMargins() }
                    .start()
            }.start()
    }

    private fun alpha() {
        animatedView.animate().withLayer()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                animatedView.animation?.cancel()
                animatedView.setBackgroundResource(currentAnimationRes)
                updateMargins()
                animatedView.animate().withLayer()
                    .alpha(1F)
                    .setDuration(300).withEndAction { updateMargins() }
                    .start()
            }.start()
    }

    private fun updateMargins() {
        val params = animatedView.layoutParams as LayoutParams
        if (currentAnimationRes == successAnimationRes) {
            params.setMargins(successDrawableMargin)
        } else {
            params.setMargins(failureDrawableMargin)
        }
        animatedView.requestLayout()
    }
}