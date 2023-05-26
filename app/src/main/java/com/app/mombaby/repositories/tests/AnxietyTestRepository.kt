package com.app.mombaby.repositories.tests

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.app.mombaby.data.AnxietyTestData
import com.app.mombaby.models.adapterModels.Option
import com.app.mombaby.models.adapterModels.Question

class AnxietyTestRepository(
    private val name: String? = "تست اضطراب",
    private var questions: ArrayList<Question> = AnxietyTestData.questions
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
        var result1 = 0f
        var result2 = 0f
        questions.forEachIndexed { index, question ->
            question.optionList.forEach {
                Log.i("TAG", "calculateResult: $it")
                if (it.isSelected)
                    if (index <= 19) result1 += it.score else result2 += it.score
            }
        }
        return Pair(result1, result2)
    }

    override fun result(): String {
        val (result1, result2) = calculateResult() as Pair<Float, Float>

        return when (result1) {
            in 20.0..30.0 -> "حالت اضطراب شما هیچ یا در کمترین حد می باشد"
            in 31.0..42.0 -> "حالت اضطراب شما خفیف می باشد"
            in 43.0..53.0 -> "حالت اضطراب شما متوسط می باشد"
            else -> "حالت اضطراب شما شدید می باشد"
        }.plus("\n").plus(
            when (result2) {
                in 20.0..34.0 -> "رگه اضطراب شما هیچ یا در کمترین حد می باشد"
                in 35.0..45.0 -> "رگه اضطراب شما خفیف می باشد"
                in 46.0..56.0 -> "رگه اضطراب شما متوسط می باشد"
                else -> "رگه اضطراب شما شدید می باشد"
            }
        )
            .plus("\n\n")
            .plus(
                "حالت اضطراب : احساس ترس، دلهره و نگرانی شما را در حال حاضر نشان می دهد.\n" +
                        "به عبارت دیگر بیانگر اضطراب کنونی شماست که از برخی تهدیدهای بیرونی یا عوامل استرس زا ناشی می شود."
            )
            .plus("\n\n")
            .plus("رگه اضطراب: نشان دهنده ی اضطراب حال حاضر و موقعیت کنونی نیست و استعداد شما را به بروز واکنش های اضطرابی در موقعیت های خاص و اضطراب برانگیز نشان می دهد.")

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