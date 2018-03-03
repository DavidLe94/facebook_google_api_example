package com.example.asus.androidadvance_assignment;

/**
 * Created by admin on 12/16/2015.
 */
public class RSSItem {
    String title;
    String imageLink;
    String postDate;
    String link;
    String description;

    public RSSItem(String title, String imageLink, String postDate, String link, String description) {
        this.title = title;
        this.imageLink = imageLink;
        this.postDate = postDate;
        this.link = link;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
