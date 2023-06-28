package com.garm.sample.consumer;

public interface KafkaConstants {
    String RETRY_COUNT_HEADER_KEY = "RETRY-COUNT";
    String ORIGINAL_TOPIC_HEADER_KEY = "ORIGINAL-TOPIC";
    String GARM_DLQ_TOPIC = "GARM-DLQ-TOPIC";
    String GARM_DLQ_2ND_TOPIC = "GARM-DLQ-2ND-TOPIC";
    String GARM_REQUEST_TOPIC = "GARM-REQUEST";
    String GARM_RESPONSE_TOPIC = "GARM-RESPONSE";
    String GARM_GROUP_ID = "GARM";
}
