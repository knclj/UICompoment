package com.test.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CustomSRL2 extends SwipeRefreshLayout {

    //外部拦截
    private float startX;
    private float startY;
    private float x;
    private float y;
    private float deltaX;
    private float deltaY;

    public CustomSRL2(Context context) {
        super(context);
    }

    public CustomSRL2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//外部拦截 解决
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startX = ev.getX();
//                startY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                x = ev.getX();
//                y = ev.getY();
//                deltaX = Math.abs(x - startX);
//                deltaY = Math.abs(y - startY);
//                if(deltaX < deltaY){
//                    return true;
//                }else{
//                    return false;
//                }
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //父控件不拦截down事件
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                super.onInterceptTouchEvent(ev);
                return false;

        }
        return super.onInterceptTouchEvent(ev);
    }
}
