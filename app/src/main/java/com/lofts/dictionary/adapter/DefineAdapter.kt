package com.lofts.dictionary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.lofts.dictionary.R
import com.lofts.dictionary.bean.Define

/**
 * @name      DictionaryKotlin
 * @package   com.lofts.dictionary.adapter
 * @author    Administrator
 * @time      2018/12/19 17:19
 * @describe
 */
class DefineAdapter(context: Context) : BaseAdapter() {

    private var mContext: Context
    private var mListDefine: MutableList<Define>

    init {
        mContext = context
        mListDefine = ArrayList()
    }

    fun resetList(list: List<Define>?){
        mListDefine.clear()
        mListDefine.addAll(list!!)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //var convertView = convertView
        var convertView = LayoutInflater.from(mContext).inflate(R.layout.item_define, parent, false)

        var type = convertView.findViewById<TextView>(R.id.textview_type)
        val paraphrase = convertView.findViewById<TextView>(R.id.textview_paraphrase)

        val define = mListDefine[position]
        type.text = define.pos
        paraphrase.text = define.def

        return convertView
    }

    override fun getItem(position: Int): Any {
        return mListDefine.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mListDefine.size
    }


}