package com.langcab.app

class Word (val wordChinese: String,
            val wordEnglish: String,
            val wordPinyin: String,
            val language: String) {
    constructor() : this("", "","","")
}
