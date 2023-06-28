package com.garm.sample.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.support.Acknowledgment;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class DlqDto<K extends Serializable, V extends SpecificRecordBase> {
    private K key;
    private V value;
    private Header retryCountHeader;
    private String originalTopic;
    private Acknowledgment ack;
}
