package com.wytiger.widget.edittext.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Description:禁止输入Emoji
 * Created by wytiger on 2017/4/20
 */

public class EmojiExcludeFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            int type = Character.getType(source.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }
        return null;
    }
}