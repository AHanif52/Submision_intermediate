package com.example.mystoryapps.ui.customview

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapps.R

class InputPass: AppCompatEditText {
    var isPassword: Boolean = false
    private var icon: Drawable? = null

    private val errorIcon: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.baseline_error_24)!!
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24)
        setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null)

        hint = context.getString(R.string.enter_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkForErrors()
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun checkForErrors() {
        val password = text?.toString()?.trim()
        if (password.isNullOrEmpty()) {
            isPassword = false
            error = resources.getString(R.string.input_pass)

        } else if (password.length < 8) {
            isPassword = false
            error = resources.getString(R.string.pass_length)
            setButtonDrawables(startOfTheText = errorIcon)
        } else {
            isPassword = true
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) checkForErrors()
    }
}