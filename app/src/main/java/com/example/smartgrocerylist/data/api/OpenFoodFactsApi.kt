package com.example.smartgrocerylist.data.api

import com.example.smartgrocerylist.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("product/{barcode}")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = "product_name,nutrition_grades,nutriments"
    ): Response<ProductResponse>

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.net/api/v2/"
    }
}
