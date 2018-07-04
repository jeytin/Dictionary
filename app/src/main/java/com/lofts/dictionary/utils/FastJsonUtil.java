package com.lofts.dictionary.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */

public class FastJsonUtil {

    public static String getString(String jsonStr, String key) {
        try {
            String value = new JSONObject(jsonStr).getString(key);
            return value;
        } catch (org.json.JSONException e) {
            return null;
        }
    }

    /**
     * 实体类解析
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(String jsonStr, Class<T> cls) {
        try {
            T t = JSON.parseObject(jsonStr, cls);
            return t;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 列表解析
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(String jsonStr, Class<T> cls) {
        try {
            List<T> list = JSON.parseArray(jsonStr, cls);
            return list;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 实体类解析（多层）
     *
     * @param jsonStr
     * @param parentKey
     * @param cls
     * @param <T>
     * @return
     * @throws org.json.JSONException
     */
    public static <T> T getBean(String jsonStr, String parentKey, Class<T> cls) throws org.json.JSONException {
        try {
            String data;
            if (!TextUtils.isEmpty(parentKey)) {
                data = new JSONObject(jsonStr).getString(parentKey);
                T t = JSON.parseObject(data, cls);
                return t;
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

}

