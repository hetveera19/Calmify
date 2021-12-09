/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.payment;

import java.util.*;

import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import edu.vt.payment.OrderDetail;
import edu.vt.EntityBeans.User;
import edu.vt.globals.Constants;

public class PaymentServices {
    private static final String CLIENT_ID = Constants.CLIENTID;
    private static final String CLIENT_SECRET = Constants.SECRET;
    private static final String MODE = "sandbox";
    private static final String PAYMENT_METHOD="paypal";

    public String authorizePayment(OrderDetail orderDetail)
            throws PayPalRESTException {

        Payer payer = getPayerInformation(orderDetail.getUser());
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(orderDetail);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        System.out.println(requestPayment.toString());

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);
        return getApprovalLink(approvedPayment);

    }

    private Payer getPayerInformation(User user) {
        Payer payer = new Payer();
        payer.setPaymentMethod(PAYMENT_METHOD);

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(Constants.CANCEL_URI);
        redirectUrls.setReturnUrl(Constants.RETURN_URI);

        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(OrderDetail orderDetail) {
        Details details = new Details();
        details.setShipping(orderDetail.getShipping());
        details.setSubtotal(orderDetail.getSubtotal());
        details.setTax(orderDetail.getTax());

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getProductName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setCurrency("USD");
        item.setName(orderDetail.getProductName());
        item.setPrice(orderDetail.getSubtotal());
        item.setTax(orderDetail.getTax());
        item.setQuantity("1");

        items.add(item);
        itemList.setItems(items);

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCity(orderDetail.getUser().getCity());
        shippingAddress.setRecipientName(orderDetail.getUser().getFirstName() + orderDetail.getUser().getLastName());
        shippingAddress.setState(orderDetail.getUser().getState());
        shippingAddress.setPostalCode(orderDetail.getUser().getZipcode());
        shippingAddress.setCountryCode("US");
        shippingAddress.setLine1(orderDetail.getUser().getAddress1());
        shippingAddress.setLine2(orderDetail.getUser().getAddress2());

        itemList.setShippingAddress(shippingAddress);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }
}