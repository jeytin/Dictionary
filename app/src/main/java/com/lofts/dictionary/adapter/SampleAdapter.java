package com.lofts.dictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lofts.dictionary.R;
import com.lofts.dictionary.bean.Sample;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter extends BaseAdapter {

    private Context mContext;
    private List<Sample> mListSample;
    private OnPlaySampleListener mListener;

    public void setPlaySampleListener(OnPlaySampleListener listener) {
        mListener = listener;
    }

    public SampleAdapter(Context context) {
        mContext = context;
        mListSample = new ArrayList<>();
    }

    public void resetList(List<Sample> list) {
        mListSample.clear();
        mListSample.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListSample.size();
    }

    @Override
    public Object getItem(int position) {
        return mListSample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sample, parent, false);

        TextView english = convertView.findViewById(R.id.textview_english);
        TextView chinese = convertView.findViewById(R.id.textview_chinese);
        ImageView speak = convertView.findViewById(R.id.imageview_speak);

        Sample sample = mListSample.get(position);
        english.setText(sample.getEng());
        chinese.setText(sample.getChn());
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.playSample(sample.getMp3Url());
            }
        });

        return convertView;
    }


    public interface OnPlaySampleListener {
        void playSample(String url);
    }
}
