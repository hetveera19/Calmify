/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Cart;
import edu.vt.EntityBeans.ItemDetails;
import edu.vt.EntityBeans.OrderDetails;
import edu.vt.EntityBeans.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ItemDetailsFacade extends AbstractFacade<ItemDetails> {

    @PersistenceContext(unitName = "Calmify-PU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public ItemDetailsFacade() {
        super(ItemDetails.class);
    }

    public int createOrder(ItemDetails item){
        getEntityManager().persist(item);
        return item.getId();
    }

    public List<ItemDetails> findItemRelatedToOrderId(int orderId){
        return entityManager.createNamedQuery("ItemDetails.findByOrderId")
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
