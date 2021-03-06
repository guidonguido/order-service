package it.polito.wa2.project.orderservice.coreography

import io.debezium.data.Envelope
import io.debezium.embedded.Connect
import io.debezium.engine.DebeziumEngine
import io.debezium.engine.RecordChangeEvent
import io.debezium.engine.format.ChangeEventFormat
import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.common.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import it.polito.wa2.project.orderservice.services.OrderService
import org.apache.commons.lang3.tuple.Pair
import org.apache.kafka.connect.data.Field
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Function
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class OutboxListener(orderRequestConnector: io.debezium.config.Configuration,
                     val orderService: OrderService) {
    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val debeziumEngine: DebeziumEngine<RecordChangeEvent<SourceRecord>>?

    private fun handleChangeEvent(sourceRecordRecordChangeEvent: RecordChangeEvent<SourceRecord>) {
        println("[+++++++++++++++++] OutboxListener Changed \n \n \n \n \n \n \n")
        val sourceRecord = sourceRecordRecordChangeEvent.record()
        val sourceRecordChangeValue = sourceRecord.value() as Struct
        if (sourceRecordChangeValue != null) {
            val operation = Envelope.Operation.forCode(sourceRecordChangeValue[Envelope.FieldName.OPERATION] as String)
            if (operation != Envelope.Operation.READ) {
                val record =
                    if (operation == Envelope.Operation.DELETE) Envelope.FieldName.BEFORE else Envelope.FieldName.AFTER // Handling Update & Insert operations.
                val struct = sourceRecordChangeValue[record] as Struct
                val payload = struct.schema().fields().stream()
                    .map { obj: Field -> obj.name() }
                    .filter { fieldName: String? -> struct[fieldName] != null }
                    .map { fieldName: String ->
                        Pair.of(
                            fieldName,
                            struct[fieldName]
                        )
                    }
                    .collect(
                        Collectors.toMap(
                            Function { (key): Pair<String, Any?> -> key },
                            Function { (_, value): Pair<String, Any?> -> value })
                    )
                val orderRequest = OrderRequestDTO(
                    payload["uuid"] as String,
                    payload["order_id"] as Long?,
                    payload["buyer_id"] as Long?,
                    payload["delivery_name"] as String,
                    payload["delivery_street"] as String,
                    payload["delivery_zip"] as String,
                    payload["delivery_city"] as String,
                    payload["delivery_number"] as String,
                    OrderStatus.fromString(payload["status"].toString()),
                    emptySet(),
                    payload["total_price"] as Double?,
                    payload["destination_wallet_id"] as Long?,
                    payload["source_wallet_id"] as Long,
                    payload["transaction_reason"].toString(),
                )
                println("[+++++++++++++++++] Operation = ${operation} \nSTATUS = ${orderRequest.status}\n\n\n")

                if (operation == Envelope.Operation.CREATE) {
                    orderService.publishOrderRequest(orderRequest)
                    println("[Debezium] Record request CREATED payload uuid : ${payload["uuid"]}")
                }


                if (operation == Envelope.Operation.UPDATE && orderRequest.status == OrderStatus.FAILED) {
                    orderService.publishOrderSagaError(OrderResponseDTO(orderRequest.orderId, orderRequest.uuid, -1L));
                    println("[Debezium] Record request UPDATED payload uuid : ${payload["uuid"]}")
                }
            }
        }
    }

    @PostConstruct
    private fun start() {
        println("[+++++++++++++++++] OutboxListener Init \n \n \n \n \n \n \n")

        executor.execute(debeziumEngine)
    }

    @PreDestroy
    @Throws(IOException::class)
    private fun stop() {
        if (debeziumEngine != null) {
            debeziumEngine.close()
        }
    }

    init {
        println("[+++++++++++++++++] OutboxListener Init \n \n \n \n \n \n \n")

        debeziumEngine = DebeziumEngine.create(
            ChangeEventFormat.of(
                Connect::class.java
            )
        )
            .using(orderRequestConnector.asProperties())
            .notifying { sourceRecordRecordChangeEvent: RecordChangeEvent<SourceRecord> ->
                handleChangeEvent(
                    sourceRecordRecordChangeEvent
                )
            }
            .build()
    }
}