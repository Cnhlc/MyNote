package com.wuwl.mynote.bean;

import java.io.Serializable;

public class Note implements Serializable {
    private int nId;
    private String nContent;
    private String nTime;

    public Note() {
    }

    public Note(int nId, String nContent, String nText) {
        this.nId = nId;
        this.nContent = nContent;
        this.nTime = nText;
    }

    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    public String getnContent() {
        return nContent;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent;
    }

    public String getnTime() {
        return nTime;
    }

    public void setnTime(String nTime) {
        this.nTime = nTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "nId=" + nId +
                ", nContent='" + nContent + '\'' +
                ", nText='" + nTime + '\'' +
                '}';
    }


}
