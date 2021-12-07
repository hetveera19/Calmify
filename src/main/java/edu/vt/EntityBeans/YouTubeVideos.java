package edu.vt.EntityBeans;

import java.io.Serializable;

public class YouTubeVideos implements Serializable {
    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public YouTubeVideos(String src) {
        this.src = src;
    }
}

