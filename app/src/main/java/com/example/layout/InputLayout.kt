package com.example.layout

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.*
import android.text.style.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FontSpan(private val font: FontType) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint?) {
        this.updateFont(paint)
    }

    override fun updateMeasureState(paint: TextPaint) {
        this.updateFont(paint)
    }

    private fun updateFont(paint: Paint?) {
        paint?.apply {
            typeface = font.typeface
            textSize = font.size.sp
        }
    }
}

class InputLayout : TextInputLayout {

    class Appearance {
        @ColorInt
        var textColor: Int = Color.dark
        var font: FontType? = Font.medium(FontSize.Footnote)

        @ColorInt
        var placeholderColor: Int = Color.lightPlaceholderColor

        @ColorInt
        var errorPlaceholderColor: Int = Color.dustyOrange

        var placeholderFont: FontType? = Font.regular(FontSize.Footnote)

        @StyleRes
        var hintTextAppearance: Int = R.style.TextInputLayoutHintText

        @StyleRes
        var errorTextAppearance: Int = R.style.TextInputLayoutErrorText

        @Px
        var startPadding: Int = 0.dp

        @Px
        var topPadding: Int = 0.dp

        @Px
        var endPadding: Int = 0.dp

        @Px
        var bottomPadding: Int = 0.dp

        companion object {
            val default: Appearance = Appearance()
        }
    }

    // region -- Properties --
    private var appearance: Appearance = Appearance.default

    var placeholder: String? = null
        set(value) {
            field = value
            this.updateEditTextHint()
        }

    var floatingPlaceholder: String? = null
        set(value) {
            field = value
            this.updateTextInputLayoutHint()
        }

    private var placeholderFont: FontType? = appearance.placeholderFont
        set(value) {
            field = value
            this.updateEditTextHint()
        }

    @ColorInt
    var placeholderTextColor: Int = appearance.placeholderColor
        set(value) {
            field = value
            this.updateEditTextHint()
        }

    @ColorInt
    var errorPlaceholderTextColor: Int = appearance.errorPlaceholderColor
        set(value) {
            field = value
            this.updateEditTextHint()
        }

    private var alwaysShowsFloatingPlaceholder: Boolean = false
        set(value) {
            field = value
            this.isExpandedHintEnabled = !value
            this.updateTextInputLayoutHint()
        }

    private val editText: TextInputEditText by lazy {
        return@lazy TextInputEditText(context).apply {
            this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            this.background = null
            this.setTextColor(appearance.textColor)
            this.applyStyle(font = appearance.font, color = appearance.textColor)
            this.addTextChangedListener(textWatcher)
            this.maxLines = 1
        }
    }
    // endregion

    // region -- Overrides --
    override fun setError(errorText: CharSequence?) {
        super.setError(errorText)
        this.updateEditTextHint()
    }
    // endregion

    // region -- Instance Variables --
    private val textWatcher: TextWatcher by lazy {
        return@lazy object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                updateTextInputLayoutHint()
            }
        }
    }
    // endregion

    // region -- INIT --
    constructor(context: Context) : super(context) {
        this.setUpAppearance(attrs = null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.setUpAppearance(attrs = attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setUpAppearance(attrs = attrs)
    }

    init {
        this.addView(editText)
        editText.inputType = InputType.TYPE_CLASS_TEXT
    }
    // endregion

    // region -- Appearance --
    data class Attribute(@StyleableRes val index: Int, val defaultValue: Any)

    private fun setUpAppearance(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.InputLayout)

        val hintTextAppearance = attributes.getResourceId(
            R.styleable.InputLayout_hintTextAppearance,
            appearance.hintTextAppearance
        )
        this.setHintTextAppearance(hintTextAppearance)

        val errorTextAppearance = attributes.getResourceId(
            R.styleable.InputLayout_errorTextAppearance,
            appearance.errorTextAppearance
        )
        this.setErrorTextAppearance(errorTextAppearance)

        val paddingValues = HashMap<Int, Int>()
        listOf(
            Attribute(R.styleable.InputLayout_android_paddingStart, appearance.startPadding),
            Attribute(R.styleable.InputLayout_android_paddingTop, appearance.topPadding),
            Attribute(R.styleable.InputLayout_android_paddingEnd, appearance.endPadding),
            Attribute(R.styleable.InputLayout_android_paddingBottom, appearance.bottomPadding)
        ).forEach { attribute ->
            val defaultValue = attribute.defaultValue as? Int ?: 0
            paddingValues[attribute.index] = if (attributes.hasValue(attribute.index)) {
                attributes.getDimensionPixelOffset(attribute.index, defaultValue)
            } else {
                defaultValue
            }
        }

        val startPadding =
            paddingValues[R.styleable.InputLayout_android_paddingStart] ?: appearance.startPadding
        val topPadding =
            paddingValues[R.styleable.InputLayout_android_paddingTop] ?: appearance.topPadding
        val endPadding =
            paddingValues[R.styleable.InputLayout_android_paddingEnd] ?: appearance.endPadding
        val bottomPadding =
            paddingValues[R.styleable.InputLayout_android_paddingBottom] ?: appearance.bottomPadding
        this.setPadding(startPadding, topPadding, endPadding, bottomPadding)

        attributes.recycle()

        errorIconDrawable = null
        this.updateEditTextHint()
    }
    // endregion

    // region -- Helpers --
    private fun updateTextInputLayoutHint() {
        val text = editText.text?.toString()
        val hint = if (alwaysShowsFloatingPlaceholder || (null != text && text.isNotEmpty())) {
            floatingPlaceholder
        } else {
            null
        }

        this.hint = hint
    }

    private fun updateEditTextHint() {
        val text = this.placeholder ?: return
        val placeholder = SpannableString(text)
        placeholder.setSpan(
            StyleSpan(Typeface.NORMAL),
            0,
            placeholder.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        placeholderFont?.let {
            placeholder.setSpan(
                FontSpan(it),
                0,
                placeholder.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }

        placeholder.setSpan(
            ForegroundColorSpan(
                if (error == null) {
                    placeholderTextColor
                } else {
                    errorPlaceholderTextColor
                }
            ), 0, placeholder.length, 0
        )
        editText.hint = placeholder
    }
    // endregion
}