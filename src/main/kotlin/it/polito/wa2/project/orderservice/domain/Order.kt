package it.polito.wa2.project.orderservice.domain

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

@Entity
class Order(
    var buyer: Long,                                    // Order creator userId

    @OneToMany(mappedBy = "order", targetEntity = OrderProduct::class)
    var orderProducs: Set<OrderProduct> = setOf(),      // List of purchased products, their amount, the purchase price
                                                        // It embeds infos about the warehouse products are picked from

    // TODO create Address Entity or keep this way
    val deliveryName: String,
    val deliveryStreet: String,
    val deliveryZip: String,
    val deliveryCity: String,
    val deliveryNumber: String,

    @Enumerated(EnumType.STRING)
    private var status: OrderStatus

): EntityBase<Long>()