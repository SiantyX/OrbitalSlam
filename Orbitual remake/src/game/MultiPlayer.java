package game;

import game.maps.GameMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MultiPlayer extends Player {
	private boolean wantsToHook;

	public MultiPlayer(int num, GameMap map) throws SlickException {
		super(num, map);
	}

	public void hook(Entity e) {
		hookedTo = e;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, ViewPort vp) {
		// check if dead
		if (dead)
			return;

		setAllRotation(dDegrees);

		if (stunTime > 0) {
			changeImageOnNotEqual(playerImg[num] + "xd", stunnedImage);
		} 
		else {
			stunTime = 0;
			changeImageOnNotEqual(playerImg[num], defaultImage);

			// hook button
			Input input = gc.getInput();
			if (input.isKeyPressed(KEYBIND) && !wasKeyDown) {
				wasKeyDown = true;
				wantsToHook = true;
			}
			if (!input.isKeyDown(KEYBIND)) {
				wasKeyDown = false;
			}
		}

		updateEntity(gc, sb, delta, vp);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g,
			ViewPort vp) {
		super.render(gc, sb, g, vp);
	}

	public void setHooked(boolean b) {
		hooked = b;
	}

	public boolean getWantsToHook() {
		boolean b = wantsToHook;
		wantsToHook = false;
		return b;
	}
}
