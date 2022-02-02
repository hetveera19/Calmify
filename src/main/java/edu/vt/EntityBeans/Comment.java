/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "Comment", indexes = {
        @Index(name = "blog_id", columnList = "blog_id"),
        @Index(name = "user_id", columnList = "user_id")
})

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c")
        , @NamedQuery(name = "Comment.findCommentsByBlogId", query = "SELECT c FROM Comment c WHERE c.blog.id = :blogId")
        , @NamedQuery(name = "Comment.findRatingByBlogId", query = "select avg(c.rating) from Comment c WHERE c.blog.id = :blogId")
        , @NamedQuery(name = "Comment.findRatingCountByBlogId", query = "select count(c) from Comment c WHERE c.blog.id = :blogId")
})


@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rating")
    private Float rating;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}