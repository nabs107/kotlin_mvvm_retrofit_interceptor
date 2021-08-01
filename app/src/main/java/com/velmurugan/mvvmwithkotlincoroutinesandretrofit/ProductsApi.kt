package com.velmurugan.mvvmwithkotlincoroutinesandretrofit

import com.google.gson.annotations.SerializedName

data class ProductsApi(val response: List<Products>): NetworkApi(type = "", status = 0, message = "")

data class Products(
    @SerializedName("product_code") val productCode: String,
    @SerializedName("lucky_draw_code") val luckyDrawCode: String
)