package com.payu.order.server.service;

import java.util.Collection;

import com.payu.order.server.model.Order;

/**
 * A service that enables you to perform operations on {@link Order} entity. 
 * The underlaying data repository is sharded by instance.
 *
 */
public interface OrderService {

	/**
	 * Creates and persistes order entity
	 * @param order the order to create
	 * @return id of a newly created order. This id is guaranteed to be unique within single shard. 
	 * @throws unexpected exceptions
	 */
    Long createOrder(Order order);

	/**
	 * Creates and persistes order entity
	 * @param order the order to create
	 * @return id of a newly created order. This id is guaranteed to be unique within single shard. 
	 */
    Order getOrder(Long id);

    /**
     * Delete all orders in a single data shard.
     * @return number of deleted orders. 
     */
	int deleteOrderById();
	
	/**
	 * Fetches all orders for a given user.
	 * @param userId the id of a user
	 * @return list of orders created by a given user, in order of creation
	 * @throws unexpected exceptions
	 */
	public Collection<Order> getOrdersByUserId(long userId);

	/**
	 * Updates a given order at underlaying persistance layer
	 * @param order the order to store
	 */
	void updateOrder(Order order);


}
