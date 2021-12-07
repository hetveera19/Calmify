package edu.vt.EntityBeans;

import java.io.Serializable;

public class Podcast implements Serializable {
    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Podcast(String src) {
        this.src = src;
    }
}
