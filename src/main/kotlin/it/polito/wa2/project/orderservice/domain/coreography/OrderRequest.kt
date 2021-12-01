package it.polito.wa2.project.orderservice.domain.coreography

import it.polito.wa2.project.orderservice.domain.EntityBase
import it.polito.wa2.project.orderservice.domain.OrderStatus
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class OrderRequest (

    // TODO Add OrderRequest UUID
    var uuid: String,

    var orderId: Long?,
    var buyerId: Long?,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus?,

    var totalPrice: Double?,

    var destinationWalletId: Long?,
    var sourceWalletId: Long,

    var transactionReason: String?,
    var reasonDetail: Long? = null
    ): EntityBase<Long>()

    /**
    @OneToMany(mappedBy = "orderRequest", targetEntity = OrderProduct::class)
    var orderProducts: MutableSet<OrderProduct> = mutableSetOf(),
    */


    /**
    fun addOrderProduct( orderProduct: OrderProduct ){
        orderProducts.add(orderProduct)
        orderProduct.orderRequest = this
    }
    */
