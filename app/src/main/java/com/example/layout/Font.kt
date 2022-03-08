package com.example.layout

import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat

class FontType private constructor(var typeface: Typeface?, var size: Float) {
    companion object {
        fun valueOf(typeface: Typeface?, size: Float): FontType? {
            if (null == typeface) {
                return null
            }
            return FontType(typeface = typeface, size = size)
        }
    }
}

enum class FontSize(val rawValue: Float) {
    Footnote(13.0F),
}

object Font {
    class Name {
        companion object {
            val regular: Typeface? by lazy {
                ResourcesCompat.getFont(applicationContext, R.font.regular)
            }
            val medium: Typeface? by lazy {
                ResourcesCompat.getFont(applicationContext, R.font.medium)
            }
        }
    }

    fun regular(size: FontSize): FontType? {
        return FontType.valueOf(typeface = Name.regular, size = size.rawValue)
    }

    fun medium(size: FontSize): FontType? {
        return FontType.valueOf(typeface = Name.medium, size = size.rawValue)
    }
}