package com.kachidoki.ma.kimgpicker.Widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kachidoki.ma.kimgpicker.Adapter.PopFolderAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.R;


/**
 * Created by Kachidoki on 2017/6/12.
 */


public class PopFolderWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;
    private final View masker;
    private final View marginView;
    private int marginPx;

    public PopFolderWindow(Context context, PopFolderAdapter adapter) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.pop_folder, null);
        masker = view.findViewById(R.id.masker);
        masker.setOnClickListener(this);
        marginView = view.findViewById(R.id.margin);
        marginView.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.pop_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  //如果不设置，就是 AnchorView 的宽度
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(0);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int maxHeight = view.getHeight() * 5 / 8;
                int realHeight = recyclerView.getHeight();
                ViewGroup.LayoutParams listParams = recyclerView.getLayoutParams();
                listParams.height = realHeight > maxHeight ? maxHeight : realHeight;
                recyclerView.setLayoutParams(listParams);
                LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) marginView.getLayoutParams();
                marginParams.height = marginPx;
                marginView.setLayoutParams(marginParams);
                enterAnimator();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ImgFolder folder,int positon) {
                if (onItemClickListener!=null) onItemClickListener.onItemClick(folder,positon);
            }
        });
    }

    private void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(recyclerView, "translationY", recyclerView.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {
        exitAnimator();
    }

    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(recyclerView, "translationY", 0, recyclerView.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PopFolderWindow.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public void setMargin(int marginPx) {
        this.marginPx = marginPx;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnItemClickListener {
        void onItemClick(ImgFolder folder,int position);
    }
}
