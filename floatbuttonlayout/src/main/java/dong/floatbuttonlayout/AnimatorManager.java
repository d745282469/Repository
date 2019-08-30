package dong.floatbuttonlayout;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/8/30 09:33
 */
public abstract class AnimatorManager {
    /**
     * 获取除了开关按钮(索引为childCount-1)之外所有子元素的展开动画
     * @param index 子元素对应的索引
     * @return 动画集合
     */
    public abstract AnimatorSet getShowAnimator(int index, View v,FloatButtonLayout layout);

    /**
     * 获取除了开关按钮(索引为childCount-1)之外所有子元素的关闭动画
     * @param index 子元素对应的索引
     * @return 动画集合
     */
    public abstract AnimatorSet getCloseAnimator(int index,View v,FloatButtonLayout layout);

    /**
     * 获取开关按钮的展开时动画
     * @return 动画集合
     */
    public abstract AnimatorSet getSwitchShowAnimator(View v,FloatButtonLayout layout);

    /**
     * 获取开关按钮的关闭时动画
     * @return 动画集合
     */
    public abstract AnimatorSet getSwitchCloseAnimator(View v,FloatButtonLayout layout);
}
