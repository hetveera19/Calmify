/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "OrderDetails", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
@Entity
@NamedQueries({
        // This named query is used in OrderDetailsFacade
        @NamedQuery(name = "OrderDetails.findOrdersByUserId", query = "SELECT o FROM OrderDetails o where o.user.id= :userId"),
        @NamedQuery(name = "OrderDetails.findOrderById", query = "SELECT o FROM OrderDetails o where o.id=:orderId")
})
public class OrderDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date")
    private Date orderDate;

    public String getOrderDate() {
        return orderDate.toString().substring(0,11);
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                ", orderDate=" + orderDate +
                '}';
    }
}