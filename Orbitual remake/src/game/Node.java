package game;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.geom.Vector2f;

public abstract class Node {
	protected CopyOnWriteArrayList<Node> children;
	protected Vector2f pos;

	public Node(float x, float y) {
		this(new Vector2f(x, y));
	}

	public Node(Vector2f v) {
		pos = v;
		children = new CopyOnWriteArrayList<Node>();
	}

	public CopyOnWriteArrayList<Node> getChildren() {
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
		synchronized (pos) {
			moveTo(pos.x+dx, pos.y+dy);
		}
	}

	public void moveTo(float x, float y) {
		synchronized (pos) {
			float dx = pos.x - x;
			float dy = pos.y - y;
			synchronized (children) {
				for(Node n : children) {
					n.translate(dx, dy);
				}
				pos.set(x, y);
			}
		}
	}

	public int xInt() {
		synchronized (pos) {
			return Math.round(pos.x);
		}
	}

	public int yInt() {
		synchronized (pos) {
			return Math.round(pos.y);
		}
	}
}
