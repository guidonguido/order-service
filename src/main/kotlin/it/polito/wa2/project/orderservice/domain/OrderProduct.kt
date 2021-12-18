package it.polito.wa2.project.orderservice.domain

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderProduct(

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    var order: Order?,

    var purchasedProductId: Long,             // productId of purchased product

    var quantity: Long,

    var purchasedProductPrice: Double,   // Product(productId)'s price

    var warehouseId: Long                    // warehouseId products are picked from
): EntityBase<Long>()