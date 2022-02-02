/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.payment;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;



import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.ReviewPaymentController;

@WebServlet("/ReviewPayment")
public class ReviewPayment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PayerInfo payerInfo;
    private Transaction transaction;
    private ShippingAddress shippingAddress;
    private Payment payment;

    @Inject
    ReviewPaymentController reviewPaymentController;

    public ReviewPayment() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        System.out.println("Inside doGet");

        try {
            PaymentServices paymentServices = new PaymentServices();
            payment = paymentServices.getPaymentDetails(paymentId);

            payerInfo = payment.getPayer().getPayerInfo();
            transaction = payment.getTransactions().get(0);
            shippingAddress = transaction.getItemList().getShippingAddress();

            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);
            request.setAttribute("shippingAddress", shippingAddress);

            User signedInUser = (User) request.getSession().getAttribute("user");

            reviewPaymentController.setEmail(signedInUser.getEmail());
            reviewPaymentController.setFirstName(signedInUser.getFirstName());
            reviewPaymentController.setLastname(signedInUser.getLastName());
            reviewPaymentController.setShipping(transaction.getAmount().getDetails().getShipping());
            reviewPaymentController.setSubTotal(transaction.getAmount().getDetails().getSubtotal());
            reviewPaymentController.setTax(transaction.getAmount().getDetails().getTax());
            reviewPaymentController.setTotal(transaction.getAmount().getTotal());

            String url = "/ReviewPayment.xhtml?paymentId=" + paymentId + "&PayerID=" + payerId;
            request.getRequestDispatcher(url).forward(request, response);

        } catch (PayPalRESTException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("/shop/Error.xhtml").forward(request, response);
        }
    }

    public PayerInfo getPayerInfo() {
        return payerInfo;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public Payment getPayment() {
        return payment;
    }

}