package com.app.mombaby.models.pregnancyHome

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PregnancyArticle")
data class PregnancyArticle(
    @SerializedName("id")
    @PrimaryKey
    @Expose
    val id: Int,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    @Expose
    val title: String = "",

    @SerializedName("description")
    @ColumnInfo(name = "description")
    @Expose
    val description: String = "",

    @SerializedName("pic")
    @ColumnInfo(name = "image")
    @Expose
    val image: String = "",

    @SerializedName("article_cat_id")
    @ColumnInfo(name = "articleCatId")
    @Expose
    val catId: Int,

    @SerializedName("view")
    @ColumnInfo(name = "view")
    @Expose
    val view: Int,

    @SerializedName("time_create")
    @ColumnInfo(name = "timeCreate")
    @Expose
    val timeCreate: Int,

    ) {
}