package com.shahbaz.farming.datamodel.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pictures(
    val data: List<DataX> = listOf(),
    val links: LinksX
):Parcelable