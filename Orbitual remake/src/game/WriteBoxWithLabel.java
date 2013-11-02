package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class WriteBoxWithLabel extends Node {
	public WriteBox wb;
	public Label label;
	
	public WriteBoxWithLabel(TrueTypeFont ttf, Vector2f centerPos, boolean isBetween, float writeWidth, String labelText, String boxText, Color bgColor, Color txtColor) throws SlickException {
		super(centerPos);
		
		if(isBetween) {
			float wholeWidth = Math.round(ttf.getWidth(labelText)) + writeWidth;
			
			label = new Label(labelText, ttf, Math.round(centerPos.x - wholeWidth/2), Math.round(centerPos.y), txtColor);
			wb = new WriteBox(boxText, new Rectangle(label.getX() + label.getWidth(), centerPos.y, writeWidth, ttf.getHeight()), bgColor, boxText, ttf, txtColor);
		}
		else {
			label = new Label(labelText, ttf, Math.round(centerPos.x - ttf.getWidth(labelText)), Math.round(centerPos.y), txtColor);
			wb = new WriteBox(boxText, new Rectangle(centerPos.x, centerPos.y, writeWidth, ttf.getHeight()), bgColor, boxText, ttf, txtColor);
		}
		
		setFill(false);
	}
	
	public WriteBoxWithLabel(TrueTypeFont ttf, Vector2f pos, float wholeWidth, String labelText, String boxText, Color bgColor, Color txtColor) throws SlickException {
		super(pos);
		
		float writeWidth = Math.round(wholeWidth - ttf.getWidth(labelText));
		
		label = new Label(labelText, ttf, Math.round(pos.x), Math.round(pos.y), txtColor);
		wb = new WriteBox(boxText, new Rectangle(label.getX() + label.getWidth(), pos.y, writeWidth, ttf.getHeight()), bgColor, boxText, ttf, txtColor);
		
		setFill(false);
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		wb.update(gc, sb, delta);
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		wb.render(gc, sb, g);
		label.render(gc, sb, g);
	}
	
	public void setFill(boolean b) {
		wb.fillRect(b);
	}
}
