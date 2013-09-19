package com.siantyxsoftware.orbitalslam.components;

import java.util.List;

import android.graphics.Color;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.framework.Graphics;
import com.siantyxsoftware.framework.Image;
import com.siantyxsoftware.framework.Input.TouchEvent;
import com.siantyxsoftware.orbitalslam.Assets;

public class MenuButton extends Entity{

	private boolean mousePressed;
	private Image image;
	
	private float w, h;
	private Vector2f pos;
	private Vector2f txtPos;
	
	private int color;
	private String text;
	private int txtColor;
	private int txtSize;
	//private TrueTypeFont ttf;
	
	public MenuButton(String id, String text, float x, float y, float w, float h, int color) {
		super(id);
		setPosition(pos);
		mousePressed = false;
		this.color = color;
		this.text = text;
		image = null;
		//this.ttf = ttf;
		this.w = w;
		this.h = h;
		this.pos = new Vector2f(x, y);
		txtSize = 14;
		Assets.mainPaint.setTextSize(txtSize);
		this.txtPos = new Vector2f(x + w/2 - Assets.mainPaint.measureText(text)/2, y + (h/2));
		this.txtColor = Color.rgb(255-Color.red(color), 255-Color.green(color), 255-Color.blue(color));
	}
	
	public MenuButton(String id, String text, float x, float y, float w, float h, int color, int txtColor) {
		this(id, text, x, y, w, h, color);
		this.txtColor = txtColor;
	}

	public MenuButton(String id, Vector2f pos, Image image) {
		super(id);
		setPosition(pos);
		this.image = image;
		w = image.getWidth();
		h = image.getHeight();
		AddComponent(new ImageRenderComponent("button_image", image));
		mousePressed = false;
	}

	public void render(Game game, float delta) {
		if(image != null) {
			super.render(game, delta);
		}
		else {
			Graphics g = game.getGraphics();
			g.drawRect(Math.round(pos.x), Math.round(pos.y), Math.round(w), Math.round(h), color);
			Assets.mainPaint.setColor(txtColor);
			Assets.mainPaint.setTextSize(14);
			g.drawString(text, Math.round(txtPos.x), Math.round(txtPos.y + txtSize/2), Assets.mainPaint);
			//FontUtils.drawCenter(ttf, text, (int)shape.getX(), (int)shape.getCenterY() - ttf.getHeight()/2, (int)shape.getWidth(), txtColor);
		}
	}
	
	public void update(Game game, float delta) {
		for (Component component : getComponents()) {
			component.update(game, delta);
		}
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		for(TouchEvent ev : touchEvents) {
			if(ev.type == TouchEvent.TOUCH_UP) {
				if(ev.x > getPosition().x && ev.x < getPosition().x + w
						&& ev.y > getPosition().y && ev.y < getPosition().getY() + h) {
					mousePressed = true;
				}
			}
		}
	}

	public boolean isPressed() {
		boolean temp = mousePressed;
		mousePressed = false;
		if(temp) {
			Assets.click.play(50);
		}
		return temp;
	}
	
	/*public boolean isMultiPressed(int button) {
		if(mousePressed && (button == buttonPressed)) {
			return true;
		}
		return false;
	}*/
	
	public Vector2f getPosition() {
		if(image != null) {
			return super.getPosition();
		}
		return pos;
	}
	
	public Image getImage() {
		return image;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getBackColor() {
		return color;
	}
	
	public void setBackColor(int color) {
		this.color = color;
	}
	
	public float getWidth() {
		return w;
	}
	
	public float getHeight() {
		return h;
	}
}
