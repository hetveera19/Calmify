/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Blog;
import edu.vt.EntityBeans.Cart;
import edu.vt.EntityBeans.ShopItems;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CartFacade extends AbstractFacade<Cart> {
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
    public CartFacade() {
        super(Cart.class);
    }

    public void create(){

    }

    public List<ShopItems> getShopItemsInUserCart(Integer primaryKey) {
        return entityManager.createNamedQuery("Cart.findShopItemsInUserCart")
                .setParameter("userId", primaryKey)
                .getResultList();
    }

    public List<Cart> getItemsInUserCart(Integer primaryKey) {
        return entityManager.createNamedQuery("Cart.findItemsInUserCart")
                .setParameter("userId", primaryKey)
                .getResultList();
    }

    public long findItemCountbyUserId(Integer UserId)
    {
        return (long) entityManager.createNamedQuery("Cart.findItemCountByUserId")
                .setParameter("userId", UserId)
                .getSingleResult();
    }

    public long findItemCountbyUserItemId(Integer UserId , Integer ItemId)
    {
        return (long) entityManager.createNamedQuery("Cart.findItemCountByUserItemId")
                .setParameter("userId", UserId)
                .setParameter("itemId", ItemId)
                .getSingleResult();
    }

    public Cart findItembyUserId(Integer UserId , Integer ItemId)
    {
        return (Cart) entityManager.createNamedQuery("Cart.findItemsByUserId")
                .setParameter("userId", UserId)
                .setParameter("itemId", ItemId)
                .getSingleResult();
    }

    public int createCartItem(Cart cart){
        getEntityManager().persist(cart);
        return cart.getId();
    }
}
