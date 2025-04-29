package com.example.smartgrocerylist.data.api

import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = "product_name,nutrition_grades,nutriments"
    ): Response<ProductResponse>

    @GET("search")
    suspend fun searchProductByName(
        @Query("search_terms") productName: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("json") json: Int = 1,
        @Query("fields") fields: String = "product_name,nutrition_grades,nutriments",
        @Query("page_size") pageSize: Int = 1000
    ): Response<SearchResponse>

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/api/v2/"
    }
}