package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.FontUtils;

public class ViewPort {
	private Vector2f pos;
	private Vector2f res;
	
	public ViewPort() {
		res = new Vector2f(Game.WIDTH, Game.HEIGHT);
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
		
	}
	
	//unfinished
	public float getZoom() {
		return 1;
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
	
	public void drawString(Font font, String text, Vector2f v, int width) {
		v = toRelative(v);
		FontUtils.drawCenter(font, text, Math.round(v.x), Math.round(v.y), width);
	}
	
	public void drawLine(Graphics g, Vector2f v1, Vector2f v2, Color color) {
		g.setColor(color);
	}
	
	public void drawLine(Graphics g, Vector2f v1, Vector2f v2) {
		Vector2f tmp1 =	toRelative(v1);
		Vector2f tmp2 = toRelative(v2);
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);
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
