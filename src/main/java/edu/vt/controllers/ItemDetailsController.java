/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.controllers;

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.*;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("itemDetailsController")
@SessionScoped
public class ItemDetailsController implements Serializable {

    private List<ItemDetails> listOfItems = new ArrayList<>();

    @EJB
    ItemDetailsFacade itemDetailsFacade;

    @EJB
    CartFacade cartFacade;

    @EJB
    OrdersFacade ordersFacade;

    ItemDetails selected;

    public void addOrderItems(User signedInUser, int orderId) {
        try {
            for (Cart cart : cartFacade.getItemsInUserCart(signedInUser.getId())) {
                ItemDetails itemDetails = new ItemDetails();
                itemDetails.setName(cart.getItem().getName());
                itemDetails.setDescription(cart.getItem().getDescription());
                itemDetails.setCategory(cart.getItem().getCategory());
                itemDetails.setImageUrl(cart.getItem().getImageUrl());
                itemDetails.setPrice(cart.getItem().getPrice());
                itemDetails.setShortDescription(cart.getItem().getShortDescription());
                itemDetails.setOrder(ordersFacade.find(orderId));
                itemDetails.setItem(cart.getItem());
                itemDetails.setQuantity(cart.getQuantity());
                System.out.println(itemDetails.toString());
                itemDetailsFacade.edit(itemDetails);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
