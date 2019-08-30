package com.dong.repository.Show;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dong.repository.R;

import dong.floatbuttonlayout.AnimatorManager;
import dong.floatbuttonlayout.FloatButtonLayout;

public class ShowFloatBtnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_float_btn);

        FloatButtonLayout layout = findViewById(R.id.fbl);
//        layout.setAnimatorManager(new AnimatorManager());
    }

    private class AnimatorManager extends dong.floatbuttonlayout.AnimatorManager{

        @Override
        public AnimatorSet getShowAnimator(int index, View v, FloatButtonLayout layout) {
            AnimatorSet set = new AnimatorSet();
            int itemHeight = v.getHeight();
            float moveY = (layout.getChildCount() - 1 - index) * itemHeight +
                    (layout.getChildCount() - 1 - index) * layout.getItemMargin();
            moveY *= -1;
            ObjectAnimator tranY = ObjectAnimator.ofFloat(v,"translationY",moveY);
            tranY.setDuration(200 * (layout.getChildCount() - index));
            set.play(tranY);
            return set;
        }

        @Override
        public AnimatorSet getCloseAnimator(int index, View v,FloatButtonLayout layout) {
            return null;
        }

        @Override
        public AnimatorSet getSwitchShowAnimator(View v,FloatButtonLayout layout) {
            AnimatorSet set = new AnimatorSet();
            int itemHeight = v.getHeight();
            float moveY = (layout.getChildCount() - 1) * itemHeight +
                    (layout.getChildCount() - 1) * layout.getItemMargin();
            moveY *= -1;
            ObjectAnimator tranY = ObjectAnimator.ofFloat(v,"translationY",moveY);
            tranY.setDuration(200 * layout.getChildCount());
            set.play(tranY);
            return set;
        }

        @Override
        public AnimatorSet getSwitchCloseAnimator(View v,FloatButtonLayout layout) {
            return null;
        }
    }
}
