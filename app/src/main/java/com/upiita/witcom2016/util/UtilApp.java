package com.upiita.witcom2016.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.upiita.witcom2016.R;

/**
 * Created by A_ESPARZA on 09/10/2017.
 */

public class UtilApp {
    /**
     * Method used to surround a word between accents in a TextView
     * @param context   Activity context
     * @param text      Text to be surrounded
     * @param prev      First Accent
     * @param end       Last accent
     * @param element   Text View
     */
    public static void betweenAccents (Context context, String text, String prev, String end, TextView element) {
        Spannable acc_prev = new SpannableString(prev);
        acc_prev.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable acc_end = new SpannableString(end);
        acc_end.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        element.setText(acc_prev);
        element.append(text);
        element.append(acc_end);
    }
}
