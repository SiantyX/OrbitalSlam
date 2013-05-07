package game;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import components.Component;
import components.ImageRenderComponent;

public class MenuButton extends Entity{

	private boolean mousePressed;
	private int buttonPressed;
	private Image image;
	
	private float w, h;
	private Vector2f pos;
	
	private Shape shape;
	private Color color;
	private String text;
	private Color txtColor;
	private TrueTypeFont ttf;
	
	public MenuButton(String id, Shape shape, Color color, String text, TrueTypeFont ttf) {
		super(id);
		setPosition(pos);
		mousePressed = false;
		this.shape = shape;
		this.color = color;
		this.text = text;
		this.ttf = ttf;
		w = shape.getWidth();
		h = shape.getHeight();
		this.pos = new Vector2f(shape.getX(), shape.getY());
		this.txtColor = new Color(1 - color.r, 1 - color.g, 1 - color.b);
	}
	
	public MenuButton(String id, Shape shape, Color color, String text, TrueTypeFont ttf, Color txtColor) {
		this(id, shape, color, text, ttf);
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
		this.shape = null;
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		if(shape == null) {
			super.render(gc, sb, g);
		}
		else {
			g.setColor(color);
			g.fill(shape);
			g.setColor(txtColor);
			FontUtils.drawCenter(ttf, text, (int)shape.getX(), (int)shape.getCenterY() - ttf.getHeight()/2, (int)shape.getWidth(), txtColor);
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		for (Component component : getComponents()) {
			component.update(gc, sb, delta);
		}
		
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
}
