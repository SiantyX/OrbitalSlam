package com.siantyxsoftware.orbitalslam.components;

public class Vector2f {
	public float x, y;
	
	public Vector2f() {
		
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f normalise() {
		// TODO Auto-generated method stub
		return null;
	}

	public float dot(Vector2f v) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float length() {
		return (float) Math.hypot(x, y);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
}
