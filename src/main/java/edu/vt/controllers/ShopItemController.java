/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.ShopItems;
import edu.vt.FacadeBeans.ShopFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("shopItemController")
@SessionScoped
public class ShopItemController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<ShopItems> listOfShopItems;
/*
    @Inject
    private CartController cartController;
*/
    private ShopItems selectedShopItem;

    // Used for Search Processing
    private String searchQuery;
    private String category;
    private double minPrice;
    private double maxPrice;
    private List<String> listOfCategories;
    private boolean cartButtonValue;
    private List<ShopItems> itemsAddedToCart;

    public ShopItems getSelected() {
        return selected;
    }

    public void setSelected(ShopItems selected) {
        this.selected = selected;
    }

    public boolean isCartButtonValue() {
        System.out.println("Button checked: "+cartButtonValue);
        return cartButtonValue;
    }

    public void setCartButtonValue(boolean cartButtonValue) {
        this.cartButtonValue = cartButtonValue;
    }

    //private SearchedItem selected;
    private ShopItems selected;

    @EJB
    private ShopFacade shopFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */


//    public List<String> getItemCategories(){
//        listOfCategories=Constants.category;
//        return listOfCategories;
//    }

    public List<ShopItems> getListOfShopItems() {
        if (listOfShopItems == null) {
            listOfShopItems = shopFacade.findAll();
        }
//        System.out.println("NAME OF 1st ITEM: "+listOfShopItems.get(0).getName());
        return listOfShopItems;
    }

    public ShopItems getSelectedShopItem() {
        return selectedShopItem;
    }

    public void setSelectedShopItem(ShopItems selectedShopItem) {
        this.selectedShopItem = selectedShopItem;
    }


    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     ShopFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    shopFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     ShopFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    shopFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
            }
        }
    }

    public void prepareCreate() {
        /*
        Instantiate a new ShopItem object and store its object reference into
        instance variable 'selected'. The ShopItem class is defined in ShopItems.java
         */
        selected = new ShopItems();
    }

    /*
    An enum is a special Java type used to define a group of constants.
    The constants CREATE, DELETE and UPDATE are defined as follows in JsfUtil.java

            public enum PersistAction {
                CREATE,
                DELETE,
                UPDATE
            }
     */

    /*
    **********************************
    CREATE a New Shop Iten in the Database
    **********************************
     */
    public void create() {
        Methods.preserveMessages();

        persist(PersistAction.CREATE,"Shop Item was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }

    /*
    *************************************
    UPDATE Selected ShopItem in the Database
    *************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"ShopItem was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected ShopItem from the Database
    ***************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"ShopItem was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    /*
    ================
    Instance Methods
    ================

    ---------------------------------------------------------
    Search for items according to category and price entered by the user
    ---------------------------------------------------------
    /**
     * Return the content of a given URL as String
     *
     * @param apiURL: API URL to fetch the JSON data file from
     * @return JSON data obtained from the given API URL as String
     * @throws Exception
     */
    public String readUrlContent(String apiURL) throws Exception {
        /*
        reader is an object reference pointing to an object instantiated from the BufferedReader class.
        Currently, it is "null" pointing to nothing.
         */
        BufferedReader reader = null;

        try {
            // Create a URL object from the webServiceURL given
            URL url = new URL(apiURL);
            /*
            The BufferedReader class reads text from a character-input stream, buffering characters
            so as to provide for the efficient reading of characters, arrays, and lines.
             */
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // Create a mutable sequence of characters and store its object reference into buffer
            StringBuilder buffer = new StringBuilder();

            // Create an array of characters of size 10240
            char[] chars = new char[10240];

            int numberOfCharactersRead;
            /*
            The read(chars) method of the reader object instantiated from the BufferedReader class
            reads 10240 characters as defined by "chars" into a portion of a buffered array.

            The read(chars) method attempts to read as many characters as possible by repeatedly
            invoking the read method of the underlying stream. This iterated read continues until
            one of the following conditions becomes true:

                (1) The specified number of characters have been read, thus returning the number of characters read.
                (2) The read method of the underlying stream returns -1, indicating end-of-file, or
                (3) The ready method of the underlying stream returns false, indicating that further input requests would block.

            If the first read on the underlying stream returns -1 to indicate end-of-file then the read(chars) method returns -1.
            Otherwise the read(chars) method returns the number of characters actually read.
             */
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            // Return the String representation of the created buffer
            return buffer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
