package com.siantyxsoftware.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.siantyxsoftware.framework.Image;
import com.siantyxsoftware.framework.Graphics.ImageFormat;

public class AndroidImage implements Image {
    Bitmap bitmap;
    Bitmap orgbitmap;
    ImageFormat format;
   
    public AndroidImage(Bitmap bitmap, ImageFormat format) {
        this.orgbitmap = bitmap;
    	this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
        orgbitmap.recycle();
    }
    
    @Override
    public float getScale() {
    	return (float)bitmap.getWidth() / (float)orgbitmap.getWidth();
    }
    
    @Override
    public void setScale(float d) {
    	if(d == 1) return;
    	bitmap = Bitmap.createScaledBitmap(orgbitmap, Math.round(d*getWidth()), Math.round(d*getHeight()), d > 1);
    }
}