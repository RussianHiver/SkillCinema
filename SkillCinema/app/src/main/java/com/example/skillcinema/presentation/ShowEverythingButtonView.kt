package com.example.skillcinema.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ShowEverythingButtonBinding

class ShowEverythingButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: ShowEverythingButtonBinding
    init {
        val inflateView =  inflate(context, R.layout.show_everything_button, this)
       binding = ShowEverythingButtonBinding.bind(inflateView)
    }
}