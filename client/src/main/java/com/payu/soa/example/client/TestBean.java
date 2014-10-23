package com.payu.soa.example.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.RetryPolicy;
import com.payu.discovery.event.Subscribe;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import com.payu.pos.server.model.Pos;
import com.payu.pos.server.service.PosService;
import com.payu.transaction.event.TransactionChangedEvent;
import com.payu.transaction.server.model.Transaction;
import com.payu.transaction.server.service.TransactionService;
import com.payu.user.server.model.User;
import com.payu.user.server.service.NoSuchUserException;
import com.payu.user.server.service.UserService;

@Component
public class TestBean {

	private static Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

	@Discover
	@RetryPolicy(exception = RemoteConnectFailureException.class)
	private OrderService orderService;

	@Discover
	@Cachable
	private PosService posService;

	@Discover
	private UserService userService;

	@Discover
	@RetryPolicy(exception = RemoteConnectFailureException.class)
	private TransactionService transService;

	@Async
	public void createOrderInAsync(int sessionId) {
		String paymentMethodBrand = "visa";
		Long posId = 2L;
		pay(sessionId, paymentMethodBrand, posId);
	}

	private void pay(int sessionId, String paymentMethodBrand, Long posId) {

		verifyPos(sessionId, paymentMethodBrand, posId);

		Long userId = createAndActivateUser();

		Order order = createOrder(sessionId, userId);
		Long orderId = orderService.createOrder(order);
		BigDecimal amount = order.getAmount();

		Long transId = createTransaction(paymentMethodBrand, orderId, amount);
		
		LOGGER.info("*** Transaction created  ***");

		verifyPaymentProcess(transId, userId, orderId, sessionId, amount,
				paymentMethodBrand, posId);
		
		LOGGER.info("*** Transaction verified ***");
	}

	private boolean verifyPaymentProcess(Long transId, Long userId,
			Long orderId, int sessionId, BigDecimal amount,
			String paymentMethodBrand, Long posId) {

		return verifyUser(userId)// 
				&& verifyOrder(userId, orderId)
				&& verifyTransaction(transId, orderId);

	}

	private boolean verifyTransaction(Long transId, Long orderId) {
		Transaction trans = transService.getTransactionById(transId);

		if (trans == null) {
			LOGGER.error("Transaction not created");
			return false;
		}

		if (!orderId.equals(trans.getOrderId())) {
			LOGGER.error("Order not bound to transaction ");
			return false;
		}
		return true;
	}

	private boolean verifyOrder(Long userId, Long orderId) {
		Order order = orderService.getOrder(orderId);

		if (order == null) {
			LOGGER.error("Order not created");
			return false;
		}

		if (!userId.equals(order.getUserId())) {
			LOGGER.error("Order not created");
			return false;
		}
		return true;
	}

	private boolean verifyUser(Long userId) {
		User user = userService.getUserById(userId);

		if (user == null) {
			LOGGER.error("User not created");
			return false;
		}

		if (!user.isActive()) {
			LOGGER.error("User not activaated");
			return false;
		}
		return true;
	}

	private Long createTransaction(String paymentMethodBrand, Long orderId,
			BigDecimal amount) {
		Transaction trans = new Transaction(amount, paymentMethodBrand, orderId);
		Long transId = transService.authorize(trans);
		return transId;
	}
	
	@Subscribe
	public void receiveEvent(TransactionChangedEvent event) {
		LOGGER.info("Transaction state confirmed " + event.getTransactionId());
	}

	private Long createAndActivateUser() {
		Long userId = userService.createUser(new User("Jan Kowalski",
				"jan.kowalski@gmail.com", "74030200430"));
		try {
			userService.activateUser(userId);
		} catch (NoSuchUserException e) {
			e.printStackTrace();
		}
		return userId;
	}

	private void verifyPos(int sessionId, String paymentMethodBrand, Long posId) {
		Pos pos = posService.getPosById(posId);
		if (!pos.isActive()
				|| !pos.isPaymetnMethodBrandActive(paymentMethodBrand)) {
			throw new IllegalStateException(String.format(
					"Payment for % not possible with method %s and pos %s ",
					sessionId, paymentMethodBrand, pos.getName()));
		}
	}

	private Order createOrder(int sessionId, Long userId) {
		Order order = new Order(new BigDecimal(Math.random() * 10000L), "Foo "
				+ sessionId);
		order.setUserId(userId);
		return order;
	}

}
