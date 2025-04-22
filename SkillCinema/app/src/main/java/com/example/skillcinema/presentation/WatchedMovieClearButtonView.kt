package com.example.skillcinema.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.skillcinema.R
import com.example.skillcinema.databinding.WatchedMovieClearButtonBinding

class WatchedMovieClearButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: WatchedMovieClearButtonBinding
    init {
        val inflateView =  inflate(context, R.layout.watched_movie_clear_button, this)
        binding = WatchedMovieClearButtonBinding.bind(inflateView)
    }
}