package com.test.main;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.lazyload.BaseLazyLoadFragment;
import com.test.lazyload.R;

import org.jetbrains.annotations.NotNull;

public class MyLazyLoadFragment extends BaseLazyLoadFragment {

    private static final String TAG = MyLazyLoadFragment.class.getSimpleName();

    public static final String INTENT_INT_INDEX = "index";

    ImageView imageView;
    TextView textView;
    int tabIndex;
    CountDownTimer con;

    public static MyLazyLoadFragment newInstance(int tabIndex) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_INT_INDEX, tabIndex);
        MyLazyLoadFragment fragment = new MyLazyLoadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, tabIndex + " fragment " + "onAttach: ");
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, tabIndex + " fragment " + "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, tabIndex + " fragment " + "onResume: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, tabIndex + " fragment " + "onPause: " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, tabIndex + " fragment " + "onDetach: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        con.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        Log.i(TAG, tabIndex + " fragment " + "setUserVisibleHint: " + isVisibleToUser );
    }

    private void getData() {
        con= new CountDownTimer(1000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                handler.sendEmptyMessage(0);
            }
        };
        con.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setVisibility(View.GONE);
            int id ;
            switch(tabIndex) {
                case 1:
                    id = R.drawable.a;
                    break;
                case 2:
                    id = R.drawable.b;
                    break;
                case 3:
                    id = R.drawable.c;
                    break;
                case 4:
                    id = R.drawable.d;
                    break;
                default:
                    id = R.drawable.a;
            }

            // 这是我注释掉的，备用的代码而已哦，同学们
            /*imageView.setImageResource(id);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setVisibility(View.VISIBLE);
            Log.d(TAG, tabIndex +" handleMessage: " );
            //模拟耗时工作
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    };

    @Override
    public void onLazyLoad() {
        Log.d(TAG, tabIndex + " fragment " + "onLazyLoad: " );
    }

    @Override
    public void onStopLazyLoad() {
        Log.d(TAG, tabIndex + " fragment " + "onStopLazyLoad: " );
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lazy_loading;
    }

    @Override
    public void initView(@NotNull View view) {
        imageView = view.findViewById(R.id.iv_content);
        textView = view.findViewById(R.id.tv_loading);
        getData();
    }
}
