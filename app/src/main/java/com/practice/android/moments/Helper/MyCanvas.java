package com.practice.android.moments.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Ashutosh on 7/21/2017.
 */

public class MyCanvas extends View {
    public MyCanvas(Context context) {
        super(context);
        // TODO Auto-generated constructor stub


    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Paint pText = new Paint();
        pText.setColor(Color.parseColor("#f0810f"));
        pText.setTextSize(30);
        pText.setStrokeWidth(8);
        canvas.drawText("Click to Select an Image", 100, 100, pText);
    }
}




