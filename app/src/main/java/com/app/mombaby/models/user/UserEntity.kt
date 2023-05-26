package com.app.mombaby.models.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "firstName")
    val firstName: String? = null,

    @ColumnInfo(name = "lastName")
    val lastName: String? = null,

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "age")
    val age: Int? = null,

    @ColumnInfo(name = "job")
    val job: String? = null,

    @ColumnInfo(name = "childNum")
    val childNum: Int? = null,

    @ColumnInfo(name = "isPregnant")
    val isPregnant: Boolean? = null,
) {

}