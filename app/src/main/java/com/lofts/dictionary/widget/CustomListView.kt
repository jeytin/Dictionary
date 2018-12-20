package com.lofts.dictionary.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import org.jetbrains.anko.db.INTEGER

/**
 * PACKAGE_NAME：com.lofts.dictionary.widget
 * DATE：2018-12-18 23:12
 * USER: asus
 * DESCRIBE:
 */
class CustomListView : ListView {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var expandSpec: Int = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

}