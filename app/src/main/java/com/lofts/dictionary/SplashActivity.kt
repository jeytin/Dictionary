package com.lofts.dictionary

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.lofts.dictionary.ui.InputActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val delayTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        gotoIndexPage()
    }

    private fun gotoIndexPage() {
        Handler().postDelayed({
            startActivity<InputActivity>()
        }, delayTime)
    }


}
