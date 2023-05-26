package com.app.mombaby.models.pregnancyHome.categories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PregnancyCategory")
data class PregnancyCategory(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    @Expose
    val id: String,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    @Expose
    val title: String,

    @ColumnInfo(name = "type")
    @Expose
    var type: Int? = 0,

    var isSelected: Boolean = false,
)