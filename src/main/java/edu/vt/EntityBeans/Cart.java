/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "Cart", indexes = {
        @Index(name = "item_id", columnList = "item_id"),
        @Index(name = "user_id", columnList = "user_id")
})
@Entity

@NamedQueries({
        // This named query is used in CartFacade
        @NamedQuery(name = "Cart.findShopItemsInUserCart", query = "SELECT s FROM Cart c,ShopItems s where c.user.id = :userId and s.id= c.item.id")
        , @NamedQuery(name = "Cart.findItemsInUserCart", query = "SELECT c FROM Cart c where c.user.id = :userId ")
        , @NamedQuery(name = "Cart.findItemCountByUserId", query = "SELECT count(c) FROM Cart c where c.user.id = :userId ")
        , @NamedQuery(name = "Cart.findItemCountByUserItemId", query = "SELECT count(c) FROM Cart c where c.user.id = :userId and c.item.id = :itemId ")
        , @NamedQuery(name = "Cart.findItemsByUserId", query = "SELECT c FROM Cart c where c.user.id = :userId and c.item.id = :itemId ")

})

public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ShopItems item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ShopItems getItem() {
        return item;
    }

    public void setItem(ShopItems item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}