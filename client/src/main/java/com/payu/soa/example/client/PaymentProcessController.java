package com.payu.soa.example.client;

import java.beans.Transient;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import com.payu.transaction.server.model.Transaction;
import com.payu.transaction.server.service.TransactionService;

@Component
public class PaymentProcessController {

	private static Logger LOGGER = LoggerFactory.getLogger(TestBean.class);
	private OrderService orderService;
	
	private TransactionService transService;


	public PaymentProcessController() {
	}

	public void pay(int sessionId, String paymentMethodBrand, Long posId) {
		BigDecimal amount = new BigDecimal(100L);
		
		PaymentProcessContext ctx = new PaymentProcessContext();
	
		CommandChainProcess  paymentProcess = new CommandChainProcess();
		
		paymentProcess.add(new VerifyPosCommand(posId));
		paymentProcess.add(new CreateUserCommand(ctx));
		paymentProcess.add(new CreateOrderCommand(ctx, sessionId, amount));
		paymentProcess.add(new AuthorizeTransCommand(ctx, paymentMethodBrand, amount));
		
		paymentProcess.runProcess();

		long userId = 0;
		LOGGER.info("Starting payment process");

		Order order = new Order(amount, "Order" + sessionId);
		order.setUserId(userId);
		
		Long orderId = orderService.createOrder(order);
		
		
		try {
			Transaction trans = new Transaction(amount, paymentMethodBrand, orderId);
			Long transId = transService.authorize(trans);
			LOGGER.debug("Transaction {0} succssfully authorized", transId);
		} catch (Exception e) {
			orderService.deleteOrderById(orderId); //is there such a method?
		}
		
		
		

		// TODO: implement your payment process here

		LOGGER.info("Finalizing payment process" + orderId);
	}

}