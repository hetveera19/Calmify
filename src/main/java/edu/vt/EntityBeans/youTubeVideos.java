package edu.vt.EntityBeans;

import java.io.Serializable;

    public class youTubeVideos implements Serializable {
        private String src;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public youTubeVideos(String src) {
            this.src = src;
        }
    }

