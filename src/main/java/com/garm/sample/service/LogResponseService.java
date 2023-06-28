package com.garm.sample.service;

import com.garm.sample.consumer.ServiceBase;
import com.garm.sample.event.LogResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogResponseService extends ServiceBase<String, LogResponse> {


    @Autowired
    public LogResponseService(DlqBase<String, LogResponse> dlqBase) {
        super(dlqBase);
    }

    @Override
    protected void doConsume(LogResponse response) {
        // implement your business
    }

}
