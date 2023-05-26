package com.app.mombaby.models.intro

object IntroData {
    lateinit var intro: Intro
    fun firstTitle(): String? = intro.firstTitle
    fun secondTitle(): String? = intro.secondTitle
    fun thirdTitle(): String? = intro.thirdTitle
    fun firstDescription(): String? = intro.firstDescription
    fun secondDescription(): String? = intro.secondDescription
    fun thirdDescription(): String? = intro.thirdDescription
}