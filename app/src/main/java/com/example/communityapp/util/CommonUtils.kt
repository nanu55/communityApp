package com.example.communityapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.communityapp.config.ApplicationClass
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.dto.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
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
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
    //천단위 콤마
    fun makeComma(num: Int): String {
        var comma = DecimalFormat("#,###")
        return "${comma.format(num)} 원"
    }

    fun getFormattedString(date:Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH시 mm분")
        dateFormat.timeZone = TimeZone.getTimeZone("Seoul/Asia")

        return dateFormat.format(date)
    }

    // 시간 계산을 통해 완성된 제품인지 확인
//    fun isOrderCompleted(orderDetail: OrderDetailResponse): String {
//        return if( checkTime(orderDetail.orderDate.time))  "주문완료" else "진행 중.."
//    }
//
//    // 시간 계산을 통해 완성된 제품인지 확인
//    fun isOrderCompleted(order: LatestOrderResponse): String {
//        return if( checkTime(order.orderDate.time))  "주문완료" else "진행 중.."
//    }

}