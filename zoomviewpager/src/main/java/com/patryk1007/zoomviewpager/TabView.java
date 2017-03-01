package com.patryk1007.zoomviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by user on 16/12/27.
 */

public class TabView extends TextView {

    private String mText = "";
    private Paint mPaint;
    private boolean isCurrent;
    private int mPosition;
    private Bitmap mBitmap;

    private int mGravity;
    private Rect mRect = new Rect();

    public void setText(String text){
        this.mText = text;
    }


    public TabView(Context context) {
        super(context);
        init();
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(){
        mPaint = getPaint();
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_arrow_down);
    }

    public void setCureentGravity(int position,boolean isCurrent,int gravity){
        this.isCurrent = isCurrent;
        this.mGravity = gravity;
        this.mPosition = position;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCenterText(canvas);
    }

    public void drawCenterText(Canvas canvas){

        mPaint.getTextBounds(mText, 0, mText.length(), mRect);

        int width = mRect.width();//文本的宽度
        int height = mRect.height();//文本的高度

        int startX = 0;
        int startY = 0;

        if (isCurrent){ //如果是当前tab gravity center
            startX = getMeasuredWidth() / 2 - width / 2;
            startY = getMeasuredHeight() / 2 + height / 2;
        }else if (!isCurrent && mPosition == 1){ // gravity left
            startY = getMeasuredHeight() / 2 + height / 2;
        }else if (!isCurrent && mPosition == 3){// gravity right
            startX = getMeasuredWidth()  - width;
            startY = getMeasuredHeight() / 2 + height / 2;
        }else if (!isCurrent && mPosition == 2){// gravity right and left

            if (mGravity == Gravity.LEFT){
                startY = getMeasuredHeight() / 2 + height / 2;
            }else if (mGravity == Gravity.RIGHT){
                startX = getMeasuredWidth()  - width;
                startY = getMeasuredHeight() / 2 + height / 2;
            }
        }

        canvas.drawText(mText,startX,startY,mPaint);

        if (isCurrent && mPosition == 2){
            canvas.drawBitmap(mBitmap,startX + width + mBitmap.getWidth(),startY - height / 2,mPaint);
        }

    }

}
