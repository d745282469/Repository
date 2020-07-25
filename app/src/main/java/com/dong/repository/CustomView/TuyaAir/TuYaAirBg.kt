package com.dong.repository.CustomView.TuyaAir

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.dong.repository.R
import com.dong.repository.Util.Log
import kotlin.random.Random

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2020/7/23 09:44
 */
class TuYaAirBg(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int,
                defStyleRes: Int) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val paint = Paint()
    private val circlePoints = ArrayList<TuYaPoint>() // 记录所有小圆的坐标

    var circleRadius: Int // 小圆的半径，px
    var circleColor: Int
    var maxCircleCount: Int // 小圆的数量

    init {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.TuYaAirBg, defStyleAttr, defStyleRes)
        circleRadius = typeArray.getInteger(R.styleable.TuYaAirBg_circleRadius, 10)
        circleColor = typeArray.getColor(R.styleable.TuYaAirBg_circleColor, Color.parseColor("#5fbfb7"))
        maxCircleCount = typeArray.getInteger(R.styleable.TuYaAirBg_circleCount, 5)
        typeArray.recycle()

        paint.color = circleColor
        paint.style = Paint.Style.FILL
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        computeCircle()
    }

    /**
     * 计算小球的位置
     */
    private fun computeCircle() {
        val x1 = -circleRadius
        val x2 = measuredWidth + circleRadius
        val y1 = -circleRadius
        val y2 = measuredHeight + circleRadius

        circlePoints.clear()
        var randomCount = maxCircleCount // 随机数量

        // 找到最接近4的倍数
        val remainder = randomCount % 4
        randomCount = if (remainder >= randomCount / 2) {
            randomCount + remainder + randomCount - 4
        } else {
            randomCount - remainder
        }

        for (index in 0 until randomCount / 4) {
            circlePoints.add(TuYaPoint(Random.nextInt(x1, 0), Random.nextInt(y1, y2), index))
        }
        for (index in 0 until randomCount / 4) {
            circlePoints.add(TuYaPoint(Random.nextInt(x1, x2), Random.nextInt(y1, 0), index + circlePoints.size))
        }
        for (index in 0 until randomCount / 4) {
            circlePoints.add(TuYaPoint(Random.nextInt(measuredWidth, x2), Random.nextInt(y1, y2), index + circlePoints.size))
        }
        for (index in 0 until randomCount / 4) {
            circlePoints.add(TuYaPoint(Random.nextInt(x1, x2), Random.nextInt(measuredHeight, y2), index + circlePoints.size))
        }
        Log.d("dong", "数量：${circlePoints.size}")
    }

    fun startAnimation() {
        val circlePoint = TuYaPoint(measuredWidth / 2, measuredHeight / 2, -1)
        val animationSet = AnimatorSet()
//        animationSet.duration = 4000
        animationSet.interpolator = AccelerateDecelerateInterpolator()

        val list = ArrayList<Animator>()
        for (index in circlePoints.indices) {
            val pointAnimator = ObjectAnimator.ofObject(this, "TuYaPoint", PointEvaluator(), circlePoints[index], circlePoint)
            val alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
            alphaAnimator.duration = 3000
            pointAnimator.duration = 4000

            list.add(pointAnimator)
            list.add(alphaAnimator)
        }

        animationSet.playTogether(list)
        animationSet.start()
        animationSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                computeCircle()
                invalidate()
                startAnimation()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    fun setTuYaPoint(point: TuYaPoint) {
        Log.d("dong", "设置点位:$point")
        circlePoints.removeAt(point.index)
        circlePoints.add(point.index, point)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        for (index in circlePoints.indices) {
            canvas.drawCircle(circlePoints[index].x.toFloat(), circlePoints[index].y.toFloat(), circleRadius.toFloat(), paint)
        }
    }
}