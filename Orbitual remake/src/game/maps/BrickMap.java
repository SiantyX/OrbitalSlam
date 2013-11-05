package game.maps;

import game.Entity;
import game.Game;
import game.ViewPort;
import game.maps.interactables.Brick;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class BrickMap extends GameMap {
	private int numAncPerRow;
	private int numAncPerColumn;

	public BrickMap() {
		super();
		numAnc = 36;

		numAncPerRow = 6;

		numAncPerColumn = numAnc / numAncPerRow;

	}

	@Override
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		return standardStartPosition(i, vp);
	}

	@Override
	public void createMap(ViewPort vp) throws SlickException {

		// Bricks
		Vector2f vector;

		Brick brick = new Brick("Brick1");
		vector = new Vector2f(0, 0);
		vp.toAbsolute(vector);
		brick.setPosition(vector);
		brick.setScaleWidth(10);
		interactables.add(brick);
		
		brick = new Brick("Brick2");
		vector = new Vector2f(0, 0);
		vp.toAbsolute(vector);
		brick.setPosition(vector);
		brick.setScaleHeight(10);
		interactables.add(brick);
		
		brick = new Brick("Brick3");
		vector = new Vector2f(Game.WIDTH, 0);
		vp.toAbsolute(vector);
		brick.setPosition(vector);
		brick.setScaleHeight(10);
		interactables.add(brick);
		
		brick = new Brick("Brick4");
		vector = new Vector2f(0, Game.HEIGHT);
		vp.toAbsolute(vector);
		brick.setPosition(vector);
		brick.setScaleWidth(10);
		interactables.add(brick);

		// Anchors
		anchors.clear();

		for (int i = 0; i < numAnc; i++) {
			Vector2f pos = new Vector2f(
					startPosX
							+ (i % numAncPerRow)
							* (((Game.WIDTH - (2 * startPosX)) / (numAncPerRow - 1))),
					startPosY
							+ (i % numAncPerColumn)
							* (((Game.HEIGHT - (2 * startPosY)) / (numAncPerColumn - 1))));
			addAnchor(i, pos, vp);
		}

		Vector2f tmp = new Vector2f(startPosX, startPosY);
		tmp = vp.toAbsolute(tmp);
		this.startPosY = Math.round(tmp.x);
		this.startPosX = Math.round(tmp.y);

	}

	@Override
	public void mapSpecificChange() throws SlickException {
		// TODO Auto-generated method stub

	}

	public String toString() {
		return "Brick Map";
	}

}
