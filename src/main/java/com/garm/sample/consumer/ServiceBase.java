package com.garm.sample.consumer;

import com.garm.sample.service.DlqBase;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

import java.io.Serializable;

public abstract class ServiceBase<K extends Serializable, V extends SpecificRecordBase> {
    private final DlqBase<K, V> dlqBase;

    public ServiceBase(DlqBase<K, V> dlqBase) {
        this.dlqBase = dlqBase;
    }

    public final void consume(ConsumerRecord<K, V> record, Acknowledgment ack) {
        try {
            doConsume(record.value());
        } catch (Exception ex) {
            dlqBase.send(record);
        } finally {
            ack.acknowledge();
        }
    }

    protected abstract void doConsume(V value);
}
