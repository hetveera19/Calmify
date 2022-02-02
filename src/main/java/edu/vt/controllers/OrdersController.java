/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.controllers;

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.ItemDetailsFacade;
import edu.vt.FacadeBeans.OrdersFacade;
import edu.vt.FacadeBeans.ShopFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("ordersController")
@SessionScoped
public class OrdersController implements Serializable {

    private List<OrderDetails> listOfOrders = new ArrayList<>();
    private User user;

    @EJB
    ItemDetailsFacade itemDetailsFacade;
    @EJB
    OrdersFacade ordersFacade;

    OrderDetails selected;

    @Inject
    ReviewPaymentController reviewPaymentController;

    public int createOrder(User signedInUser) {
        int id = 0;
        try {
            System.out.println(signedInUser.getId());
            OrderDetails order = new OrderDetails();
            order.setUser(signedInUser);
            order.setOrderDate(new Date());
            order.setTotalPrice(Double.parseDouble(reviewPaymentController.getTotal()));
            System.out.println(order.toString());

            id = ordersFacade.createUserOrder(order);
            System.out.println("Order Created with Id :" + id);

        } catch (Exception e) {
            System.out.println(e);
        }

        return id;
    }

    public List<OrderDetails> getListOfOrders() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        listOfOrders = ordersFacade.getAllUserOrders(signedInUser.getId());

        System.out.println(listOfOrders);
        return listOfOrders;
    }

    public List<ItemDetails> getListOfItemsForGivenOrder(OrderDetails orderdetails) {
        List<ItemDetails> listOfAllOrderItems = itemDetailsFacade.findItemRelatedToOrderId(orderdetails.getId());
        return listOfAllOrderItems;
    }
}