package com.siantyxsoftware.framework;

import com.siantyxsoftware.framework.Graphics.ImageFormat;

public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
	public float getScale();
	public void setScale(float f);
}