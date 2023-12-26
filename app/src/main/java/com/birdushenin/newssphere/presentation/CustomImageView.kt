package com.birdushenin.newssphere.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CustomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val radius = width.coerceAtMost(height) / 2.toFloat()
        canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, paint)

        val bitmapDrawable = drawable as? BitmapDrawable
        bitmapDrawable?.bitmap?.let { bitmap ->
            val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader

            val diameter = radius * 2
            var scale = 1.0f
            if (bitmap.width != diameter.toInt() || bitmap.height != diameter.toInt()) {
                scale = diameter / bitmap.width.coerceAtMost(bitmap.height).toFloat()
            }

            val matrix = Matrix()
            matrix.setScale(scale, scale)
            matrix.postTranslate(
                -((bitmap.width * scale - width) / 2),
                -((bitmap.height * scale - height) / 2)
            )
            shader.setLocalMatrix(matrix)

            canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, paint)
        }
    }
}