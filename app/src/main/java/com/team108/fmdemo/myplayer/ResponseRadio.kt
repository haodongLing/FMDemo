package com.team108.zzfm.model.response

import com.google.gson.annotations.SerializedName

data class ResponseRadio(
    val result: List<RadioItem>
) {
    companion object {
        const val RADIO_TYPE_ARTICLE    = "article"
        const val RADIO_TYPE_STORY      = "story"
        const val RADIO_TYPE_OCCUPATION = "occupation"
        const val RADIO_TYPE_GENERALFM  = "fm"
        const val RADIO_TYPE_XDPFM      = "xdp_fm"
    }
}

data class RadioItem(
    @SerializedName("country_id") val countryId: Int,
    @SerializedName("country_info") val countryInfo: CountryInfo,
    val desc: String,
    val id: Int,
    /**
     * [ResponseRadio.RADIO_TYPE_ARTICLE]
     * [ResponseRadio.RADIO_TYPE_STORY]
     * [ResponseRadio.RADIO_TYPE_OCCUPATION]
     * [ResponseRadio.RADIO_TYPE_GENERALFM]
     * [ResponseRadio.RADIO_TYPE_XDPFM]
     */
    val type: String,
    val image: String,
    @SerializedName("is_vip") val isVip: Int,
    val name: String,
    val title: String,
    @SerializedName("voice_url") val voiceUrl: String,
    @SerializedName("qr_code_url") val qrCodeUrl: String,
    @SerializedName("voice_url_list") val voiceUrlList: List<String>
)

data class CountryInfo(
    @SerializedName("flag_url") val flagUrl: String,
    val id: Int,
    val name: String
)