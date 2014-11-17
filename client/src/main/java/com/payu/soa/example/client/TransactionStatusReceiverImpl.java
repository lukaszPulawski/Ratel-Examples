package com.payu.soa.example.client;

import com.payu.discovery.event.Subscribe;
import com.payu.transaction.event.TransactionChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionStatusReceiverImpl implements TransactionStatusReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionStatusReceiverImpl.class);

    @Subscribe
    public void receiveEvent(TransactionChangedEvent event) {
        LOGGER.info("Transaction status change received: {}", event.getTransStatus());
    }

}
