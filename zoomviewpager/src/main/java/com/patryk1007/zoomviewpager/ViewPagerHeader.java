package com.patryk1007.zoomviewpager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHeader extends HorizontalScrollView implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {

    private TextAttr textViewAttr;
    private TabView[] textViews = new TabView[0];
    private int headerPerView = 3;
    private int headerWidth;
    private ViewPager viewPager;

    private int[] mPosition = new int[2];

    private OnClickListener mListener;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickTab(x,y);
                return false;
            default:

                return super.onTouchEvent(ev);
        }
    }

    /**
     * 点击tab
     *
     * @param x
     * @param y
     */
    public void clickTab(int x, int y){
        parsePosition(textViews[1],x);
        parsePosition(textViews[2],x);
        parsePosition(textViews[3],x);
    }

    /**
     * 解析点击事件
     *
     * @param view
     * @param x
     */
    public void parsePosition(View view, int x){

        view.getLocationOnScreen(mPosition);
        int startX = mPosition[0];
        int endX = mPosition[0] +  view.getWidth();

        if (x >= startX && x <= endX) {
            if (mListener != null){
                mListener.onClick(view);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setCurrentPosition(position, positionOffsetPixels, positionOffset);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        prepareHeaders();
    }

    public ViewPagerHeader(Context context, TextAttr textViewAttr) {
        super(context);
        this.textViewAttr = textViewAttr;
        defaultSettings();
    }

    public ViewPagerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(this);
        prepareHeaders();
    }

    private void defaultSettings() {
        setHorizontalScrollBarEnabled(false);
    }

    private void prepareHeaders() {
        PagerAdapter adapter = viewPager.getAdapter();
        List<String> headers = new ArrayList<>();
        headers.add("");
        for (int i = 0; i < adapter.getCount(); i++) {
            CharSequence header = adapter.getPageTitle(i);
            headers.add(String.valueOf(header == null ? "" : header));
        }
        headers.add("");
        createHeader(headers);
    }

    private void createHeader(List<String> headers) {
        textViews = new TabView[headers.size()];
        headerWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        this.removeAllViews();
        LinearLayout rootContainer = createRootContainer();
        for (int i = 0; i < headers.size(); i++) {
            rootContainer.addView(createHeaderItem(i, headers.get(i)));
        }
        this.addView(rootContainer);
    }

    private LinearLayout createRootContainer() {
        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(Color.WHITE);

        return container;
    }

    private TextView createHeaderItem(int position, String headerText) {

        int padding = (int) textViewAttr.getHvPadding();

        TabView header = new TabView(getContext());

        if (position == 1){
            header.setId(R.id.tab_setting);
        }else if (position == 2){
            header.setId(R.id.tab_hall);
        }else if (position == 3){
            header.setId(R.id.tab_private_letter);
        }

        LayoutParams linearParams = new LayoutParams(headerWidth / headerPerView, LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(linearParams);

        header.setScaleX(textViewAttr.getHvMinScale());
        header.setScaleY(textViewAttr.getHvMinScale());
        header.setAlpha(textViewAttr.getHvTextAlpha());
        header.setTextColor(textViewAttr.getHvTextColor());
        header.setPadding(0, padding, 0,padding);
        Paint paint = header.getPaint();
        paint.setFakeBoldText(true);

        header.setMaxLines(1);
        header.setEllipsize(TextUtils.TruncateAt.END);
        header.setText(headerText);
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, textViewAttr.getHvTextSize());

        header.setGravity(Gravity.CENTER);

        textViews[position] = header;

        return header;
    }

    public void setTabOnClickListener(OnClickListener listener){
        mListener = listener;
    }

    public void setCurrentPosition(int viewPagerPosition, int offset, float scale) {
        Log.i("jiao","viewPagerPosition "+viewPagerPosition+" offset "+offset+" scale "+scale);
        int currentPosition = viewPagerPosition + 1;
        updateScale(currentPosition, scale);
        scrollTo(viewPagerPosition * headerWidth / headerPerView + offset / headerPerView, 0);
    }

    private void updateScale(int current, float offset) {
        current = Math.round(current + offset);
        float position = 1 - (offset > 0.5f ? 1 - offset : offset);
        if (textViews != null && textViews.length > current) {

            Log.i("jiao"," current "+current);

            if (current == 2){
                textViews[2].setCureentGravity(2,true,Gravity.CENTER);
                textViews[1].setCureentGravity(1,false,Gravity.LEFT);
                textViews[3].setCureentGravity(3,false,Gravity.RIGHT);
            }else if (current == 1){
                textViews[2].setCureentGravity(2,false,Gravity.RIGHT);
                textViews[1].setCureentGravity(1,true,Gravity.CENTER);
                textViews[3].setCureentGravity(3,false,Gravity.CENTER);
            }else if (current == 3){
                textViews[2].setCureentGravity(2,false,Gravity.LEFT);
                textViews[1].setCureentGravity(1,false,Gravity.CENTER);
                textViews[3].setCureentGravity(3,true,Gravity.CENTER);
            }


            updateTextView(textViews[current], getScale(position - 0.5f), getAlpha(position - 0.5f), textViewAttr.getHvTextColorActiveTab());
            updateNextAndPrev(current);
        }
    }

    private float getAlpha(float scale) {
        float range = textViewAttr.getHvTextAlphaActiveTab() - textViewAttr.getHvTextAlpha();
        return textViewAttr.getHvTextAlpha() + range * scale / 0.5f;
    }

    private float getScale(float scale) {
        float range = textViewAttr.getHvMaxScale() - textViewAttr.getHvMinScale();
        return textViewAttr.getHvMinScale() + range * scale / 0.5f;
    }

    private void updateNextAndPrev(int current) {

        if (current == 1){
//            textViews[current].setGravity(Gravity.CENTER);
//            textViews[2].setGravity(Gravity.RIGHT);

//            textViews[2].setPosition(current);
        }else if (current == 2){
//            textViews[2].setGravity(Gravity.CENTER);
//            textViews[2].setPosition(current);

//            textViews[1].setGravity(Gravity.LEFT);
//            textViews[3].setGravity(Gravity.RIGHT);
        }else if (current == 3){
//            textViews[2].setGravity(Gravity.LEFT);
//            textViews[2].setPosition(current);

//            textViews[3].setGravity(Gravity.CENTER);
        }


        float scale = textViewAttr.getHvMinScale();
        float alpha = textViewAttr.getHvTextAlpha();
        int color = textViewAttr.getHvTextColor();

        if (current > 0 && textViews != null && textViews.length > 0) {
            updateTextView(textViews[current - 1], scale, alpha, color);
        }

        if (textViews != null && textViews.length > current + 1) {
            updateTextView(textViews[current + 1], scale, alpha, color);
        }
    }

    private void updateTextView(TextView textView, float scale, float alpha, int color) {
        textView.setScaleX(scale);
        textView.setScaleY(scale);
        textView.setAlpha(alpha);
        textView.setTextColor(color);
    }

}
