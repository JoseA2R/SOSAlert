package com.acin.josefigueira.sosalert.Classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import com.acin.josefigueira.sosalert.R;

public class SetCountDown {

    public void SetCountDown(){

    }

  /*  private BitmapDrawable writeTextCountDown(int drawableId, String Text){
        Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(11);
        Rect textRect = new Rect();
        paint.getTextBounds(Text, 0, Text.length(), textRect);
        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, xPos, yPos, paint);

        return new BitmapDrawable(getResources(),bm);
    }*/

}
