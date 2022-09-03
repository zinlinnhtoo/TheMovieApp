package com.example.themovieapp.persistence.typeconverters

import androidx.room.TypeConverter
import com.example.themovieapp.data.vos.ProductionCompanyVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductionCompanyTypeConverter {
    @TypeConverter
    fun toString(productionCompanyList: List<ProductionCompanyVO>?): String {
        return Gson().toJson(productionCompanyList)
    }

    @TypeConverter
    fun toProductionCompanyList(productionCompaniesJsonString: String): List<ProductionCompanyVO>? {
        val productionCompanyListType = object : TypeToken<List<ProductionCompanyVO>?>() {}.type
        return Gson().fromJson(productionCompaniesJsonString, productionCompanyListType)
    }
}