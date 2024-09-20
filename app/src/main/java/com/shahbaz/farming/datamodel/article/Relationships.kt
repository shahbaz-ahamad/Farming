package com.shahbaz.farming.datamodel.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Relationships(
    val companions: Companions,
    val pictures: Pictures
):Parcelable