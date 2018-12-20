package com.lofts.dictionary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.lofts.dictionary.R
import com.lofts.dictionary.bean.Sample

/**
 * @name      DictionaryKotlin
 * @package   com.lofts.dictionary.adapter
 * @author    Administrator
 * @time      2018/12/19 17:57
 * @describe
 */

class SampleAdapter(context: Context) : BaseAdapter() {

    private var mContext: Context
    private var mListSample: MutableList<Sample>
    private var mListener: OnPlaySampleListener? = null

    init {
        mContext = context
        mListSample = ArrayList()
    }

    fun setPlaySampleListener(listener: OnPlaySampleListener) {
        mListener = listener
    }

    fun resetList(list: List<Sample>?) {
        mListSample.clear()
        mListSample.addAll(list!!)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sample, parent, false)

        var english = convertView.findViewById(R.id.textview_english) as TextView
        var chinese = convertView.findViewById(R.id.textview_chinese) as TextView
        var speak = convertView.findViewById(R.id.imageview_speak) as ImageView

        var sample = mListSample[position]
        english.text = sample.eng
        chinese.text = sample.chn

        speak.setOnClickListener {
            mListener?.playSample(sample.mp3Url)
        }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return mListSample[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mListSample.size
    }

    interface OnPlaySampleListener {
        fun playSample(url: String)
    }

}