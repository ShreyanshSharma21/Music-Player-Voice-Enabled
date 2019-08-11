package com.music.voice_enabled.musicplayervoiceenabled.model;

import java.io.Serializable;

public class AudioModel implements Serializable {
    private String name;
    private Integer path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPath() {
        return path;
    }

    public void setPath(Integer path) {
        this.path = path;
    }

    public AudioModel(String name, Integer path) {
        this.name = name;
        this.path = path;
    }
}

