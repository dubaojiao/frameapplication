package com.du.frameapplication.pojo;

public class BokData {
    private Integer id;
    private String img;
    private Integer newFlag;
    private String title;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(Integer newFlag) {
        this.newFlag = newFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BokData{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", newFlag=" + newFlag +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
