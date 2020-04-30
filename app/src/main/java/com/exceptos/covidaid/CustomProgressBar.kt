package com.exceptos.covidaid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt


class CustomProgressBar : View {

    // % value of the progressbar.
    internal var progressBarValue = 0
    internal var maxprogressValue = 100
    internal var setText = false
    internal var colorProgress: Int = DKGRAY

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        progressBarValue = attrs.getAttributeIntValue(null, "progressBarValue", 0)
    }

    fun setValue(value: Int) {
        progressBarValue = value
        invalidate()
    }

    fun setProgressColor(value: Int) {
        colorProgress = value
        invalidate()
    }

    fun setMaxValue(value: Int) {
        maxprogressValue = value
        invalidate()
    }

    fun setText(value: Boolean) {
        setText = value
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cornerRadius = 30.0f

        // Draw the background of the bar.
        val backgroundPaint = Paint()
        backgroundPaint.color = LTGRAY
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.isAntiAlias = true

        val backgroundRect = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint)

        // Draw the progress bar.
        val barPaint = Paint()
        barPaint.color = colorProgress
        barPaint.style = Paint.Style.FILL
        barPaint.isAntiAlias = true

        val progress = backgroundRect.width() / maxprogressValue * progressBarValue
        val barRect = RectF(0f, 0f, progress, canvas.clipBounds.bottom.toFloat())

        canvas.drawRoundRect(barRect, cornerRadius, cornerRadius, barPaint)

        if(setText) {

            // Draw progress text in the middle.
            val textPaint = Paint()
            textPaint.color = WHITE
            textPaint.setTextSize(20f)

            val text = "$progressBarValue%"
            val textBounds = Rect()

            textPaint.getTextBounds(text, 0, text.length, textBounds)

            canvas.drawText(
                text,
                backgroundRect.centerX() - textBounds.width() / 2,
                backgroundRect.centerY() + textBounds.height() / 2,
                textPaint
            )
        }

    }
}