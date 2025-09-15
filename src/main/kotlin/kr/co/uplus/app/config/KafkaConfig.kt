package kr.co.uplus.app.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.transaction.KafkaTransactionManager

/**
 * Configuration class for Apache Kafka.
 * This class sets up Kafka producers, consumers, and topics.
 */
@Configuration
@EnableKafka
class KafkaConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${spring.kafka.consumer.group-id}")
    private lateinit var groupId: String

    /**
     * Creates and configures the Kafka producer factory with transaction support.
     *
     * @return The configured producer factory with transaction capability
     */
    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        // Enable transactions
        configProps[ProducerConfig.TRANSACTIONAL_ID_CONFIG] = "app-tx-"
        configProps[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true
        configProps[ProducerConfig.ACKS_CONFIG] = "all"

        return DefaultKafkaProducerFactory(configProps)
    }

    /**
     * Creates and configures the Kafka transaction manager.
     *
     * @return The configured Kafka transaction manager
     */
    @Bean
    fun kafkaTransactionManager(): KafkaTransactionManager<String, String> {
        return KafkaTransactionManager(producerFactory())
    }

    /**
     * Creates and configures the Kafka template for sending messages with transaction support.
     *
     * @return The configured Kafka template with transaction capability
     */
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val template = KafkaTemplate(producerFactory())
        template.setTransactionIdPrefix("app-tx-")
        return template
    }

    /**
     * Creates and configures the Kafka consumer factory with transaction support.
     *
     * @return The configured consumer factory that can work with transactional producers
     */
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        // Configure consumer to read only committed messages
        configProps[ConsumerConfig.ISOLATION_LEVEL_CONFIG] = "read_committed"
        return DefaultKafkaConsumerFactory(configProps)
    }

    /**
     * Creates and configures the Kafka listener container factory with transaction support.
     *
     * @return The configured listener container factory that supports transactions
     */
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        // Enable batch listening for better transaction support
        factory.isBatchListener = true
        // Configure container properties for transactions
        factory.containerProperties.transactionManager = kafkaTransactionManager()
        return factory
    }

    /**
     * Creates and configures the Kafka admin client.
     *
     * @return The configured admin client
     */
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        return KafkaAdmin(configs)
    }

    /**
     * Example topic configuration.
     * You can define multiple topics as needed.
     *
     * @return The configured topic
     */
    @Bean
    fun exampleTopic(): NewTopic {
        return NewTopic("example-topic", 1, 1.toShort())
    }

    /**
     * Person topic configuration.
     *
     * @return The configured topic for person data
     */
    @Bean
    fun personTopic(): NewTopic {
        return NewTopic("person-topic", 1, 1.toShort())
    }
}
