/*
 * Copyright (c) 2016. Ravi Rao.
 *
 * This file is created as part of VISA POC and  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visa.r4r.poc.herospin.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.customview.BezierView;
import com.visa.r4r.poc.herospin.customview.FloatingActionButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ravrarao on 8/21/2016.
 */
public class IntroActivity extends AppCompatActivity {
    int index = 1;
    private int screenHeight;
    private int screenWidth;

    private final int BEZIER_ENTER_DURATION = 1300;
    private final int BEZIER_EXIT_DURATION = 800;


    @BindView(R.id.bezier)
    BezierView mBezierView;

    @BindView(R.id.floating)
    FloatingActionButton mFloatingActionButton;

    @BindViews({R.id.card0, R.id.card1, R.id.card2})
    List<ImageView> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        this.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                enter();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    IntroActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @OnClick({R.id.floating})
    public void onClick(View view) {
        if (index == 1) {
            exit();
            index++;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }, BEZIER_EXIT_DURATION + BezierView.ANIMATION_DURATION + 100);

        }
    }


    public void enter() {
        mBezierView.switchLine(BezierView.TYPE_SECOND);
        Animator animator = ObjectAnimator.ofFloat(mBezierView, "baseLine", -BezierView.RANGE, mFloatingActionButton.getY() - mFloatingActionButton.getMeasuredHeight() / 2);
        //animator.setStartDelay(BezierView.ANIMATION_DURATION);
        // animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(BEZIER_ENTER_DURATION);
        animator.start();

        Animator floatingAnimaor = ObjectAnimator.ofFloat(mFloatingActionButton, "translationY", 300f, 0f);
        floatingAnimaor.setStartDelay(BezierView.ANIMATION_DURATION);
        floatingAnimaor.setDuration(BEZIER_ENTER_DURATION);
        floatingAnimaor.setInterpolator(new OvershootInterpolator());
        floatingAnimaor.start();
        floatingAnimaor.addListener(new MyAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFloatingActionButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFloatingActionButton.switchState(FloatingActionButton.CIRCLE_TO_ROUND_RECT);
            }
        });

        cardEnter();
    }

    public void exit() {
        mFloatingActionButton.switchState(FloatingActionButton.ROUND_RECT_TO_CIRCLE, BEZIER_EXIT_DURATION);
        cardExit();

        mBezierView.switchLine(BezierView.TYPE_THIRD);

        Animator animator = ObjectAnimator.ofFloat(mBezierView, "baseLine", mBezierView.getBaseLine(), 0f);
        animator.setStartDelay(BezierView.ANIMATION_DURATION);
        // animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(BEZIER_EXIT_DURATION);
        animator.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBezierView.switchLine(BezierView.TYPE_ONE);
                    }
                });

            }
        }, BEZIER_EXIT_DURATION + BezierView.ANIMATION_DURATION - 110);
    }

    public void cardEnter() {
        int delay = 100;
        for (final ImageView card : cards) {
            Animator animator = ObjectAnimator.ofFloat(card, "translationX", screenWidth - card.getLeft(), 0f);
            animator.setDuration(550);
            animator.setStartDelay(BezierView.ANIMATION_DURATION + delay * cards.indexOf(card));
            animator.start();
            animator.addListener(new MyAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    card.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void cardExit() {
        int delay = 50;
        for (final ImageView card : cards) {
            Animator animator = ObjectAnimator.ofFloat(card, "translationY", 0, -(card.getY() + card.getMeasuredHeight()));
            animator.setDuration(300 + 150 * cards.indexOf(card));
            animator.setStartDelay(delay * cards.indexOf(card));
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
        }
    }

    public class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
