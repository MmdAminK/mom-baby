package com.app.mombaby.models.adapterModels

import android.os.Parcel
import android.os.Parcelable

data class Question(
    var questionTitle: String? = "",
    var optionList: ArrayList<Option> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    ) {
        optionList.apply {
            parcel.readArrayList(Option::class.java.classLoader)
        }
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(questionTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}