/*
 * Created by Anubhav Nanda on 2021.10.29
 * Copyright © 2021 Anubhav Nanda. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Comment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {
    /*
    ---------------------------------------------------------------------------------------------
    The EntityManager is an API that enables database CRUD (Create Read Update Delete) operations
    and complex database searches. An EntityManager instance is created to manage entities
    that are defined by a persistence unit. The @PersistenceContext annotation below associates
    the entityManager instance with the persistence unitName identified below.
    ---------------------------------------------------------------------------------------------
     */
    @PersistenceContext(unitName = "Calmify-PU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public CommentFacade() {
        super(Comment.class);
    }

    public List<Comment> findCommentsByBlogPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return entityManager.createNamedQuery("Comment.findCommentsByBlogId")
                .setParameter("blogId", primaryKey)
                .getResultList();
    }

    public double findRatingByBlogPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return (double) entityManager.createNamedQuery("Comment.findRatingByBlogId")
                .setParameter("blogId", primaryKey)
                .getSingleResult();
    }

    public long findRatingCountByBlogPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return (long) entityManager.createNamedQuery("Comment.findRatingCountByBlogId")
                .setParameter("blogId", primaryKey)
                .getSingleResult();
    }

}
