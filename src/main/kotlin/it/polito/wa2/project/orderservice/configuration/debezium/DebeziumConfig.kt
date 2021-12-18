package it.polito.wa2.project.orderservice.configuration.debezium

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DebeziumConfig {

    @Bean
    fun orderRequestConnector(): io.debezium.config.Configuration? {
        return io.debezium.config.Configuration.create()
            .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "./tmp/offsets.dat")
            .with("offset.flush.interval.ms", 60000)
            .with("name", "order-created-connector")
            //.with("database.server.name", studentDBHost.toString() + "-" + studentDBName)
            .with("database.hostname", "localhost")
            .with("database.port", "3304")
            .with("database.user", "root")
            .with("database.password", "root")
            .with("database.dbname", "orderservice")
            .with("database.include.list", "orderservice")
            .with("include.schema.changes", "false")
            .with("database.server.id", "10189")
            .with("database.server.name", "orderservice-mysql-db-server")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .with("database.history.file.filename", "./tmp/dbhistory.dat")
            // .with("table.whitelist", "orders")
            .with("table.include.list", "orderservice.order_request")
            .with("database.history.skip.unparseable.ddl", true)
            .with("database.allowPublicKeyRetrieval", true)
            .build()
    }
}