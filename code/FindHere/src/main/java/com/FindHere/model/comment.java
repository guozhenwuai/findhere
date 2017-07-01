package com.FindHere.model;

import java.util.Map;

/**
 * Created by msi on 2017/7/1.
 */

public class comment {
    private boolean hasImage;
    private boolean hastText;
    private boolean hasMusic;
    private  String text;
    private Map<Integer,String> images;
    private Map<Integer,String> sounds;

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

}
