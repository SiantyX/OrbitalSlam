package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

public class Label extends Node {
	private String text;
	private Font font;
	private TrueTypeFont ttf;
	private Color color;
	
	public Label(String str, Font font, int x, int y, Color color) {
		super(x, y);
		this.font = font;
		this.text = str;
		this.color = color;
		ttf = new TrueTypeFont(font, true);
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g, ViewPort vp) {
		vp.drawStringCenter(ttf, text, pos, ttf.getWidth(text), color);
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		FontUtils.drawCenter(ttf, text, Math.round(pos.x), Math.round(pos.y), ttf.getWidth(text), color);
	}
	
	public void setText(String str) {
		text = str;
	}
	
	public String getText() {
		return text;
	}
	
	public void setFont(Font font) {
		this.font = font;
		ttf = new TrueTypeFont(font, true);
	}
	
	public Font getFont() {
		return font;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public void setX(int x) {
		moveTo(x, pos.y);
	}
	
	public void setY(int y) {
		moveTo(pos.x, y);
	}
	
	public void translate(int plusx, int plusy) {
		translate(plusx, plusy);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
