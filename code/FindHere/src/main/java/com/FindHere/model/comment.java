package com.FindHere.model;

import java.util.Map;


public class Comment {
    private String type;
    private int id;
    private int userId;
    private int targetId;
    private int contentId;
    private String text;
    private Map<Integer,String> images;
    private Map<Integer,String> sounds;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public int getUserId(){return userId;}

    public void setUserId(int userId){this.userId = userId;}

    public int getTargetId(){return targetId;}

    public void setTargetId(int targetId){this.targetId = targetId;}

    public int getContentId(){return contentId;}

    public void setContentId(int contentId){this.contentId = contentId;}

    public String getText(){return text;}

    public void setText(String text){this.text = text;}

    public Map<Integer, String> getImages() {
        return images;
    }

    public void setImages(Map<Integer, String> images) {
        this.images = images;
    }

    public Map<Integer, String> getSounds() {
        return sounds;
    }

    public void setSounds(Map<Integer, String> sounds) {
        this.sounds = sounds;
    }

    public boolean hasImage(){return !(images==null);}

    public boolean hasText(){return !(text==null);}

    public boolean hasMusic(){return !(sounds==null);}

}
