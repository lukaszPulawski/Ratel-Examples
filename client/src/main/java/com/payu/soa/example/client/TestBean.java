package com.payu.soa.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TestBean {

	private static Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

	@Autowired
	private PaymentProcessController processController;

	@Async
	public void runProcess(int workerId) {
		String paymentMethodBrand = "visa";
		Long posId = 2L;
		processController.pay(workerId, paymentMethodBrand, posId);
	}
	
	public void startup() {
		
	}


}
