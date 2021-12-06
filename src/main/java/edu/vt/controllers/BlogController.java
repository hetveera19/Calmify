/*
 * Created by Anubhav Nanda on 2021.10.29
 * Copyright © 2021 Anubhav Nanda. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Blog;
import edu.vt.EntityBeans.Comment;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserPhoto;
import edu.vt.FacadeBeans.BlogFacade;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.imgscalr.Scalr;
import org.primefaces.model.file.UploadedFile;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "recipeController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("blogController")

/*
The @SessionScoped annotation preserves the values of the recipeController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the RecipeController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
 */
public class BlogController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    RecipeFacade bean into the instance variable 'recipeFacade' after it is instantiated at runtime.
     */
    @EJB
    private BlogFacade blogFacade;

    @EJB
    private CommentFacade commentFacade;

    // List of object references of Recipe objects
    private List<Blog> listOfPublishedBlogs = null;

    private List<Blog> listofUserBlogs = null;

    // selected = object reference of a selected Recipe object
    private Blog selected;

    // Flag indicating if recipe data changed or not
    private Boolean blogDataChanged;

    private double blogRating = 0;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    private String test;

    private String filename;
    private UploadedFile file;

    public List<Blog> getListOfPublishedBlogs() {
        if (listOfPublishedBlogs == null) {
            listOfPublishedBlogs = blogFacade.findBlogByPublished(true);
        }
        return listOfPublishedBlogs;
    }

    public List<Blog> getListOfUserBlogs() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        // Obtain the database primary key of the signedInUser object
        Integer primaryKey = signedInUser.getId();

        if (listofUserBlogs == null) {
            listofUserBlogs = blogFacade.findUserBlogsByUserPrimaryKey(primaryKey);
        }
        return listofUserBlogs;
    }

    public Blog getSelected() {
        return selected;
    }

    public void setSelected(Blog selected) {
        this.selected = selected;
    }

    public Boolean getBlogDataChanged() {
        return blogDataChanged;
    }

    public void setBlogDataChanged(Boolean blogDataChanged) {
        this.blogDataChanged = blogDataChanged;
    }

    public int getBlogRating(int id) {
        long ratingCount = commentFacade.findRatingCountByBlogPrimaryKey(id);

        if (ratingCount == 0)
            return 0;

        blogRating = (int) commentFacade.findRatingByBlogPrimaryKey(id);
        //blogRating = (int) rating ;
        return (int) blogRating;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }


    /*
    ================
    Instance Methods
    ================
     */

    /*
     ****************************************
     *   Unselect Selected Recipe Object   *
     ****************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     *************************************
     *   Cancel and Display ListOfFavorites.xhtml   *
     *************************************
     */
    public String cancel() {
        // Unselect previously selected recipe object if any
        selected = null;
        return "/blog/List?faces-redirect=true";
    }

    public String blogPhoto(int id, String extension) {
        /*
        The database primary key of the signed-in User object was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
        */

        System.out.println(id);
        System.out.println(Constants.BLOGS_URI + "defaultBlogPhoto.jpeg");

        if (!(new File(Constants.BLOGS_ABSOLUTE_PATH + id + "." + "jpeg").exists())) {
            // No blog photo exists. Return defaultBlogPhoto.png.
            return Constants.BLOGS_URI + "defaultBlogPhoto.jpeg";
        }

        /*
        photoList.get(0) returns the object reference of the first Photo object in the list.
        getThumbnailFileName() message is sent to that Photo object to retrieve its
        thumbnail image file name, e.g., 5_thumbnail.jpeg
         */
        System.out.println(Constants.BLOGS_URI + id + "." + "jpeg");
        return Constants.BLOGS_URI + id + "." + "jpeg";
    }

    public boolean isPublished(int id) {
        /*
        The username of a signed-in user is put into the SessionMap in the
        initializeSessionMap() method in LoginManager upon user's sign in.
        If there is a username, that means, there is a signed-in user.
         */
        System.out.println(id);
        Blog blog = blogFacade.find(id);
        return blog.getPublished();
    }

    public void publish(int id) {
        Methods.preserveMessages();

        /*
        Instantiate a new Recipe object and store its object reference into
        instance variable 'selected'. The Recipe class is defined in Recipe.java
         */
        System.out.println("In Prepare publish");
        selected = blogFacade.find(id);
        System.out.println(selected.getId());
        selected.setPublished(true);

        persist(PersistAction.UPDATE, "Blog Successfully Published!");

        System.out.println("Test");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPublishedBlogs = null;     // Invalidate listOfRecipes to trigger re-query.
            listofUserBlogs = null;
        }


    }

    /*
     ***************************************
     *   Prepare to Create a New Recipe    *
     ***************************************
     */
    public String prepareCreate() {
        /*
        Instantiate a new Recipe object and store its object reference into
        instance variable 'selected'. The Recipe class is defined in Recipe.java
         */
        System.out.println("In Prepare Create");
        selected = new Blog();
        return "/blog/SpillYourThoughts?faces-redirect=true";

    }

    public String prepareEdit() {
        /*
        Instantiate a new Recipe object and store its object reference into
        instance variable 'selected'. The Recipe class is defined in Recipe.java
         */
        System.out.println("In Blog Edit");
        System.out.println(selected.getId());
        return "/blog/EditYourThoughts?faces-redirect=true";

    }

    /*
     ********************************************
     *   CREATE a New Recipe in the Database       *
     ********************************************
     */
    public void create(int publish) {
        Methods.preserveMessages();

        /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
        */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");
        System.out.println(publish);

        boolean published = false;

        selected.setUser(signedInUser);
        selected.setPublicationDate(new Date());
        selected.setPublished(published);

        System.out.println("1");


        // Check if a file is selected
        if (file.getSize() == 0) {
            Methods.showMessage("Information", "No File Selected!",
                    "You need to choose a file first before clicking Upload.");
        }

        /*
        MIME (Multipurpose Internet Mail Extensions) is a way of identifying files on
        the Internet according to their nature and format.

        A "Content-type" is simply a header defined in many protocols, such as HTTP, that
        makes use of MIME types to specify the nature of the file currently being handled.

        Some MIME file types: (See http://www.freeformatter.com/mime-types-list.html)

            JPEG Image      = image/jpeg or image/jpg
            PNG image       = image/png
            GIF image       = image/gif
            Plain text file = text/plain
            HTML file       = text/html
            JSON file       = application/json

         Some of the MIME type mappings are specified in web.xml
         */
        // Obtain the uploaded file's MIME file type
        String mimeFileType = file.getContentType();

        System.out.println("2");

        if (mimeFileType.startsWith("image/")) {
            // The uploaded file is an image file
            /*
            The subSequence() method returns the portion of the mimeFileType string from the 6th
            position to the last character. Note that it starts with "image/" which has 6 characters at
            positions 0,1,2,3,4,5. Therefore, we start the subsequence at position 6 to obtain the file extension.
             */
            String fileExtension = mimeFileType.subSequence(6, mimeFileType.length()).toString();

            String fileExtensionInCaps = fileExtension.toUpperCase();

            switch (fileExtensionInCaps) {
                case "JPG":
                case "JPEG":
                case "PNG":
                case "GIF":
                    // File is an acceptable image type
                    break;
                default:
                    Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                            "Selected file type is not a JPG, JPEG, PNG, or GIF!");
            }
        } else {
            Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                    "Selected file type is not a JPG, JPEG, PNG, or GIF!");
        }

        System.out.println("3");

        // If it is an image file, obtain its file extension; otherwise, set png as the file extension anyway.
        String fileExtension = mimeFileType.startsWith("image/") ? mimeFileType.subSequence(6, mimeFileType.length()).toString() : "png";
        selected.setExtension(fileExtension);

        persist(PersistAction.CREATE, "Blog was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {

            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPublishedBlogs = null;      // Invalidate listOfRecipes to trigger re-query.
            listofUserBlogs = null;

        }
    }

    /*
     ***********************************************
     *   UPDATE Selected Recipe in the Database       *
     ***********************************************
     */
    public void update() {
        Methods.preserveMessages();

        // Check if a file is selected
        if (file.getSize() != 0) {

            //We need to remove the old file also

            Blog blog = blogFacade.find(selected.getId());
            String extension = blog.getExtension();

            String targetFileName = Constants.BLOGS_ABSOLUTE_PATH + selected.getId() + "." + extension;
            try {
                Files.deleteIfExists(Paths.get(targetFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }



            /*
        MIME (Multipurpose Internet Mail Extensions) is a way of identifying files on
        the Internet according to their nature and format.

        A "Content-type" is simply a header defined in many protocols, such as HTTP, that
        makes use of MIME types to specify the nature of the file currently being handled.

        Some MIME file types: (See http://www.freeformatter.com/mime-types-list.html)

            JPEG Image      = image/jpeg or image/jpg
            PNG image       = image/png
            GIF image       = image/gif
            Plain text file = text/plain
            HTML file       = text/html
            JSON file       = application/json

         Some of the MIME type mappings are specified in web.xml
         */

            // Obtain the uploaded file's MIME file type
            String mimeFileType = file.getContentType();

            System.out.println("2");

            if (mimeFileType.startsWith("image/")) {
                // The uploaded file is an image file
            /*
            The subSequence() method returns the portion of the mimeFileType string from the 6th
            position to the last character. Note that it starts with "image/" which has 6 characters at
            positions 0,1,2,3,4,5. Therefore, we start the subsequence at position 6 to obtain the file extension.
             */
                String fileExtension = mimeFileType.subSequence(6, mimeFileType.length()).toString();

                String fileExtensionInCaps = fileExtension.toUpperCase();

                switch (fileExtensionInCaps) {
                    case "JPG":
                    case "JPEG":
                    case "PNG":
                    case "GIF":
                        // File is an acceptable image type
                        break;
                    default:
                        Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                                "Selected file type is not a JPG, JPEG, PNG, or GIF!");
                }
            } else {
                Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                        "Selected file type is not a JPG, JPEG, PNG, or GIF!");
            }

            System.out.println("3");

            // If it is an image file, obtain its file extension; otherwise, set png as the file extension anyway.
            String fileExtension = mimeFileType.startsWith("image/") ? mimeFileType.subSequence(6, mimeFileType.length()).toString() : "png";
            selected.setExtension(fileExtension);
        }

        persist(PersistAction.UPDATE, "Blog was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPublishedBlogs = null;     // Invalidate listOfRecipes to trigger re-query.
            listofUserBlogs = null;

        }
    }

    /*
     ***********************************************
     *   DELETE Selected Recipe in the Database       *
     ***********************************************
     */
    public void destroy(int id) {
        Methods.preserveMessages();

        selected = blogFacade.find(id);
        System.out.println(selected.getId());
        persist(PersistAction.DELETE, "Blog was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPublishedBlogs = null;     // Invalidate listOfRecipes to trigger re-query.
            listofUserBlogs = null;

        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        System.out.println(selected.toString());
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

                     RecipeFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    int id;
                    if (persistAction == PersistAction.CREATE) {
                        id = blogFacade.createBlog(selected);
                    }
                    else {
                        id = selected.getId();
                        blogFacade.edit(selected);
                    }
                    /*
                    InputStream is an abstract class, which is the superclass of all classes representing
                    an input stream of bytes. It is imported as: import java.io.InputStream;
                    Convert the uploaded file into an input stream of bytes.
                    */

                    if (file.getSize() != 0) {
                        String targetFileName = id + "." + selected.getExtension();

                        /*
                        InputStream is an abstract class, which is the superclass of all classes representing
                        an input stream of bytes. It is imported as: import java.io.InputStream;
                        Convert the uploaded file into an input stream of bytes.
                        */
                        InputStream inputStream = file.getInputStream();

                        // Write the uploaded file's input stream of bytes under the photo object's
                        // filename using the inputStreamToFile method given below
                        File uploadedFile = inputStreamToFile(inputStream, targetFileName);
                    }

                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     RecipeFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    String targetFileName = Constants.BLOGS_ABSOLUTE_PATH + selected.getId() + "." + selected.getExtension();
                    Files.deleteIfExists(Paths.get(targetFileName));
                    blogFacade.remove(selected);
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

    /*
    ***************************************************
    Write Given InputStream into a File with Given Name
    ***************************************************
     */

    /**
     * @param inputStream of bytes to be written into file with name targetFilename
     * @return the created file targetFile
     */
    private File inputStreamToFile(InputStream inputStream, String targetFilename) throws IOException {

        File targetFile = null;

        try {
            /*
            inputStream.available() returns an estimate of the number of bytes that can be read from
            the inputStream without blocking by the next invocation of a method for this input stream.
            A memory buffer of bytes is created with the size of estimated number of bytes.
             */
            byte[] buffer = new byte[inputStream.available()];

            // Read the bytes of data from the inputStream into the created memory buffer.
            inputStream.read(buffer);

            // Create a new empty file with the given name targetFilename in the UserPhotoStorage directory
            targetFile = new File(Constants.BLOGS_ABSOLUTE_PATH, targetFilename);

            // A file OutputStream is an output stream for writing data to a file
            OutputStream outStream;

            /*
            FileOutputStream is intended for writing streams of raw bytes such as image data.
            Create a new FileOutputStream for writing to the empty targetFile
             */
            outStream = new FileOutputStream(targetFile);

            // Create the targetFile in the UserPhotoStorage directory with the inputStream given
            outStream.write(buffer);

            // Close the output stream and release any system resources associated with it.
            outStream.close();

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong in input stream to file!",
                    "See: " + ex.getMessage());
        }

        // Return the created targetFile
        return targetFile;
    }

    /*
    ********************************************
    Store Signed-In User's Thumbnail Photo Image
    ********************************************

    When signedInUser uploads a photo, a thumbnail (small) version of the photo image
    is created in this method by using the Scalr.resize method provided in the
    imgscalr (Java Image Scaling Library) imported as imgscalr-lib-4.2.jar
     */
    private void saveThumbnail(File inputFile, UserPhoto inputPhoto) {

        try {
            // Buffer the photo image from the uploaded inputFile
            BufferedImage uploadedPhoto = ImageIO.read(inputFile);

            /*
            The thumbnail photo image size is set to 200x200px in Constants.java as follows:
            public static final Integer THUMBNAIL_SIZE = 200;

            If the signedInUser uploads a large photo file, we will scale it down to THUMBNAIL_SIZE
            and use it so that the size is reasonable for performance reasons.

            The photo image scaling is properly done by using the imgscalr-lib-4.2.jar file.

            The thumbnail file is named as "userId_thumbnail.fileExtension",
            e.g., 5_thumbnail.jpg for signedInUser with id 5.
             */
            // Scale the uploaded photo image to the THUMBNAIL_SIZE using imgscalr.
            BufferedImage thumbnailPhoto = Scalr.resize(uploadedPhoto, Constants.THUMBNAIL_SIZE);

            // Create the thumbnail photo file in the UserPhotoStorage directory
            File thumbnailPhotoFile = new File(Constants.PHOTOS_ABSOLUTE_PATH, inputPhoto.getThumbnailFileName());

            /*
            NOTE: ImageIO is imported as: import javax.imageio.ImageIO;
            Write the thumbnailPhoto into thumbnailPhotoFile with the file extension.
             */
            ImageIO.write(thumbnailPhoto, inputPhoto.getExtension(), thumbnailPhotoFile);

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while saving the thumbnail file!",
                    "See: " + ex.getMessage());
        }
    }
}