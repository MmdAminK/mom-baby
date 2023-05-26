package com.app.mombaby.models.pregnancyHome

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PregnancyBlog")
data class PregnancyBlog(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Expose
    val id: Int,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    @Expose
    val title: String? = "",

    @SerializedName("description")
    @ColumnInfo(name = "description")
    @Expose
    val description: String? = "",

    @SerializedName("pic")
    @ColumnInfo(name = "image")
    @Expose
    val image: String? = "",

    @SerializedName("pregnancy_guide_cat_id", alternate = ["article_cat_id"])
    @ColumnInfo(name = "categoryId")
    @Expose
    val categoryId: String?,

    @SerializedName("week")
    @ColumnInfo(name = "week")
    @Expose
    val week: Int,


    @SerializedName("view")
    @ColumnInfo(name = "view")
    @Expose
    var view: Int,

    @SerializedName("time_create")
    @ColumnInfo(name = "timeCreate")
    @Expose
    val timeCreate: String?,

    @ColumnInfo(name = "type")
    var type: Int = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(categoryId)
        parcel.writeInt(week)
        parcel.writeInt(view)
        parcel.writeString(timeCreate)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PregnancyBlog> {
        override fun createFromParcel(parcel: Parcel): PregnancyBlog {
            return PregnancyBlog(parcel)
        }

        override fun newArray(size: Int): Array<PregnancyBlog?> {
            return arrayOfNulls(size)
        }
    }
}

enum class PregnancyBlogsType {
    GUIDE, ARTICLE, MAINARTICLE
}