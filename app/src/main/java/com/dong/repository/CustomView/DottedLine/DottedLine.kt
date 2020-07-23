package com.dong.repository.CustomView.DottedLine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2020/7/22 15:57
 */
@SuppressLint("ViewConstructor")
class DottedLine(context: Context,
                 attrs: AttributeSet?,
                 defStyleAttr: Int,
                 defStyleRes: Int)
    : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val paint = Paint()
    var singleLineWidth = 20 // 单个线条的宽度，单位px
    var singleLineHeight = 4 // 单个线条的高度，单位px
    var lineMargin = 10 // 线条之间的间距，单位px

    private var maxLines = 0 // 最大可以绘制多少条线

    init {
        paint.color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 计算可以绘制的线的数量
        maxLines = measuredWidth / (singleLineWidth + lineMargin)
        singleLineHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        for (index in 0 until maxLines) {
            canvas.drawRect((index * (singleLineWidth + lineMargin)).toFloat(),
                    0f,
                    (index * (singleLineWidth + lineMargin)).toFloat() + singleLineWidth,
                    singleLineHeight.toFloat(),
                    paint)
        }
    }
}