package com.app.mombaby.models.intro

data class IntroUiModel(
    val title: String? = "",
    val description: String? = "",
    val imageView: Int? = null,
    val imageUrl: String? = "",
    val page: Int = 1,
    val isFirstPage: Boolean = false,
    val isLastPage: Boolean = false,
    val onNextClick: (() -> Unit)? = null,
    val onPreviousClick: (() -> Unit)? = null
) {
}