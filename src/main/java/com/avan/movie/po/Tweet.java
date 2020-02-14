package com.avan.movie.po;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_tweet")

public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private String poster;
    private String type;
    private String description;
    private Boolean published;

    @ManyToOne
    private Admin admin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;


    public Tweet() {
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
