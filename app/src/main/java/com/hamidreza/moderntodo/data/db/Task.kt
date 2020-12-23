package com.hamidreza.moderntodo.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis()
):Parcelable{
    fun createdDateFormatted():String{
        return DateFormat.getDateTimeInstance().format(created)
    }
}
