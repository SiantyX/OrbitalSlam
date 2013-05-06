package gamestates;

import game.Game;
import game.MenuButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class DisplayModeState extends BasicGameState implements Comparator{
	public static final int ID = 3;
	private LinkedList<DisplayMode> resolutions;
	private String DisplayMode;
	private int index;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		
		try {
			DisplayMode[] temp = Display.getAvailableDisplayModes();
			resolutions = new LinkedList<DisplayMode>();
			for (int i=0; i < temp.length; i ++) {
				resolutions.add(temp[i]);
			}
			
			for (int i=0; i < resolutions.size(); i ++) {
				if (resolutions.get(i).toString().contains("x 16") || !(resolutions.get(i).toString().contains("@60"))) {
					resolutions.remove(i);
					i--;
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(resolutions, this);
		
		index = 0;
		DisplayMode = resolutions.get(index).toString();
		for(int i = 0; i < resolutions.size(); i++) {
			if(resolutions.get(i).toString().contains(new Integer(Game.WIDTH).toString()) && resolutions.get(i).toString().contains(new Integer(Game.HEIGHT).toString())) {
				DisplayMode = resolutions.get(i).toString();
				index = i;
			}
		}
		
		for (int i=0; i < resolutions.size(); i ++) {
			System.out.println(resolutions.get(i).toString());
		}
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		g.drawString(DisplayMode, Game.centerWidth-100, Game.centerHeight );
	
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		
		Input input = gc.getInput();
		
		if (input.isMousePressed(1)) {
			if (!(index-1 < 0)) {
				DisplayMode = resolutions.get(index-1).toString();
				index--;
			}
		}
		
		if (input.isMousePressed(0)) {
			if (!(index+1 >= resolutions.size())) {
				DisplayMode = resolutions.get(index+1).toString();
				index++;
			}
			
		}
		
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			Game.app.setDisplayMode(resolutions.get(index).getWidth(), resolutions.get(index).getHeight(), Game.fullscreen);
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			sb.enterState(Game.LASTID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
			Game.LASTID = getID();
			sb.closeRequested();
		}

	}
	
	// endast för displaymodes
	public int compare(Object o1, Object o2) {
		DisplayMode e1 = (DisplayMode) o1;
		DisplayMode e2 = (DisplayMode) o2;
		
		if(e1.getWidth() < e2.getWidth()) {
			return -1;
		}
		else if(e1.getWidth() == e2.getWidth()) {
			if(e1.getHeight() < e2.getHeight()) {
				return -1;
			}
			else if(e1.getHeight() == e2.getHeight()) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else {
			return 1;
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}
