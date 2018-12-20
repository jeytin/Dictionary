package com.lofts.dictionary.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.lofts.dictionary.R
import com.lofts.dictionary.adapter.DefineAdapter
import com.lofts.dictionary.adapter.SampleAdapter
import com.lofts.dictionary.bean.Result
import com.lofts.dictionary.utils.RequestManager
import kotlinx.android.synthetic.main.activity_input.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.item_headview.*
import org.jetbrains.anko.longToast
import java.io.IOException

/**
 * PACKAGE_NAME：com.lofts.dictionary.ui
 * DATE：2018-12-18 22:52
 * USER: asus
 * DESCRIBE:
 */
class ResultActivity : AppCompatActivity() {

    private val URL = "http://xtk.azurewebsites.net/BingDictService.aspx"

    private var headView: View? = null
    private var mAdapter: SampleAdapter? = null
    private var mAdapterDefine: DefineAdapter? = null
    private var inputWord: String? = null
    private var mResult: Result? = null
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_result)

        initView()
        getTranslateData();
    }

    private fun initView() {
        inputWord = getIntent().getStringExtra("word");
        edittext_search_word.setText(inputWord)

        headView = LayoutInflater.from(this).inflate(R.layout.item_headview, null);
        listview_sample.addHeaderView(headView);
        mAdapter = SampleAdapter(this);
        listview_sample.setAdapter(mAdapter);
        mMediaPlayer = MediaPlayer()

        edittext_search_word.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                getTranslateData()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        imageview_title_left.setOnClickListener{
            finish()
        }

    }

    private fun getTranslateData() {
        var inputValue = edittext_search_word.text.toString();
        if (TextUtils.isEmpty(inputValue)) {
            longToast("请输入需要翻译的单词")
            return;
        }

        val map = HashMap<String, String>()
        map.put("Word", inputValue)

        RequestManager.getInstance(this)
            .requestAsyn<Any>(URL, RequestManager.TYPE_GET, map, object : RequestManager.ReqCallBack {
                override fun onReqSuccess(result: Any) {
                    if (result != null) {
                        mResult = Gson().fromJson(result.toString(), Result::class.java)
                        setData()
                    }
                }

                override fun onReqFailed(errorMsg: String) {
                    longToast(errorMsg)
                }

            })
    }

    private fun setData() {
        if (mResult?.word != null) {
            listview_sample.setVisibility(View.VISIBLE);
            textview_word.text = mResult?.word
        } else {
            longToast("该单词不存在")
            return;
        }

        if (mResult?.pronunciation != null) {
            ll_speak_english.setVisibility(View.VISIBLE);
            ll_speak_america.setVisibility(View.VISIBLE);
            textview_english_symbol.text = "英[${mResult?.pronunciation?.BrE}]"
            textview_america_symbol.text = "美[${mResult?.pronunciation?.AmE}]"
            imageview_english_speak.setOnClickListener {
                playMedia(mResult?.pronunciation?.BrEmp3);
            }
            textview_america_symbol.setOnClickListener {
                playMedia(mResult?.pronunciation?.AmEmp3)
            }
        } else {
            ll_speak_english.setVisibility(View.GONE);
            ll_speak_america.setVisibility(View.GONE);
        }

        if (mResult!!.sams != null && mResult!!.sams.size > 0) {
            listview_define.setVisibility(View.VISIBLE)
            mAdapterDefine = DefineAdapter(this)
            listview_define.setAdapter(mAdapterDefine)
            mAdapterDefine?.resetList(mResult?.defs)
        }

        if (mResult!!.sams != null && mResult!!.sams.size > 0) {
            mAdapter?.resetList(mResult?.sams)

            mAdapter?.setPlaySampleListener(object : SampleAdapter.OnPlaySampleListener {
                override fun playSample(url: String) {
                    playMedia(url)
                }
            })
        }
    }

    private fun playMedia(url: String?) {
        if (!TextUtils.isEmpty(url) && !mMediaPlayer!!.isPlaying()) {
            try {
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(url)
                mMediaPlayer?.prepareAsync()
                mMediaPlayer?.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
            } catch (e: IOException) {
                longToast("播放失败")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mMediaPlayer?.stop()
    }

}