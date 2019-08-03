package com.sadwyn.statepb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sadwyn.resultprogressbar.ResultProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ResultProgressBar.AnimationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn.setOnClickListener { pb.reset(); pb2.reset();pb3.reset() }
        successBtn.setOnClickListener { pb.success(); pb2.success(); pb3.success() }
        failureBtn.setOnClickListener { pb.failure(); pb2.failure(); pb3.failure() }
        pb.listener = this
    }

    override fun onAnimationResult(success: Boolean) {
        Log.i("TAG", "Animation success status is : $success")
    }
}
