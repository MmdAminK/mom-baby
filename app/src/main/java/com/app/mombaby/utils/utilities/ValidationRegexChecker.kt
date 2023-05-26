package com.app.mombaby.utils.utilities

import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidationRegexChecker {

    fun phoneNumberWithLength(phone: String): Boolean {
        val pattern = Pattern.compile("^09[0-9]{9}$")
        return pattern.matcher(phone).find()
    }

    fun phoneNumber(phone: String): Boolean {
        val pattern = Pattern.compile("^09.*$")
        return pattern.matcher(phone).find()
    }

    private val validEmailRegex: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun emailValidator(emailStr: String?): Boolean {
        val matcher: Matcher = validEmailRegex.matcher(emailStr!!)
        return matcher.find()
    }
}