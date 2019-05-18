package com.team108.fmdemo

import com.google.gson.annotations.SerializedName

data class RadioItem(
    val title: String,
    val url: String,
    val http: String,
    val imgurl: String
)

data class RadioItem2(
    val name: String,
    @SerializedName("media") val url: String,
    val icon: String
)