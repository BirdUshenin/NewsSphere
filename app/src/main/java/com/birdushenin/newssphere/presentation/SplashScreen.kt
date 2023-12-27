package com.birdushenin.newssphere.presentation

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.birdushenin.newssphere.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator, isReverse: Boolean) {}
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                coroutineScope.launch {
                    loadContent()
                }
            }

            override fun onAnimationEnd(p0: Animator) {}

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationRepeat(p0: Animator) {}
        })
    }

    private suspend fun loadContent() {
        delay(1000)
        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
