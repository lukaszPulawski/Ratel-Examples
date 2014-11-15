package com.payu.soa.example.client;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.payu.discovery.Discover;
import com.payu.order.server.service.OrderService;
import com.payu.pos.server.service.PosService;
import com.payu.transaction.server.service.TransactionService;
import com.payu.user.server.service.UserService;

@Component
public class ExcerciseStartupBean {

	private static Logger LOGGER = LoggerFactory.getLogger(ExcerciseStartupBean.class);

	
	private static final int MAX_FAILURES = 30;

	@Discover
	private OrderService orderService;
	
	@Discover
	private PosService posService;
	
	@Discover
	private UserService userService;
	
	@Discover
	private TransactionService transactionService;
	
	
	@Async
	public void ensureServicesDiscovered() throws InterruptedException {
		ensureSuccessfulCall(orderService::getOrder);
		ensureSuccessfulCall(transactionService::getTransactionById);
		ensureSuccessfulCall(userService::getUserById);
		ensureSuccessfulCall(posService::getPosById);
		LOGGER.info("ALL SERVICES DISCOVERED SUCCESSFULLY");
	}


	private void ensureSuccessfulCall(Function<Long, Object> f) throws InterruptedException {
	
		boolean successfull = false;
	
		int failureCnt = 0;
		
		while (!successfull && failureCnt < MAX_FAILURES) {
			try {
				f.apply(1L);
				successfull = true;
			} catch (Exception e) {
				failureCnt++;
				TimeUnit.SECONDS.sleep(5);
			}
		}
 		
	}
	
	
}
