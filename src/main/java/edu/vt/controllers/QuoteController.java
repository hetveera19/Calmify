package edu.vt.controllers;


import java.net.URL;
import edu.vt.globals.Methods;

import java.io.IOException;
import java.io.*;
import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;



@RequestScoped
@Named("quoteController")

public class QuoteController implements Serializable{

    public String getQuote() {
        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        try {
            String quoteJson = "https://zenquotes.io/api/random/[your_key]";

            // Obtain the JSON file (String of characters) containing the search results
            // The readUrlContent() method is given below
            String searchResultsJsonData = Methods.readUrlContent(quoteJson);
            // Create a new JSON array from the returned file
            //JSONArray searchResultsJson = new JSONArray(searchResultsJsonData);
            JSONObject searchResultsJson = new JSONObject(searchResultsJsonData);
            System.out.println(searchResultsJson.toString());
            String a = (String) searchResultsJson.get("a");
            String q = (String) searchResultsJson.get("q");
            return q+"by"+a;
        } catch (Exception ex) {
            Methods.showMessage("Information", "No Results!", "No recipe found for the search query!");
        }
        return null;
    }

}
