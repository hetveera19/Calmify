/*
 * Created by Het Veera on 2021.11.1
 * Copyright © 2021 Het Veera. All rights reserved.
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

                     VideoFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    shopFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     VideoFacade inherits the remove(selected) method from the AbstractFacade class.
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
        Instantiate a new Video object and store its object reference into
        instance variable 'selected'. The Video class is defined in Video.java
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
    CREATE a New Video in the Database
    **********************************
     */
    public void create() {
        Methods.preserveMessages();

        persist(PersistAction.CREATE,"Video was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }

    /*
    *************************************
    UPDATE Selected Video in the Database
    *************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"Video was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected Video from the Database
    ***************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"Video was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfShopItems = null;    // Invalidate listOfShopItems to trigger re-query.
        }
    }
    //private List<Shop> listOfShopItems = null;
    //    public String getSearchString() {
//        return searchString;
//    }
//
//    public void setSearchString(String searchString) {
//        this.searchString = searchString;
//    }
//
//    public SearchedItem getSelected() {
//        return selected;
//    }
//
//    public void setSelected(SearchedItem selected) {
//        this.selected = selected;
//    }
//
//    public String getDietLabel() {
//        return dietLabel;
//    }
//
//    public void setDietLabel(String dietLabel) {
//        this.dietLabel = dietLabel;
//    }
//
//    public String getHealthLabel() {
//        return healthLabel;
//    }
//
//    public void setHealthLabel(String healthLabel) {
//        this.healthLabel = healthLabel;
//    }
//
//    public String getMaxNoOfRecipes() {
//        return maxNoOfRecipes;
//    }
//
//    public void setMaxNoOfRecipes(String maxNoOfRecipes) {
//        this.maxNoOfRecipes = maxNoOfRecipes;
//    }
//
//    public String getSearchApiUrl() {
//        return searchApiUrl;
//    }
//
//    public void setSearchApiUrl(String searchApiUrl) {
//        this.searchApiUrl = searchApiUrl;
//    }
//    public List<String> getListOfDietLabels(){
//        return Constants.DIET_LABELS;
//    }
//
//    public List<String> getListOfHealthLabels(){
//        return Constants.HEALTH_LABELS;
//    }
//
//    public List<String> getListOfMaximumNumberOfRecipies(){
//        return Constants.MAX_NUMBER_OF_RECIPIES;
//    }
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
     */
//    public String performSearch() {
//
//        selected = null;
//        listOfSearchedItems = new ArrayList<>();
//        dietLabel=dietLabel.toLowerCase();
//        healthLabel=healthLabel.toLowerCase();
//        //searchApiUrl = "https://api.edamam.com/search?q="+searchString+"&app_id="+Constants.FREE_RECIPE_SEARCH_API_ID+"&app_key="+Constants.FREE_RECIPE_SEARCH_API_KEY+"&from=0&to="+maxNoOfRecipes+"&diet="+dietLabel+"&health="+healthLabel+"";
//
//        System.out.println(searchApiUrl);
//        try {
//                // Obtain the JSON file containing the recipe search results at the given page number
//                String recipeSearchResultsJsonData = readUrlContent(searchApiUrl);
//
//                // Instantiate a JSON object from the JSON data obtained
//                JSONObject resultsJsonObject = new JSONObject(recipeSearchResultsJsonData);
//
//                // Obtain a JSONArray of recipe objects (Each recipe is represented as a JSONObject)
//                JSONArray jsonArrayFoundRecipes = resultsJsonObject.getJSONArray("hits");
//
//                int index = 0;
//
//                //System.out.println(jsonArrayFoundRecipes.toString());
//
//                if (jsonArrayFoundRecipes.length() > index) {
//
//                    while (jsonArrayFoundRecipes.length() > index) {
//
//                        // Get the recipe JSONObject at index
//                        JSONObject jsonObject = jsonArrayFoundRecipes.getJSONObject(index);
//                        JSONObject foundRecipe = jsonObject.getJSONObject("recipe");
//                        /*
//                        ======== JSON Data Optional Processing ========
//
//                        optBoolean(String key, boolean defaultValue)
//                            Obtain the Boolean value for the given "key" if a value exists; otherwise, use the defaultValue.
//
//                        optDouble(String key, double defaultValue)
//                            Obtain the Double value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.
//
//                        optInt(String key, int defaultValue)
//                            Obtain the Int value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.
//
//                        optLong(String key, long defaultValue)
//                            Obtain the Long value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.
//
//                        optString(String key, String defaultValue)
//                            Obtain the String value for the given "key" if a value exists; otherwise, use the defaultValue.
//
//                        ============================
//                        Recipe Label
//                        ============================
//                         */
//                        String labelA = foundRecipe.optString("label", "");
//                        if (labelA.equals("")) {
//                            index++;
//                            continue;
//                        }
//
//                        /*
//                        =========================
//                        Recipe Image URL
//                        =========================
//                         */
//                        String image_urlA = foundRecipe.optString("image", "");
//                        if (image_urlA.equals("")) {
//                            image_urlA = "No image url is provided!";
//                        }
//
//                        /*
//                        ==================
//                        Recipe Source / Publisher
//                        ==================
//                         */
//                        String sourceA = foundRecipe.optString("source", "");
//                        if (sourceA.equals("")) {
//                            sourceA = "No Source Provided";
//                        }
//
//                        /*
//                        =====================================
//                        Recipe website
//                        =====================================
//                         */
//                        String websiteUrlA = foundRecipe.optString("url", "");
//                        if (websiteUrlA == "") {
//                            index++;
//                            continue;
//                        }
//
//                        /*
//                     ==================
//                     Recipe Diet Labels
//                     ==================
//                     */
//                        JSONArray dietLabelsAsArray = foundRecipe.getJSONArray("dietLabels");
//
//                        String dietLabelsA = "";
//                        int dietLabelsArrayLength = dietLabelsAsArray.length();
//
//                        if (dietLabelsArrayLength > 0) {
//                            for (int j = 0; j < dietLabelsArrayLength; j++) {
//                                String aDietLabel = dietLabelsAsArray.optString(j, "");
//                                if (j < dietLabelsArrayLength - 1) {
//                                    aDietLabel = aDietLabel + ", ";
//                                }
//                                dietLabelsA = dietLabelsA + aDietLabel;
//                            }
//                        }
//
//                        /*
//                     ====================
//                     Recipe Health Labels
//                     ====================
//                     */
//                        JSONArray healthLabelsAsArray = foundRecipe.getJSONArray("healthLabels");
//
//                        String healthLabelsA = "";
//                        int healthLabelsArrayLength = healthLabelsAsArray.length();
//
//                        if (healthLabelsArrayLength > 0) {
//                            for (int j = 0; j < healthLabelsArrayLength; j++) {
//                                String aHealthLabel = healthLabelsAsArray.optString(j, "");
//                                if (j < healthLabelsArrayLength - 1) {
//                                    aHealthLabel = aHealthLabel + ", ";
//                                }
//                                healthLabelsA = healthLabelsA + aHealthLabel;
//                            }
//                        }
//
//                        /*
//                     =======================
//                     Recipe Ingredient Lines
//                     =======================
//                     */
//                        JSONArray ingredientLinesAsArray = foundRecipe.getJSONArray("ingredientLines");
//
//                        String ingredientLinesA = "";
//                        int ingredientLinesArrayLength = ingredientLinesAsArray.length();
//
//                        if (ingredientLinesArrayLength > 0) {
//                            for (int j = 0; j < ingredientLinesArrayLength; j++) {
//                                String anIngredientLine = ingredientLinesAsArray.optString(j, "");
//                                if (j < ingredientLinesArrayLength - 1) {
//                                    anIngredientLine = anIngredientLine + ", ";
//                                }
//                                ingredientLinesA = ingredientLinesA + anIngredientLine;
//                            }
//                        }
//
//                        /*
//                     ===============
//                     Recipe Calories
//                     ===============
//                     */
//                        Double caloriesA = foundRecipe.optDouble("calories", 0.0);
//
//                        if (caloriesA == 0.0) {
//                            // Skip the recipe with unknown calories
//                            index++;
//                            continue;   // Jump to the next iteration
//
//                        } else {
//                            /* Round the calories value to 2 decimal places */
//                            caloriesA = caloriesA * 100;
//                            caloriesA = (double) Math.round(caloriesA);
//                            caloriesA = caloriesA / 100;
//                        }
//
//
//                        SearchedItem recipe = new SearchedItem(labelA, image_urlA, sourceA,
//                                websiteUrlA, dietLabelsA, healthLabelsA, ingredientLinesA, caloriesA);
//
//                        System.out.println(recipe.toString());
//
//                        // Add the newly created recipe object to the list of searchedRecipes
//                        listOfSearchedRecipes.add(recipe);
//                        index++;
//                    }
//
//                }
//            //}
//
//        } catch (Exception ex) {
//            Methods.showMessage("Fatal Error", "The Recipe API limit of 4 accesses per second has been exceeded!",
//                    "See: " + ex.getMessage());
//            clear();
//            searchString = "";
//            return "";
//        }
//        searchString="";
//        return "/apiSearch/ApiSearchResults?faces-redirect=true";
//    }

//    public void clear() {
//        searchString="";
//        healthLabel="";
//        dietLabel="";
//        maxNoOfRecipes="";
//    }

    /*
     ************************
     *   Read URL Content   *
     ************************
     */
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
