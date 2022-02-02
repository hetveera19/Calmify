/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Cart;
import edu.vt.EntityBeans.OrderDetails;
import edu.vt.EntityBeans.ShopItems;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrdersFacade extends AbstractFacade<OrderDetails> {
    @PersistenceContext(unitName = "Calmify-PU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public OrdersFacade() {
        super(OrderDetails.class);
    }

    public void create(){

    }

    public List<OrderDetails> getOrder(Integer orderId){
        return entityManager.createNamedQuery("OrderDetails.findOrderById")
                .setParameter("orderId", orderId).getResultList();
    }

    public List<OrderDetails> getAllUserOrders(Integer userId) {
        return entityManager.createNamedQuery("OrderDetails.findOrdersByUserId")
                .setParameter("userId", userId)
                .getResultList();
    }

    public int createUserOrder(OrderDetails order){
        getEntityManager().persist(order);
        return order.getId();
    }
}
