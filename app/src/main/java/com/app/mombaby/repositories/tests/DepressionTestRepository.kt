package com.app.mombaby.repositories.tests

import android.os.Parcel
import android.os.Parcelable
import com.app.mombaby.data.DepressionTestData
import com.app.mombaby.models.adapterModels.Option
import com.app.mombaby.models.adapterModels.Question

class DepressionTestRepository(
    private val name: String? = "تست افسردگی",
    private val questions: ArrayList<Question> = DepressionTestData.questions
) : TestRepositoryImpl, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()
    ) {
        questions.apply {
            parcel.readArrayList(Question::class.java.classLoader)
        }
    }

    override fun name(): String? {
        return name
    }

    override fun calculateResult(): Any {
        var result = 0f
        questions.forEachIndexed { _, question ->
            question.optionList.forEach { if (it.isSelected) result += it.score }
        }
        return result
    }

    override fun result(): String {
        return when (calculateResult() as Float) {
            in 0.0..13.0 -> "بدون افسردگی یا در کمترین حالت افسردگی"
            in 14.0..19.0 -> "افسردگی خفیف"
            in 20.0..28.0 -> "افسردگی متوسط"
            in 29.0..63.0 -> "افسردگی شدید"
            else -> "بدون نتیجه"
        }
    }

    override fun questions(): ArrayList<Question> {
        return questions
    }

    override fun questionsSize(): Int {
        return questions.size
    }

    override fun options(pos: Int): ArrayList<Option> {
        return questions[pos].optionList
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DepressionTestRepository> {
        override fun createFromParcel(parcel: Parcel): DepressionTestRepository {
            return DepressionTestRepository(parcel)
        }

        override fun newArray(size: Int): Array<DepressionTestRepository?> {
            return arrayOfNulls(size)
        }
    }

}