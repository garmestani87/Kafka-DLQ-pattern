package com.garm.sample.service;

import com.garm.sample.dto.DlqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.garm.sample.consumer.KafkaConstants.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Component
@Slf4j
public class DlqBase<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaTemplate<K, V> kafkaTemplate;
    private static final int MAX_RETRY = 5;
    private static final int DELAY_MILLISECONDS = 5000;

    public final void send(final ConsumerRecord<K, V> consumerRecord) {
        String originalTopic = consumerRecord.topic();
        ProducerRecord<K, V> record = new ProducerRecord<>(GARM_DLQ_TOPIC, consumerRecord.key(), consumerRecord.value());
        record.headers().add(ORIGINAL_TOPIC_HEADER_KEY, originalTopic.getBytes(UTF_8));

        Header retryCount = consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY);
        if (retryCount != null) {
            record.headers().add(retryCount);
        }

        kafkaTemplate.send(record);
    }

    public final void resend(final DlqDto<K, V> dto) {
        try {
            int retryCount = 0;
            if (dto.getRetryCountHeader() != null) {
                retryCount = Integer.parseInt(new String(dto.getRetryCountHeader().value(), UTF_8));
            }
            if (retryCount < MAX_RETRY) {
                retryCount += 1;
                ProducerRecord<K, V> record = new ProducerRecord<>(dto.getOriginalTopic(), dto.getKey(), dto.getValue());
                record.headers().add(RETRY_COUNT_HEADER_KEY, String.valueOf(retryCount).getBytes(UTF_8));
                Thread.sleep(DELAY_MILLISECONDS);
                kafkaTemplate.send(record);
            } else {
                send2ndDlq(dto);
            }
        } catch (Exception e) {
            send2ndDlq(dto);
        } finally {
            dto.getAck().acknowledge();
        }
    }

    public final void send2ndDlq(final DlqDto<K, V> dto) {
        try {
            ProducerRecord<K, V> record = new ProducerRecord<>(GARM_DLQ_2ND_TOPIC, dto.getKey(), dto.getValue());
            record.headers().add(ORIGINAL_TOPIC_HEADER_KEY, dto.getOriginalTopic().getBytes(UTF_8));
            kafkaTemplate.send(record);
        } catch (Exception ex) {
            log.error("Unable to process DLQ message", ex);
        }
    }
}

