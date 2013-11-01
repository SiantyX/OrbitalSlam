package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.FontUtils;

public class ViewPort {
	private Vector2f pos;
	private Vector2f res;
	
	public ViewPort(Vector2f res) {
		this.res = res;
		pos = new Vector2f(0, 0);
	}
	
	public void setConstraints(int w, int h) {
		setConstraints(new Vector2f(w, h));
	}
	
	public void setConstraints(Vector2f v) {
		res = v;
	}
	
	//unfinished
	public void setZoom(float factor) {
		res.x /= factor;
		res.y /= factor;
		
		
	}
	
	//unfinished
	public float getZoom() {
		return Game.WIDTH/res.x;
	}
	
	public void setPosition(int x, int y) {
		setPosition(new Vector2f(Math.round(x), Math.round(y)));
	}
	
	public void setPosition(Vector2f v) {
		pos = v;
	}
	
	public void translate(float x, float y) {
		pos.set(pos.x + x, pos.y + y);
	}
	
	public Vector2f toRelative(Vector2f v) {
		Vector2f tmp = new Vector2f();
		tmp.set(v.x*getZoom() - pos.x, v.y*getZoom() - pos.y);
		return tmp;
	}
	
	public Vector2f toAbsolute(Vector2f v) {
		Vector2f tmp = new Vector2f();
		tmp.set(pos.x + v.x/getZoom(), pos.y + v.y/getZoom());
		return tmp;
	}
	
	// ej med zoom ftm
	public void centerOn(Vector2f v) {
		pos.set(v.x - res.x/2, v.y - res.y/2);
	}
	
	public void drawStringCenter(TrueTypeFont font, String text, Vector2f v, int width) {
		v = toRelative(v);
		FontUtils.drawCenter(font, text, Math.round(v.x), Math.round(v.y), width);
	}
	
	public void drawStringCenter(TrueTypeFont font, String text, Vector2f v, int width, Color color) {
		v = toRelative(v);
		FontUtils.drawCenter(font, text, Math.round(v.x), Math.round(v.y), width, color);
	}
	
	public void drawString(Graphics g, TrueTypeFont font, String text, Vector2f v) {
		g.setFont(font);
		v = toRelative(v);
		g.drawString(text, v.x, v.y);
	}
	
	public void drawString(Graphics g, TrueTypeFont font, String text, Vector2f v, Color color) {
		g.setColor(color);
		this.drawString(g, font, text, v);
	}
	
	public void drawLine(Graphics g, Vector2f v1, Vector2f v2, Color color) {
		g.setColor(color);
		this.drawLine(g, v1, v2);
	}
	
	public void drawLine(Graphics g, Vector2f v1, Vector2f v2) {
		Vector2f tmp1 =	toRelative(v1);
		Vector2f tmp2 = toRelative(v2);
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);
	}
	
	public void draw(Graphics g, Shape shape) {
		Vector2f tmp = new Vector2f(shape.getX(), shape.getY());
		shape.setLocation(tmp.x, tmp.y);
		g.draw(shape);
	}
	
	public void fill(Graphics g, Shape shape) {
		Vector2f tmp = new Vector2f(shape.getX(), shape.getY());
		shape.setLocation(tmp.x, tmp.y);
		g.fill(shape);
	}
	
	public float getPosX() {
		return pos.x;
	}
	
	public float getPosY() {
		return pos.y;
	}
	public float getResX() {
		return res.x + pos.x;
	}
	
	public float getResY() {
		return res.y + pos.y;
	}
}
