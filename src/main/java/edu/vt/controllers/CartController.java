/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.controllers;

import edu.vt.EntityBeans.Cart;
import edu.vt.EntityBeans.ShopItems;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.BlogFacade;
import edu.vt.FacadeBeans.CartFacade;
import edu.vt.FacadeBeans.ShopFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("cartController")
@SessionScoped
public class CartController implements Serializable {

    @EJB
    private CartFacade cartFacade;

    @EJB
    private ShopFacade shopFacade;

    @Inject
    private UserController userController;

    public ShopItems getItem() {
        return item;
    }

    public void setItem(ShopItems item) {
        this.item = item;
    }

    ShopItems item;

    private List<Cart> listOfItemsAddedToCart= new ArrayList<>();
    private Cart selected;
    private double totalPrice=0.0;

    public List<Cart> getListOfItemsAddedToCart() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        listOfItemsAddedToCart=cartFacade.getItemsInUserCart(signedInUser.getId());
        return listOfItemsAddedToCart;
    }

    public void setListOfItemsAddedToCart(List<Cart> listOfItemsAddedToCart) {
        this.listOfItemsAddedToCart = listOfItemsAddedToCart;
    }

    public long getListofItemsinCart()
    {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        long count = cartFacade.findItemCountbyUserId(signedInUser.getId());
        return count;
    }

    public String removeItemFromCart(int id)
    {

        Methods.preserveMessages();

        selected = cartFacade.find(id);
        persist(JsfUtil.PersistAction.DELETE, "Item removed from cart Successfully!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfItemsAddedToCart = null;     // Invalidate listOfItemsAddedToCart to trigger re-query.
            return "/Cart?faces-redirect=true";
        }
        return "/Cart?faces-redirect=false";
    }

    public void removeAllUserItemFromCart(User signedInUser)
    {
        System.out.println("Inside removeAllUserItemFromCart");
        for (Cart cart : cartFacade.getItemsInUserCart(signedInUser.getId())) {
            try {
                cartFacade.remove(cart);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    public void createCartItem(int id){

        System.out.println("Inside createCartItem");
        Methods.preserveMessages();

        if (userController.isLoggedIn() == false)
        {
            Methods.showMessage("Information",
                    "Unable to Add Item!", "To add a Item to Cart, a user must have signed in!");
        }
        else {

            /*
                'user', the object reference of the signed-in user, was put into the SessionMap
                in the initializeSessionMap() method in LoginManager upon user's sign in.
            */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            try {
                long itemCount = cartFacade.findItemCountbyUserItemId(signedInUser.getId() , id);
                if (itemCount == 0) {
                    System.out.println("Item Doesn't exist");
                    ShopItems shopItem = shopFacade.find(id);
                    selected = new Cart();
                    selected.setUser(signedInUser);
                    selected.setItem(shopItem);
                    selected.setQuantity(1);
                    persist(JsfUtil.PersistAction.CREATE, "Item added to cart!");

                }
                else {
                    System.out.println("Item Exists");
                    Cart cart = cartFacade.findItembyUserId(signedInUser.getId() , id);
                    cart.setQuantity(cart.getQuantity() + 1);
                    selected = cart;
                    persist(JsfUtil.PersistAction.UPDATE, "Item added to cart!");
                }
            }
            catch (Exception e) {
                    System.out.println(e);
            }

            if (!JsfUtil.isValidationFailed()) {

                // No JSF validation error. The CREATE operation is successfully performed.
                selected = null;        // Remove selection
                listOfItemsAddedToCart = null;      // Invalidate listOfItemsAddedToCart to trigger re-query.

            }
        }
    }

    public Double getTotalPrice(int signedInUserId) {

        Double price = 0.0;
        for(Cart cartItem:cartFacade.getItemsInUserCart(signedInUserId)){
            price+=cartItem.getItem().getPrice() * cartItem.getQuantity();
        }
        return price;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }


    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     CartFacade inherits the edit(selected) method from the AbstractFacade class.
                     while implements the createCartItem(selected) method
                     */
                    int id;
                    if (persistAction == JsfUtil.PersistAction.CREATE) {
                        id = cartFacade.createCartItem(selected);
                    }
                    else {
                        id = selected.getId();
                        cartFacade.edit(selected);
                    }
                    /*
                    InputStream is an abstract class, which is the superclass of all classes representing
                    an input stream of bytes. It is imported as: import java.io.InputStream;
                    Convert the uploaded file into an input stream of bytes.
                    */

                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     CartFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    cartFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
            }
        }
    }
}
