/*
 * Created by Anshika Tyagi on 2021.9.26
 * Copyright © 2021 Anshika Tyagi. All rights reserved.
 */
package edu.vt.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("sliderController")
@RequestScoped
public class SliderController {

    // The List contains image filenames, e.g., photo1.png, photo2.png, etc.
    private List<String> listOfSliderImageFilenames;
    //private List<ResponsiveOption> responsiveOptions1;
    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization 
    method init() is the first method invoked before this class is put into service. 
    */
    @PostConstruct
    public void init() {
        listOfSliderImageFilenames = new ArrayList<>();

        for (int i = 1; i <= 13; i++) {
            listOfSliderImageFilenames.add("picture-" + i + ".jpg");
        }
//        responsiveOptions1 = new ArrayList<>();
//        responsiveOptions1.add(new ResponsiveOption("1024px", 5));
//        responsiveOptions1.add(new ResponsiveOption("768px", 3));
//        responsiveOptions1.add(new ResponsiveOption("560px", 1));
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<String> getListOfSliderImageFilenames() {
        return listOfSliderImageFilenames;
    }



}


