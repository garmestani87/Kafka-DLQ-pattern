package com.garm.sample.state;

import com.garm.sample.dto.DlqDto;
import com.garm.sample.event.LogResponse;
import com.garm.sample.service.DlqBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.garm.sample.consumer.KafkaConstants.GARM_RESPONSE_TOPIC;
import static com.garm.sample.consumer.KafkaConstants.RETRY_COUNT_HEADER_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogResponseState implements DlqState {
    private final DlqBase<String, LogResponse> dlq;

    @Override
    public void resend(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack) {
        dlq.resend(new DlqDto<String, LogResponse>().setAck(ack)
                .setOriginalTopic(GARM_RESPONSE_TOPIC)
                .setKey(consumerRecord.key())
                .setValue((LogResponse) consumerRecord.value())
                .setRetryCountHeader(consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY)));
    }
}
