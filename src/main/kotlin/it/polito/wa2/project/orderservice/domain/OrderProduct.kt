package it.polito.wa2.project.orderservice.domain

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderProduct(
    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    var order: Order,

    var purchasedProductId: Long,             // productId of purchased product

    var amount: Long,

    var purchasedProductPrice: BigDecimal,   // Product(productId)'s price

    var warehouseId: Long                    // warehouseId products are picked from
): EntityBase<Long>()