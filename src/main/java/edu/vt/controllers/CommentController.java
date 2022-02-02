/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Blog;
import edu.vt.EntityBeans.Comment;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserPhoto;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.UserPhotoFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.event.RateEvent;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "commentController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("commentController")

/*
The @SessionScoped annotation preserves the values of the commentController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the CommentController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
 */
public class CommentController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CommentFacade bean into the instance variable 'commentFacade' after it is instantiated at runtime.
     */
    @EJB
    private CommentFacade commentFacade;

    @EJB
    private UserPhotoFacade userPhotoFacade;

    @Inject
    private UserController userController;

    // List of object references of Comment objects
    private List<Comment> listOfBlogComments = null;

    // selected = object reference of a selected Comment object
    private Comment selected;

    // Flag indicating if comment data changed or not
    private Boolean commentDataChanged;

    public List<Comment> getListOfBlogComments(Blog blog) {
        listOfBlogComments = null;
        if (listOfBlogComments == null && blog.getId() != null) {
            listOfBlogComments = commentFacade.findCommentsByBlogPrimaryKey(blog.getId());
            System.out.println(blog.getId());
        }
        return listOfBlogComments;
    }

    public Comment getSelected() {
        return selected;
    }

    public void setSelected(Comment selected) {
        this.selected = selected;
    }

    public Boolean getCommentDataChanged() {
        return commentDataChanged;
    }

    public void setCommentDataChanged(Boolean commentDataChanged) {
        this.commentDataChanged = commentDataChanged;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     ****************************************
     *   Unselect Selected Comment Object   *
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
        // Unselect previously selected blog object if any
        selected = null;
        return "/blog/List?faces-redirect=true";
    }

    public void setRating(){
        System.out.println("In Set Rating");
        selected.setRating(1.0F);
    }

    public String userPhoto(int id) {
        /*
        The database primary key of the signed-in User object was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */

        List<UserPhoto> photoList = userPhotoFacade.findPhotosByUserPrimaryKey(id);

        if (photoList.isEmpty()) {
            // No user photo exists. Return defaultUserPhoto.png.
            return Constants.PHOTOS_URI + "defaultUserPhoto.png";
        }

        /*
        photoList.get(0) returns the object reference of the first Photo object in the list.
        getThumbnailFileName() message is sent to that Photo object to retrieve its
        thumbnail image file name, e.g., 5_thumbnail.jpeg
         */
        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        return Constants.PHOTOS_URI + thumbnailFileName;
    }

    public String publicationDay(Date date) {

        TimeUnit time;
        long diffrence;
        String returnMsg;

        Date systemDate = java.util.Calendar.getInstance().getTime();
        long diff = systemDate.getTime() - date.getTime();
        time = TimeUnit.DAYS;
        diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        returnMsg = "Posted " + diffrence + " days ago";
        if (diffrence == 0)
        {
            returnMsg = "Posted today";

        }
        return returnMsg;
    }

    public int ratingConversion(float rating) {
        int rate = (int) rating;
        return rate;
    }


    /*
     ***************************************
     *   Prepare to Create a New Comment    *
     ***************************************
     */
    public void prepareCreate() {
        /*
        Instantiate a new Comment object and store its object reference into
        instance variable 'selected'. The Comment class is defined in Comment.java
         */
        System.out.println("In Comment Create");
        selected = new Comment();
    }

    /*
     ********************************************
     *   CREATE a New Comment in the Database   *
     ********************************************
     */
    public void create(Blog blog) {
        Methods.preserveMessages();

        if(selected.getRating() == null && selected.getContent().length() == 0)
        {
            Methods.showMessage("Error",
                    "Unable to Post!", "To Post a comment, user must provide either rating or content!");
        }
        else if (userController.isLoggedIn() == false)
        {
            Methods.showMessage("Information",
                    "Unable to Post!", "To Post a comment, a user must have signed in!");
        }
        else {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
            */

            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            selected.setPublicationDate(new Date());
            selected.setBlog(blog);
            selected.setUser(signedInUser);

            persist(PersistAction.CREATE, "Comment Successfully Posted!");

            if (!JsfUtil.isValidationFailed()) {
                // No JSF validation error. The CREATE operation is successfully performed.
                selected = null;        // Remove selection
                listOfBlogComments = null;      // Invalidate listOfBlogComments to trigger re-query.
            }
        }
    }

    /*
     ***********************************************
     *   UPDATE Selected Comment in the Database       *
     ***********************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"Comment was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfBlogComments = null;     // Invalidate listOfBlogComments to trigger re-query.
        }
    }

    /*
     ***********************************************
     *   DELETE Selected Comment in the Database       *
     ***********************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"Comment was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfBlogComments = null;     // Invalidate listOfBlogComments to trigger re-query.
        }
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

                     CommentFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    commentFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     CommentFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    commentFacade.remove(selected);
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
}