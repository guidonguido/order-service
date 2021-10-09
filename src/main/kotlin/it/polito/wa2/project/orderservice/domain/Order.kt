package it.polito.wa2.project.orderservice.domain

import javax.persistence.*

@Entity
@Table(name = "ORDERS")
class Order(
    var buyerId: Long,                                    // Order creator userId


    // TODO create Address Entity or keep this way
    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    // TODO add CascadeType
    @OneToMany(mappedBy = "order",
        cascade = arrayOf(CascadeType.ALL), // Persist, Delete, Merge... OrderProduct(s) when same operation is executed via Order object
        targetEntity = OrderProduct::class)
    var orderProducts: MutableSet<OrderProduct> = mutableSetOf()
                                                            // List of purchased products, their amount, the purchase price
                                                            // It embeds infos about the warehouse products are picked from
): EntityBase<Long>() {

    fun addOrderProduct( orderProduct: OrderProduct ){
        orderProducts.add(orderProduct)
        orderProduct.order = this
    }
}