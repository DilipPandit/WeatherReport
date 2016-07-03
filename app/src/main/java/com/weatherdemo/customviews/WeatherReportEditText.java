package com.weatherdemo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Dilip on 27-06-2016.
 */

public class WeatherReportEditText extends EditText
{

    public WeatherReportEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            int style = Typeface.NORMAL;
            String i = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textStyle");
            if (i != null)
            {
                style = Integer.parseInt(i.replace("0x", ""));
            }
            init(context, style);
        }
    }

    public WeatherReportEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (!isInEditMode())
        {
            int style = Typeface.NORMAL;
            String i = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textStyle");
            if (i != null)
            {
                style = Integer.parseInt(i.replace("0x", ""));
            }
            init(context, style);
        }
    }

    public WeatherReportEditText(Context context)
    {
        super(context);
        if (!isInEditMode())
        {
            init(context, Typeface.NORMAL);

        }
    }

    private void init(Context ctx, int style)
    {
        setTypeface(FontManager.getHelvecticaThin(ctx), style);
    }

}
