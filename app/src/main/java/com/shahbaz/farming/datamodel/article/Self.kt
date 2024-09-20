package com.shahbaz.farming.datamodel.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Self(
    val api: String ="",
    val website: String ="",
) : Parcelable