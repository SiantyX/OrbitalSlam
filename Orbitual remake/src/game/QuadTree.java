package game;

import game.maps.interactables.Interactable;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;


// http://gamedev.tutsplus.com/tutorials/implementation/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space/
public class QuadTree {
	private final int MAX_OBJECTS = 10;
	private final int MAX_LEVELS = 5;

	private int level;
	private ArrayList<Interactable> inters;
	private Rectangle bounds;
	private QuadTree[] nodes;

	public QuadTree(int pLevel, Rectangle rect) {
		level = pLevel;
		inters = new ArrayList<Interactable>();
		bounds = rect;
		nodes = new QuadTree[4];
	}

	public void clear() {
		inters.clear();

		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	private void split() {
		int subWidth = Math.round(bounds.getWidth() / 2);
		int subHeight = Math.round(bounds.getHeight() / 2);
		int x = Math.round(bounds.getX());
		int y = Math.round(bounds.getY());

		nodes[0] = new QuadTree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new QuadTree(level+1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new QuadTree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new QuadTree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	private int getIndex(Interactable inter) {
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (inter.getPosition().getY() < horizontalMidpoint && inter.getPosition().getY() + inter.getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (inter.getPosition().getY() > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if (inter.getPosition().getX() < verticalMidpoint && inter.getPosition().getX() + inter.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			}
			else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (inter.getPosition().getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			}
			else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	public void insert(Interactable inter) {
		if (nodes[0] != null) {
			int index = getIndex(inter);

			if (index != -1) {
				nodes[index].insert(inter);

				return;
			}
		}

		inters.add(inter);

		if (inters.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < inters.size()) {
				int index = getIndex(inters.get(i));
				if (index != -1) {
					nodes[index].insert(inters.remove(i));
				}
				else {
					i++;
				}
			}
		}
	}

	public ArrayList<Interactable> retrieve(ArrayList<Interactable> returnObjects, Interactable inter) {
		int index = getIndex(inter);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, inter);
		}

		for(Interactable i : inters) {
			if(i.equals(inter)) continue;
			returnObjects.add(i);
		}

		return returnObjects;
	}
}
