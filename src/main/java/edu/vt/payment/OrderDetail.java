/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.payment;

import edu.vt.EntityBeans.User;

import javax.inject.Named;

@Named("orderdetail")
public class OrderDetail {

    private String productName;
    private float subtotal;
    private float shipping;
    private float tax;
    private float total;
    private int quantitiy;
    private User user;

    @Override
    public String toString() {
        return "OrderDetail{" +
                "productName='" + productName + '\'' +
                ", subtotal=" + subtotal +
                ", shipping=" + shipping +
                ", tax=" + tax +
                ", total=" + total +
                ", quantitiy=" + quantitiy +
                '}';
    }

    public OrderDetail(String productName, String subtotal,
                       String shipping, String tax, String total, User user) {
        this.productName = productName;
        this.subtotal = Float.parseFloat(subtotal);
        this.shipping = Float.parseFloat(shipping);
        this.tax = Float.parseFloat(tax);
        this.total = Float.parseFloat(total);
        this.user = user;
    }

    public OrderDetail(){}

    public String getProductName() {
        return productName;
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal);
    }

    public String getShipping() {
        return String.format("%.2f", shipping);
    }

    public String getTax() {
        return String.format("%.2f", tax);
    }

    public String getTotal() {
        return String.format("%.2f", total);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
