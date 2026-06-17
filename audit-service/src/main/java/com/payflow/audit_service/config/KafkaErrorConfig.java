package com.payflow.audit_service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaErrorConfig {
    @Bean
    public DeadLetterPublishingRecoverer recoverer(

            KafkaTemplate<Object, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) ->
                        new TopicPartition(
                                record.topic() + "-dlt",
                                record.partition()
                        )
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(
            DeadLetterPublishingRecoverer recoverer) {

        FixedBackOff fixedBackOff =
                new FixedBackOff(2000L, 3);

        DefaultErrorHandler handler =
                new DefaultErrorHandler(
                        recoverer,
                        fixedBackOff
                );

        handler.setRetryListeners(
                (record, ex, deliveryAttempt) ->
                        log.error(
                                "Retry Attempt={} Topic={} Key={}",
                                deliveryAttempt,
                                record.topic(),
                                record.key()
                        )
        );

        return handler;
    }
}