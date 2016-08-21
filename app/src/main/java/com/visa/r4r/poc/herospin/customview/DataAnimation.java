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

package com.visa.r4r.poc.herospin.customview;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

/**
 * @author LiYanZhao
 * @date 16-3-19 下午5:13
 */
public class DataAnimation implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    ValueAnimator mValueAnimator;
    private DataAnimationListener mDataAnimationListener;

    private final long DEFAULT_DURATION = 500;

    public DataAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mValueAnimator.addListener(this);
        mValueAnimator.addUpdateListener(this);

        mDataAnimationListener = new DataAnimationListener() {
            @Override
            public void onAnimationUpdate(float scale) {

            }

            @Override
            public void onAnimationFinish() {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

        };
    }

    public void startAnimation(long duration) {
        if (duration >= 0) {
            mValueAnimator.setDuration(duration);
        } else {
            mValueAnimator.setDuration(DEFAULT_DURATION);
        }
        mValueAnimator.start();
    }

    public void cancelAnimation() {
        mValueAnimator.cancel();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mDataAnimationListener.onAnimationUpdate(animation.getAnimatedFraction());
    }

    public void setInterpolator(TimeInterpolator interpolator){
        mValueAnimator.setInterpolator(interpolator);
    }

    public void setStartDelay(long startDelay){
        mValueAnimator.setStartDelay(startDelay);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mDataAnimationListener.onAnimationStart(animation);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mDataAnimationListener.onAnimationFinish();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void setDataAnimationListener(DataAnimationListener dataAnimationListener) {
        if (null != dataAnimationListener) {
            this.mDataAnimationListener = dataAnimationListener;
        }
    }

}
