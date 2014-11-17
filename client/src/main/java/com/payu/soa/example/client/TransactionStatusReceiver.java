package com.payu.soa.example.client;

import com.payu.transaction.event.TransactionChangedEvent;

public interface TransactionStatusReceiver {

    void receiveEvent(TransactionChangedEvent event);

}
