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

import com.paypal.base.rest.PayPalRESTException;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.CartController;
import edu.vt.controllers.UserController;
import edu.vt.payment.PaymentServices;

@WebServlet("/authorizePayment")
public class AuthorizePayment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    CartController cartController;

    public AuthorizePayment() {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User signedInUser = (User) request.getSession().getAttribute("user");
        System.out.println(signedInUser.getEmail());

        double CartPrice = cartController.getTotalPrice(signedInUser.getId());
        int Shipping = 10;
        int Tax = (int) Math.ceil(CartPrice * 0.05);
        double Total = CartPrice + Shipping + Tax;

        String product = "Item";
        String subtotal = String.valueOf(CartPrice);
        String shipping = String.valueOf(Shipping);
        String tax = String.valueOf(Tax);
        String total=String.valueOf(Total);

        OrderDetail orderDetail = new OrderDetail(product, subtotal, shipping, tax, total , signedInUser);
        System.out.print("Order Details: "+orderDetail.toString());
        try {
            PaymentServices paymentServices = new PaymentServices();
            String approvalLink = paymentServices.authorizePayment(orderDetail);

            response.sendRedirect(approvalLink);

        } catch (PayPalRESTException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("/shop/Error.xhtml").forward(request, response);
        }
    }
}