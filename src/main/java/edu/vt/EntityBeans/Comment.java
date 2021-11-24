package edu.vt.EntityBeans;

import javax.persistence.*;
import java.util.Date;

@Table(name = "comment")
@Entity

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c")
        , @NamedQuery(name = "Comment.findCommentsByArticleId", query = "SELECT c FROM Comment c WHERE c.articleId = :articleId")
})

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "article__id", nullable = false)
    private Integer articleId;

    @Column(name = "publicationDate", nullable = false)
    private Date publicationDate;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

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

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}