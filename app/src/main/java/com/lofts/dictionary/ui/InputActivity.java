package com.lofts.dictionary.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lofts.dictionary.R;
import com.lofts.dictionary.utils.RequestManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class InputActivity extends AppCompatActivity {

    private static final String TAG = InputActivity.class.getSimpleName();

    @BindView(R.id.edittext_input)
    EditText mEtInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_input);

        ButterKnife.bind(this);

    }

    @OnEditorAction(R.id.edittext_input)
    public boolean setOnEditerAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            getTranslateResult();
            return true;
        }
        return false;
    }

    private void getTranslateResult() {
        String inputValue = mEtInput.getText().toString();
        if (TextUtils.isEmpty(inputValue)) {
            Toast.makeText(this, "请输入需要翻译的单词", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("word", inputValue);
        intent.setClass(this, ResultActivity.class);
        startActivity(intent);
    }


}
