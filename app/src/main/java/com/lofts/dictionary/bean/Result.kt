package com.lofts.dictionary.bean

/**
 * PACKAGE_NAME：com.lofts.dictionary.bean
 * DATE：2018-12-18 23:08
 * USER: asus
 * DESCRIBE:
 */
data class Result(
    var word: String,
    var pronunciation: Pronunciation,
    var defs: List<Define>,
    var sams: List<Sample>
)