package com.payflow.audit_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"payment.kafka.topics.payment-created=payment-created-topic",
		"spring.kafka.consumer.group-id=audit-service-group",
		"spring.kafka.consumer.key-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer",
		"spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer",
		"spring.kafka.consumer.properties.spring.deserializer.key.delegate.class=org.apache.kafka.common.serialization.StringDeserializer",
		"spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer",
		"spring.kafka.consumer.properties.spring.json.use.type.headers=false",
		"spring.kafka.consumer.properties.spring.json.value.default.type=com.payflow.audit_service.event.PaymentCreatedEvent"
})
class AuditServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
