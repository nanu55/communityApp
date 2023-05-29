package com.example.communityapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.dto.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object CommonUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToTimezone(millis: Long, timezone: String): LocalDateTime {
        val instant = Instant.ofEpochMilli(millis)
        val zoneId = ZoneId.of(timezone)
        return LocalDateTime.ofInstant(instant, zoneId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }


    fun getUserById(userId: String, callback: (User?) -> Unit) {
        val databaseRef = FirebaseManager.database.reference.child("users").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                callback(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}