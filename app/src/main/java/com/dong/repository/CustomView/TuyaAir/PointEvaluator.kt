package com.dong.repository.CustomView.TuyaAir

import android.animation.TypeEvaluator
import android.graphics.Point

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2020/7/23 11:11
 */
class PointEvaluator : TypeEvaluator<TuYaPoint> {

    /**
     * 根据插值器给出的百分比，计算出具体的值
     *
     * @param fraction      插值器计算出来的百分比
     * @param startValue    开始位置
     * @param endValue      结束位置
     */
    override fun evaluate(fraction: Float, startValue: TuYaPoint?, endValue: TuYaPoint?): TuYaPoint {
        val x = (endValue!!.x - startValue!!.x) * fraction + startValue.x
        val y = (endValue.y - startValue.y) * fraction + startValue.y
        return TuYaPoint(x.toInt(),y.toInt(),startValue.index)
    }
}