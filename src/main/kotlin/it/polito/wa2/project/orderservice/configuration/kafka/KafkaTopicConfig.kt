package it.polito.wa2.project.orderservice.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin


@Configuration
class KafkaTopicConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    /**
    @Bean
    fun orderSagaResponseTopic(): NewTopic {
        return NewTopic("orderSagaResponse", 1, 1.toShort())
    }
     */
    @Bean
    fun orderSagaRequestTopic(): NewTopic {
        return NewTopic("orderOrderSagaRequest", 1, 1.toShort())
    }

    @Bean
    fun orderOrderSagaResponseTopic(): NewTopic {
        return NewTopic("orderOrderSagaResponse", 1, 1.toShort())
    }

}