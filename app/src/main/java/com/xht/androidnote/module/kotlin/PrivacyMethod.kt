package com.xht.androidnote.module.kotlin

data class PrivacyMethod(
    val methods: List<PrivacyMethodData>,
)

data class PrivacyMethodData(val name_regex: String, val message: String)