package com.app.mombaby.repositories.tests

import com.app.mombaby.models.adapterModels.Option
import com.app.mombaby.models.adapterModels.Question

interface TestRepositoryImpl {
    fun name(): String?
    fun calculateResult(): Any
    fun result(): String
    fun questions(): ArrayList<Question>
    fun questionsSize(): Int
    fun options(pos: Int): ArrayList<Option>
}