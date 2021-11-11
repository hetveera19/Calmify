/*
 * Created by Osman Balci on 2021.11.4
 * Copyright © 2021 Osman Balci. All rights reserved.
 */

package galleria;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named(value = "photoService")
@ApplicationScoped
public class PhotoService {
    /*
    ============================
    Instance Variable (Property)
    ============================
     */
    private List<Photo> listOfPhotos;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization
    method init() is the first method invoked before this class is put into service.
     */
    @PostConstruct
    public void init() {
        listOfPhotos = new ArrayList<>();


        for (int i = 1; i <= 13; i++) {
            listOfPhotos.add(new Photo("/resources/images/photos/picture-" + i + ".jpg","unused",
                    "Description for Photo"+i, "Photo"+i));
        }
        /*listOfPhotos.add(new Photo("/resources/images/photos/picture-1.jpg", "unused",
                "Description for Photo 1", "Photo 1"));
        listOfPhotos.add(new Photo("/resources/images/photos/picture-2.jpg", "unused",
                "Description for Photo 2", "Photo 2"));
        listOfPhotos.add(new Photo("/resources/images/photos/picture-3.jpg", "unused",
                "Description for Photo 3", "Photo 3"));
        listOfPhotos.add(new Photo("/resources/images/photos/picture-4.jpg", "unused",
                "Description for Photo 4", "Photo 4"));
        listOfPhotos.add(new Photo("/resources/images/photos/picture-5.jpg", "unused",
                "Description for Photo 5", "Photo 5"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo6.jpg", "unused",
                "Description for Photo 6", "Photo 6"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo7.jpg", "unused",
                "Description for Photo 7", "Photo 7"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo8.jpg", "unused",
                "Description for Photo 8", "Photo 8"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo9.jpg", "unused",
                "Description for Photo 9", "Photo 9"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo10.jpg", "unused",
                "Description for Photo 10", "Photo 10"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo11.jpg", "unused",
                "Description for Photo 11", "Photo 11"));
        listOfPhotos.add(new Photo("/resources/images/photos/photo12.jpg", "unused",
                "Description for Photo 12", "Photo 12"));*/
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<Photo> getListOfPhotos() {
        return listOfPhotos;
    }
}
