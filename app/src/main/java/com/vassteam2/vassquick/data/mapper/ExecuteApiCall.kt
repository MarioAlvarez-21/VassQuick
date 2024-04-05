package com.vassteam2.vassquick.data.mapper

import com.google.gson.Gson
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.data.response.ErrorResponse
import retrofit2.Response

suspend fun <R, M> executeApiCall(
    call: suspend () -> Response<R>,
    map: (R) -> M
): ApiResponse<M> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResponse.Success(map(body))
            } else {
                throw Exception("Error. Please try again later")
            }
        } else {
            var errorResponse: ErrorResponse
            try {
                val gson = Gson()
                errorResponse = gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            }
            catch (e: Exception){
                throw Exception("Error. Please try again later")
            }
            var message = "Error. Please try again later."
            if(errorResponse.message != null){
                message = errorResponse.message
            }else if(errorResponse.success != null){
                message = errorResponse.success
            }
            throw Exception(message)
        }
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }
}
suspend fun <R, M> executeApiCallBoolean(
    call: suspend () -> Response<R>,
    map: (R) -> M
): ApiResponse<M> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResponse.Success(map(body))
            } else {
                throw Exception("Error. Please try again later")
            }
        } else {
            var errorResponse: ErrorResponse
            try {
                val gson = Gson()
                errorResponse = gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            }
            catch (e: Exception){
                throw Exception("Error. Please try again later")
            }
            var message = "Error. Please try again later."
            if(errorResponse.message != null){
                message = errorResponse.message
            }else if(errorResponse.success != null){
                message = errorResponse.success
            }
            throw Exception(message)
        }
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }
}


suspend fun <R, M> executeApiCallList(
    call: suspend () -> Response<List<R>>,
    map: (R) -> M
): ApiResponse<List<M>> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResponse.Success(body.map(map))
            } else {
                throw Exception("Error. Please try again later")
            }
        } else {
            var errorResponse: ErrorResponse
            try {
                val gson = Gson()
                errorResponse = gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            }
            catch (e: Exception){
                throw Exception("Error. Please try again later")
            }
            throw Exception(errorResponse.message)
        }
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }
}


suspend fun <R, M> executeApiCallAndFilterById(
    call: suspend () -> Response<List<R>>,
    map: (R) -> M,
    filter: (R) -> Boolean,
): ApiResponse<M> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val filteredItem = body.find { filter(it) }
                if (filteredItem != null) {
                    ApiResponse.Success(map(filteredItem))
                } else {
                    throw Exception("Item not found")
                }
            } else {
                throw Exception("Error. Please try again later")
            }
        } else {
            var errorResponse: ErrorResponse
            try {
                val gson = Gson()
                errorResponse = gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            }
            catch (e: Exception){
                throw Exception("Error. Please try again later")
            }
            throw Exception(errorResponse.message)
        }
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }
}