package game;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

public abstract class Node {
	protected ArrayList<Node> children;
	protected Vector2f pos;
	
	public Node(float x, float y) {
		pos = new Vector2f(x, y);
		children = new ArrayList<Node>();
	}
	
	public Node(Vector2f v) {
		pos = v;
		children = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void addChild(Node node) {
		children.add(node);
	}
	
	public void removeChild(Node node) {
		children.remove(node);
	}
	
	public void removeAll() {
		children.clear();
	}
	
	public void translate(float dx, float dy) {
		moveTo(pos.x+dx, pos.y+dy);
	}
	
	public void moveTo(float x, float y) {
		float dx = pos.x - x;
		float dy = pos.y - y;
		for(Node n : children) {
			n.translate(dx, dy);
		}
		pos.set(x, y);
	}
	
	public int xInt() {
		return Math.round(pos.x);
	}
	
	public int yInt() {
		return Math.round(pos.y);
	}
}
