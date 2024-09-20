package com.shahbaz.farming.datamodel.article

import Attributes
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val attributes: Attributes,
    val id: String ="",
    val links: Links,
    val relationships: Relationships,
    val type: String =""
):Parcelable