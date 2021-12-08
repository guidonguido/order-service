package it.polito.wa2.project.orderservice.repositories.coreography

import it.polito.wa2.project.orderservice.domain.coreography.OrderRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRequestRepository: CrudRepository<OrderRequest, Long> {

    fun findByUuid(uuid: String): Set<OrderRequest>
}