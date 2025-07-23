package com.example.myapplication_ass2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_table")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,    // 任务名称（Location + Cleaner Level）
    val amount: Double,  // 金额（SGD）
    val time: String,    // 预约时间
    val taken: Boolean = false
)
