package it.polito.wa2.project.orderservice.domain

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

@Entity
class Order(
    var buyerId: Long,                                    // Order creator userId

    @OneToMany(mappedBy = "order", targetEntity = OrderProduct::class)
    var orderProducts: Set<OrderProduct> = setOf(),      // List of purchased products, their amount, the purchase price
                                                        // It embeds infos about the warehouse products are picked from

    // TODO create Address Entity or keep this way
    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus

): EntityBase<Long>()