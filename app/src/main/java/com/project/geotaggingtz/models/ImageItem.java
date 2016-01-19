package com.project.geotaggingtz.models;

public class ImageItem {
    private byte[] blob;

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public ImageItem(byte[] blob) {
            this.blob = blob;
    }

}
