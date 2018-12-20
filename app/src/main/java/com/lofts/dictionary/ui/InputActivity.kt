package com.lofts.dictionary.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.lofts.dictionary.R
import kotlinx.android.synthetic.main.activity_input.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

/**
 * PACKAGE_NAME：com.lofts.dictionary.ui
 * DATE：2018-12-18 22:49
 * USER: asus
 * DESCRIBE:
 */
class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_input)

        initView()
    }

    private fun initView() {
        edittext_input.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                getTranslateResult()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })
    }

    private fun getTranslateResult() {
        val inputValue = edittext_input.text.toString()
        if (TextUtils.isEmpty(inputValue)) {
            longToast("请输入要查询的单词")
            return
        }
        startActivity<ResultActivity>("word" to inputValue)

    }

}