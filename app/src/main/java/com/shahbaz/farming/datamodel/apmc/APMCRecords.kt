package com.shahbaz.farming.datamodel.apmc

data class APMCRecords(
    val state: String,
    val district: String,
    val market: String,
    val commodity: String,
    val min_price: String,
    val max_price: String,
    val modal_price: String,
)
