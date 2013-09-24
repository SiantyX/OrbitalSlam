package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class MessageBox extends Node {
	private Color txtColor, outlineColor;
	private TrueTypeFont ttf;
	private Rectangle rect;
	private CopyOnWriteArrayList<String> messages;
	
	private final float space = 2.0f;
	private int stringLimit;
	
	public MessageBox(int w, int h, float x, float y, Color outlineColor, Color txtColor, TrueTypeFont ttf) {
		super(x, y);
		this.txtColor = txtColor;
		this.outlineColor = outlineColor;
		this.ttf = ttf;
		rect = new Rectangle(x, y, w, h);
		messages = new CopyOnWriteArrayList<String>();
		stringLimit = (h-5)/(Math.round(ttf.getHeight()+space));
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		rect.setX(pos.x);
		rect.setY(pos.y);
		
		g.setColor(outlineColor);
		g.draw(rect);
		
		g.setFont(ttf);
		g.setColor(txtColor);
		for(int i = 0; (i < messages.size()) && (i < stringLimit); i++) {
			g.drawString(messages.get(i), pos.x + 10, pos.y + rect.getHeight() - (i * (ttf.getHeight() + space)) - ttf.getHeight() - 5);
		}
	}
	
	public void addMessage(String str) {
		messages.add(0, str);
	}
	
	public CopyOnWriteArrayList<String> getMessages() {
		return messages;
	}
}
