package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val paymentDao = db.paymentDao()

    private val _payments = MutableStateFlow<List<PaymentEntity>>(emptyList())
    val payments: StateFlow<List<PaymentEntity>> = _payments

    init {
        viewModelScope.launch {
            paymentDao.getAllPayments().collect { list ->
                _payments.value = list
            }
        }
    }

    fun addPayment(name: String, amount: Double, time: String) {
        viewModelScope.launch {
            paymentDao.insertPayment(
                PaymentEntity(name = name, amount = amount, time = time)
            )
        }
    }

    fun markAsDone(payment: PaymentEntity) {
        viewModelScope.launch {
            paymentDao.updatePayment(payment.copy(taken = true))
        }
    }
}
