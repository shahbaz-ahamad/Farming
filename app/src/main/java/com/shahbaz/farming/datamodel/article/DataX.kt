package com.shahbaz.farming.datamodel.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataX(
    val id: String ="",
    val type: String=""
):Parcelable