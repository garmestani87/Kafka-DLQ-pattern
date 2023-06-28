package com.garm.sample.service;

import com.garm.sample.consumer.ServiceBase;
import com.garm.sample.event.LogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogRequestService extends ServiceBase<String, LogRequest> {

    @Autowired
    public LogRequestService(DlqBase<String, LogRequest> dlqBase) {
        super(dlqBase);
    }

    @Override
    protected void doConsume(LogRequest request) {
        // implement your business
    }
}
