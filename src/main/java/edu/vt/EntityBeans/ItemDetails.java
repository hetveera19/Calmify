/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "ItemDetails", indexes = {
        @Index(name = "item_id", columnList = "item_id"),
        @Index(name = "order_id", columnList = "order_id")
})
@Entity
@NamedQueries({
        // This named query is used in ItemDetailsFacade
        @NamedQuery(name = "ItemDetails.findByOrderId", query = "SELECT i FROM ItemDetails i where i.order.id= :orderId")
        //@NamedQuery(name = "Shop.findByCategoryAndPrice", query = "SELECT s FROM Shop s WHERE s.category=category and s.price>=minPrice and s.price<=maxPrice")
})
public class ItemDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 256)
    private String name;

    @Column(name = "description", nullable = false, length = 2048)
    private String description;

    @Column(name = "category", nullable = false, length = 64)
    private String category;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "short_description", nullable = false, length = 1024)
    private String shortDescription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetails order;

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

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}