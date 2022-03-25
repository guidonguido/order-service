package it.polito.wa2.project.orderservice.dto.common

import com.fasterxml.jackson.annotation.JsonCreator

enum class TransactionReason{
    PAYMENT, RECHARGE, REFUND;

    companion object {
        private val mapping: MutableMap<String, TransactionReason> = mutableMapOf(
            "payment" to PAYMENT,
            "Payment" to PAYMENT,
            PAYMENT.name to PAYMENT,

            "recharge" to RECHARGE,
            "Recharge" to RECHARGE,
            RECHARGE.name to RECHARGE,

            "refund" to REFUND,
            "Refund" to REFUND,
            REFUND.name to REFUND,
        )

        @JsonCreator
        fun fromString(value:String): TransactionReason? = mapping.get(value)
    }
}