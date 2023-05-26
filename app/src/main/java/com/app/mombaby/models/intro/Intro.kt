package com.app.mombaby.models.intro

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Intro(
    @SerializedName("pre_title1")
    @Expose
    val firstTitle: String?,

    @SerializedName("pre_title2")
    @Expose
    val secondTitle: String?,

    @SerializedName("pre_title3")
    @Expose
    val thirdTitle: String?,

    @SerializedName("pre_description1")
    @Expose
    val firstDescription: String?,

    @SerializedName("pre_description2")
    @Expose
    val secondDescription: String?,

    @SerializedName("pre_description3")
    @Expose
    val thirdDescription: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstTitle)
        parcel.writeString(secondTitle)
        parcel.writeString(thirdTitle)
        parcel.writeString(firstDescription)
        parcel.writeString(secondDescription)
        parcel.writeString(thirdDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Intro> {
        override fun createFromParcel(parcel: Parcel): Intro {
            return Intro(parcel)
        }

        override fun newArray(size: Int): Array<Intro?> {
            return arrayOfNulls(size)
        }
    }
}
