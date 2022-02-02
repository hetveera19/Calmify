/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.payment;

import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.CartController;
import edu.vt.controllers.ItemDetailsController;
import edu.vt.controllers.OrdersController;

@WebServlet("/executePayment")
public class ExecutePayment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    OrdersController ordersController;

    @Inject
    ItemDetailsController itemDetailsController;

    @Inject
    CartController cartController;


    public ExecutePayment() {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        try {
            PaymentServices paymentServices = new PaymentServices();
            Payment payment = paymentServices.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);

            User signedInUser = (User) request.getSession().getAttribute("user");
            int orderId = ordersController.createOrder(signedInUser);
            itemDetailsController.addOrderItems(signedInUser, orderId);
            cartController.removeAllUserItemFromCart(signedInUser);

            request.getRequestDispatcher("/shop/UserOrders.xhtml").forward(request, response);

        } catch (PayPalRESTException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("/shop/Error.xhtml").forward(request, response);
        }
    }

}