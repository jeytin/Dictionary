package com.lofts.dictionary.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lofts.dictionary.R;
import com.lofts.dictionary.adapter.DefineAdapter;
import com.lofts.dictionary.adapter.SampleAdapter;
import com.lofts.dictionary.bean.Result;
import com.lofts.dictionary.bean.Sample;
import com.lofts.dictionary.utils.FastJsonUtil;
import com.lofts.dictionary.utils.RequestManager;
import com.lofts.dictionary.widget.CustomListView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();

    private static final String URL = "http://xtk.azurewebsites.net/BingDictService.aspx";


    @BindView(R.id.edittext_search_word)
    EditText mEtInput;

    @BindView(R.id.listview_sample)
    ListView mLvSample;

    private View headView;

    private SampleAdapter mAdapter;
    private DefineAdapter mAdapterDefine;
    private String inputWord;
    private Result mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        ButterKnife.bind(this);

        initView();

        getTranslateData();
    }

    private void initView() {
        inputWord = getIntent().getStringExtra("word");
        mEtInput.setText(inputWord);

        headView = LayoutInflater.from(this).inflate(R.layout.item_headview, null);
        mLvSample.addHeaderView(headView);

        mAdapter = new SampleAdapter(this);
        mLvSample.setAdapter(mAdapter);
    }

    private void setData() {
        HeaderViewHolder holder = new HeaderViewHolder(headView);
        if (mResult.getWord() != null) {
            mLvSample.setVisibility(View.VISIBLE);
            holder.mTvWord.setText(mResult.getWord());
        } else {
            Toast.makeText(ResultActivity.this, "该单词不存在", Toast.LENGTH_LONG).show();
            return;
        }

        if (mResult.getPronunciation() != null) {
            holder.mLlEnglish.setVisibility(View.VISIBLE);
            holder.mLlAmerica.setVisibility(View.VISIBLE);
            holder.mTvEnglishSymbol.setText("英[" + mResult.getPronunciation().getBrE() + "]");
            holder.mTvAmericaSymbol.setText("美[" + mResult.getPronunciation().getAmE() + "]");
        } else {
            holder.mLlEnglish.setVisibility(View.GONE);
            holder.mLlAmerica.setVisibility(View.GONE);
        }

        if (mResult.getSams() != null && mResult.getSams().size() > 0) {
            holder.mLvDefine.setVisibility(View.VISIBLE);
            mAdapterDefine = new DefineAdapter(this);
            holder.mLvDefine.setAdapter(mAdapterDefine);
            mAdapterDefine.resetList(mResult.getDefs());
        }

        if (mResult.getSams() != null && mResult.getSams().size() > 0) {
            mAdapter.resetList(mResult.getSams());
        }
    }

    private void getTranslateData() {
        String inputValue = mEtInput.getText().toString();
        if (TextUtils.isEmpty(inputValue)) {
            Toast.makeText(this, "请输入需要翻译的单词", Toast.LENGTH_LONG).show();
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("Word", inputValue);
        RequestManager.getInstance(this).requestAsyn(URL, RequestManager.TYPE_GET, map, new RequestManager.ReqCallBack() {

            @Override
            public void onReqSuccess(Object result) {
                if (result != null) {
                    mResult = FastJsonUtil.getBean(result.toString(), Result.class);
                    setData();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                Toast.makeText(ResultActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnEditorAction(R.id.edittext_search_word)
    public boolean setOnEditerAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            getTranslateData();
            return true;
        }
        return false;
    }

    @OnClick(R.id.imageview_title_left)
    public void setOnClick(View view) {
        finish();
    }

    private void setHegiht(ListView listView) {
        /*获取adapter*/
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        /* listAdapter.getCount()返回数据项的数目*/
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        /*
         * listView.getDividerHeight()获取子项间分隔符占用的高度
         * params.height最后得到整个ListView完整显示需要的高度
         * */
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    class HeaderViewHolder {

        @BindView(R.id.textview_word)
        TextView mTvWord;

        @BindView(R.id.ll_speak_english)
        LinearLayout mLlEnglish;

        @BindView(R.id.textview_english_symbol)
        TextView mTvEnglishSymbol;

        @BindView(R.id.imageview_english_speak)
        ImageView mIvEnglishSpeak;

        @BindView(R.id.ll_speak_america)
        LinearLayout mLlAmerica;

        @BindView(R.id.textview_america_symbol)
        TextView mTvAmericaSymbol;

        @BindView(R.id.imageview_america_speak)
        ImageView mIvAmericaSpeak;

        @BindView(R.id.listview_define)
        CustomListView mLvDefine;

        public HeaderViewHolder(View headerRootView) {
            ButterKnife.bind(this, headerRootView);
        }
    }


}
