package game;

import gamestates.AudioSettingsState;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import components.Component;
import components.ImageRenderComponent;

public class MenuButton extends Entity {

	private boolean mousePressed;
	private int buttonPressed;
	private Image image;
	private boolean fill;
	private boolean writecenter;
	
	private float w, h;
	
	private Shape shape;
	private Color color;
	private String text;
	private Color txtColor;
	private TrueTypeFont ttf;
	
	private Sound sound;
	
	public MenuButton(String id, Shape shape, Color color, String text, TrueTypeFont ttf) throws SlickException {
		super(id);
		mousePressed = false;
		this.shape = shape;
		this.color = color;
		this.text = text;
		this.ttf = ttf;
		w = shape.getWidth();
		h = shape.getHeight();
		pos = new Vector2f(shape.getX(), shape.getY());
		this.txtColor = new Color(1 - color.r, 1 - color.g, 1 - color.b);
		sound = new Sound("res/audio/sound/klick.ogg");
		fill = true;
		writecenter = true;
	}
	
	public MenuButton(String id, Shape shape, Color color, String text, TrueTypeFont ttf, Color txtColor) throws SlickException {
		this(id, shape, color, text, ttf);
		this.txtColor = txtColor;
	}

	public MenuButton(String id, Vector2f pos, Image image) throws SlickException {
		super(id);
		setPosition(pos);
		this.image = image;
		w = image.getWidth();
		h = image.getHeight();
		AddComponent(new ImageRenderComponent("button_image", image));
		mousePressed = false;
		this.shape = null;
		sound = new Sound("res/audio/sound/klick.ogg");
		writecenter = true;
		fill = true;
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		if(shape == null) {
			super.render(gc, sb, g);
		}
		else {
			if(shape == null || ttf == null || txtColor == null) return;
			g.setColor(color);
			if(fill)
				g.fill(shape);
			else
				g.draw(shape);
			g.setColor(txtColor);
			try {
				if(writecenter) {
					FontUtils.drawCenter(ttf, text, (int)shape.getX(), (int)shape.getCenterY() - ttf.getHeight()/2, (int)shape.getWidth(), txtColor);
				}
				else {
					g.setFont(ttf);
					g.drawString(text, (int)shape.getX() + 10, (int)shape.getCenterY() - ttf.getHeight()/2);
				}
			}
			catch(NullPointerException e) {
				
			}
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		super.update(gc, sb, delta);
		
		int mousePosX = Mouse.getX();
		int mousePosY = Math.abs(Mouse.getY() - Game.app.getHeight());
		Input input = gc.getInput();
		
		if(shape == null) {
			if ( mousePosX > getPosition().getX() && mousePosX < getPosition().getX() + w
					&& mousePosY > getPosition().getY() && mousePosY < getPosition().getY() + h ) { // cursor is inside button
				if (input.isMousePressed(0)) {
					mousePressed = true;
					input.clearKeyPressedRecord();
					input.clearMousePressedRecord();
					buttonPressed = 0;
				} 
				else if(input.isMousePressed(1)) {
					mousePressed = true;
					input.clearKeyPressedRecord();
					input.clearMousePressedRecord();
					buttonPressed = 1;
				}
				else {
					mousePressed = false;
				}
			}
		}
		else {
			shape.setX(pos.x);
			shape.setY(pos.y);
			if(shape.contains(mousePosX, mousePosY)) {
				if(input.isMousePressed(0)) {
					mousePressed = true;
					input.clearKeyPressedRecord();
					input.clearMousePressedRecord();
					buttonPressed = 0;
				}
				else if(input.isMousePressed(1)) {
					mousePressed = true;
					input.clearKeyPressedRecord();
					input.clearMousePressedRecord();
					buttonPressed = 1;
				}
				else {
					mousePressed = false;
				}
			}
		}
	}

	public boolean isMousePressed() {
		boolean temp = mousePressed;
		mousePressed = false;
		if(temp) {
			sound.play(1, AudioSettingsState.SOUND_LEVEL*AudioSettingsState.MASTER_LEVEL);
		}
		return temp;
	}
	
	public boolean isMousePressed(int button) {
		if(mousePressed && (button == buttonPressed)) {
			return true;
		}
		return false;
	}
	
	public Vector2f getPosition() {
		if(shape == null) {
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
	
	public Color getBackColor() {
		return color;
	}
	
	public void setBackColor(Color color) {
		this.color = color;
	}
	
	public float getWidth() {
		return shape.getWidth();
	}
	
	public float getHeight() {
		return shape.getHeight();
	}
	
	public void fillRect(boolean b) {
		fill = b;
	}

	public void writeCenter(boolean b) {
		writecenter = b;
	}
}
