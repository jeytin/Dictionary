package com.lofts.dictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lofts.dictionary.R;
import com.lofts.dictionary.bean.Define;
import com.lofts.dictionary.bean.Sample;

import java.util.ArrayList;
import java.util.List;

public class DefineAdapter extends BaseAdapter {

    private Context mContext;
    private List<Define> mListDefine;

    public DefineAdapter(Context context) {
        mContext = context;
        mListDefine = new ArrayList<>();
    }

    public void resetList(List<Define> list) {
        mListDefine.clear();
        mListDefine.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListDefine.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDefine.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_define, parent, false);

        TextView type = convertView.findViewById(R.id.textview_type);
        TextView paraphrase = convertView.findViewById(R.id.textview_paraphrase);

        Define define = mListDefine.get(position);
        type.setText(define.getPos());
        paraphrase.setText(define.getDef());

        return convertView;
    }
}
