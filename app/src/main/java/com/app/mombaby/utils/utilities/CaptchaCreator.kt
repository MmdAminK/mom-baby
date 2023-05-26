package com.app.mombaby.utils.utilities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.*

class CaptchaCreator private constructor() {
    private var paddingLeft = 0
    private var paddingTop = 0
    private val stringBuilder = StringBuilder()
    private val random = Random()

    var code: String? = null
        private set

    fun createBitmap(): Bitmap {
        paddingLeft = 0
        paddingTop = 0
        val bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        code = createCode()
        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR))
        val paint = Paint()
        paint.textSize = DEFAULT_FONT_SIZE.toFloat()

        for (element in code!!) {
            randomTextStyle(paint)
            randomPadding()
            canvas.drawText(
                element.toString(),
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                paint
            )
        }
        for (j in 0 until DEFAULT_LINE_NUMBER) {
            drawLine(canvas, paint)
        }
        canvas.save()
        canvas.restore()
        return bitmap
    }

    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = random.nextInt(DEFAULT_WIDTH)
        val startY = random.nextInt(DEFAULT_HEIGHT)
        val stopX = random.nextInt(DEFAULT_WIDTH)
        val stopY = random.nextInt(DEFAULT_HEIGHT)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    private fun randomColor(): Int {
        stringBuilder.delete(0, stringBuilder.length)
        var haxString: String
        for (i in 0..2) {
            haxString = Integer.toHexString(random.nextInt(0xFF))
            if (haxString.length == 1) {
                haxString = "0$haxString"
            }
            stringBuilder.append(haxString)
        }
        return Color.parseColor("#$stringBuilder")
    }

    private fun randomPadding() {
        paddingLeft += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT)
        paddingTop = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP)
    }

    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        paint.isFakeBoldText = random.nextBoolean()
        var skewX = (random.nextInt(11) / 10).toFloat()
        skewX = if (random.nextBoolean()) skewX else -skewX
        paint.textSkewX = skewX
    }

    private fun createCode(): String {
        stringBuilder.delete(0, stringBuilder.length)
        for (i in 0 until DEFAULT_CODE_LENGTH) {
            stringBuilder.append(CHARS[random.nextInt(CHARS.size)])
        }
        return stringBuilder.toString()
    }

    companion object {
        private val CHARS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        private var mCaptchaCreator: CaptchaCreator? = null
        private const val DEFAULT_CODE_LENGTH = 4
        private const val DEFAULT_FONT_SIZE = 60
        private const val DEFAULT_LINE_NUMBER = 3
        private const val BASE_PADDING_LEFT = 40
        private const val RANGE_PADDING_LEFT = 30
        private const val BASE_PADDING_TOP = 70
        private const val RANGE_PADDING_TOP = 15
        private const val DEFAULT_WIDTH = 300
        private const val DEFAULT_HEIGHT = 100
        private const val DEFAULT_COLOR = 0xDF
        val instance: CaptchaCreator?
            get() {
                if (mCaptchaCreator == null) {
                    mCaptchaCreator = CaptchaCreator()
                }
                return mCaptchaCreator
            }
    }
}
