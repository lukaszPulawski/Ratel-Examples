package com.payu.soa.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessController {

	private static Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

	public PaymentProcessController() {
	}

	public void pay(int sessionId, String paymentMethodBrand, Long posId) {
		LOGGER.info("Starting payment process");

		// TODO: implement your payment process here

		LOGGER.info("Finalizing payment process");
	}

}