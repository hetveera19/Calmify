/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Table(name = "Blog", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
@Entity

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "Blog.findAll", query = "SELECT b FROM Blog b")
        , @NamedQuery(name = "Blog.findBlogsByUserId", query = "SELECT b FROM Blog b WHERE b.user.id = :userId")
        , @NamedQuery(name = "Blog.findBlogsByPublished", query = "SELECT b FROM Blog b WHERE b.published = :published")

})

public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    @Column(name = "title", nullable = false)
    @Size(min = 1, max = 128)
    private String title;

    @Lob
    @Column(name = "summary", nullable = false)
    private String summary;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "extension")
    private String extension;

    @Column(name = "published")
    private Boolean published;

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", publicationDate=" + publicationDate +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", extension='" + extension + '\'' +
                ", published=" + published +
                '}';
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}