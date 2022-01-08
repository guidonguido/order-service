package it.polito.wa2.project.orderservice.configuration.kafka

import it.polito.wa2.project.orderservice.dto.common.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${spring.kafka.consumer.group-id}")
    private val groupId: String? = null

    @Bean
    fun consumerFactoryResponse(): ConsumerFactory<String, OrderResponseDTO> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        // props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        // props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        val errorHandlingDeserializer: ErrorHandlingDeserializer<OrderResponseDTO> = ErrorHandlingDeserializer(
            JsonDeserializer(
                OrderResponseDTO::class.java
            ).trustedPackages("*").ignoreTypeHeaders()
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), errorHandlingDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactoryResponse(): ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO>()
        factory.setConsumerFactory(consumerFactoryResponse())
        return factory
    }

    @Bean
    fun consumerFactoryRequest(): ConsumerFactory<String, OrderRequestDTO> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        // props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        // props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        val errorHandlingDeserializer: ErrorHandlingDeserializer<OrderRequestDTO> = ErrorHandlingDeserializer(
            JsonDeserializer(
                OrderRequestDTO::class.java
            ).trustedPackages("*").ignoreTypeHeaders()
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), errorHandlingDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactoryRequest(): ConcurrentKafkaListenerContainerFactory<String, OrderRequestDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderRequestDTO>()
        factory.setConsumerFactory(consumerFactoryRequest())
        return factory
    }
}