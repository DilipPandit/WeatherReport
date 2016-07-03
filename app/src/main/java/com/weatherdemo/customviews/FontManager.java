package com.weatherdemo.customviews;

import android.content.Context;
import android.graphics.Typeface;


public class FontManager
{
	private static Typeface tfHelvectica;

	public static Typeface getHelvecticaThin(Context ctx)
	{
		if (tfHelvectica == null)
		{
			tfHelvectica = Typeface.createFromAsset(ctx.getAssets(), "fonts/mavenpro.ttf");
		}
		return tfHelvectica;
	}

	private static Typeface tfHelvecticaItalic;

	public static Typeface getHelvecticaThinItalic(Context ctx)
	{
		if (tfHelvecticaItalic == null)
		{
			tfHelvecticaItalic = Typeface.createFromAsset(ctx.getAssets(), "fonts/mavenpro.ttf");
		}
		return tfHelvecticaItalic;
	}

	private static Typeface tftitleFont;

	public static Typeface getTitleFont(Context ctx)
	{
		if (tftitleFont == null)
		{
			tftitleFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/mavenpro.ttf");
		}
		return tftitleFont;
	}

}
