package gamestates;

import game.Game;
import game.MenuButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class ControlsSettingsState extends BasicGameState implements KeyListener {
	private final int ID;
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, cancelButton;
	private ArrayList<MenuButton> playerCButtons;
	private TrueTypeFont ttf;
	private TrueTypeFont bigText;

	private final int defualtKeyBinds[] = {Input.KEY_TAB, Input.KEY_D, Input.KEY_Y, Input.KEY_COMMA, Input.KEY_P, Input.KEY_ENTER, Input.KEY_UP, Input.KEY_NUMPAD5, Input.KEY_ENTER};
	private static int keyBinds[] = {Input.KEY_TAB, Input.KEY_D, Input.KEY_Y, Input.KEY_COMMA, Input.KEY_P, Input.KEY_ENTER, Input.KEY_UP, Input.KEY_NUMPAD5, Input.KEY_ENTER};
	private static int keyBindChanges[] = {Input.KEY_TAB, Input.KEY_D, Input.KEY_Y, Input.KEY_COMMA, Input.KEY_P, Input.KEY_ENTER, Input.KEY_UP, Input.KEY_NUMPAD5, Input.KEY_ENTER};

	private int lastPressed;
	private int pressed;
	private int lastId;

	public ControlsSettingsState(int id) {
		ID = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {

		buttons = new ArrayList<MenuButton>();

		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		f = new Font("Arial", Font.BOLD, 14);
		ttf = new TrueTypeFont(f, true);

		playerCButtons = new ArrayList<MenuButton>();
		for(int i = 0 ; i < Game.MAX_PLAYERS; i++) {
			MenuButton tmp = new MenuButton(new Integer(i).toString(), new Rectangle((200 + (i * ((Game.WIDTH - 400) / (Game.MAX_PLAYERS-1)))) - 50, Game.centerHeight - 100, 100, 30), Color.white, Input.getKeyName(keyBinds[i]), ttf);
			playerCButtons.add(tmp);
			buttons.add(tmp);
		}
		
		MenuButton tmp = new MenuButton("8", new Rectangle(Game.centerWidth - 75, Game.centerHeight + 35, 150, 45), Color.white, Input.getKeyName(keyBinds[8]), ttf);
		playerCButtons.add(tmp);
		buttons.add(tmp);
		
		f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		
		okButton = new MenuButton("ok", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Ok", ttf);
		cancelButton = new MenuButton("cancel", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Cancel", ttf);

		buttons.add(okButton);
		buttons.add(cancelButton);

		lastPressed = 9999;
		pressed = 999;
		gc.getInput().addListener(this);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		FontUtils.drawCenter(bigText, "Controls", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		g.setColor(Color.white);
		for(int i = 0 ; i < Game.MAX_PLAYERS; i++) {
			FontUtils.drawCenter(ttf, "Player " + new Integer(i+1).toString(), (200 + (i * ((Game.WIDTH - 400) / (Game.MAX_PLAYERS-1)))) - 50, Game.centerHeight - 150, 100);
		}
		
		FontUtils.drawCenter(ttf, "Main button (multiplayer)", Game.centerWidth - 100, Game.centerHeight, 200);

		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}

		if(lastPressed == pressed) {
			FontUtils.drawCenter(bigText, "Press a key", Game.centerWidth - 200, Game.centerHeight/3 + bigText.getHeight() + 20, 400);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}

		Input input = gc.getInput();

		if(pressed != lastPressed) {
			if (okButton.isMousePressed()) {
				for(int i = 0; i < Game.MAX_PLAYERS; i++) {
					keyBinds[i] = keyBindChanges[i];
				}
				sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
						100));
				((InGameState) sb.getState(Game.State.INGAMESTATE.ordinal())).setControls(keyBinds);
				((ServerMultiplayerState) sb.getState(Game.State.SERVERMULTIPLAYERSTATE.ordinal())).setControls(keyBinds);
				((ClientMultiplayerState) sb.getState(Game.State.CLIENTMULTIPLAYERSTATE.ordinal())).setControls(keyBinds);
			}

			if (input.isKeyPressed(Input.KEY_ESCAPE) || cancelButton.isMousePressed()) {
				sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
						100));
			}

			for(MenuButton pcb : playerCButtons) {
				if(pcb.isMousePressed()) {
					lastPressed = pressed;
					lastId = Integer.parseInt(pcb.getId());
				}
			}
		}
	}
	
	public int[] getKeyBinds() {
		return keyBinds;
	}

	public void keyPressed(int key, char c) {
		if(lastPressed == pressed) {
			if(pressed != Input.KEY_ESCAPE) {
				for(int i = 0; i < keyBindChanges.length; i++) {
					if(keyBindChanges[i] == key && !(lastId == 8)) {
						int j = i;
						j = (j >= 3 ? j+1 : j);
						j = (j > 6 ? j+1 : j);
						keyBindChanges[i] = Input.KEY_NUMPAD7 + j;
						if(playerCButtons.size() <= i)
							break;
						else
							playerCButtons.get(i).setText(Input.getKeyName(keyBindChanges[i]));
					}
				}
				
				keyBindChanges[lastId] = key;
				if(lastId == 8)
					playerCButtons.get(playerCButtons.size() - 1).setText(Input.getKeyName(key));
				else
					playerCButtons.get(lastId).setText(Input.getKeyName(key));
			}
			
			lastPressed = 9999;
			pressed = key;
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}
