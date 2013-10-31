package gamestates;

import java.awt.Font;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import game.Game;
import game.MenuButton;

import networking.Lobby;
import networking.NetHandler;

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

public class BrowserState extends BasicGameState implements KeyListener {
	private final int ID;
	
	private ArrayList<MenuButton> buttons;
	private ArrayList<MenuButton> lobbys;
	private MenuButton backButton, refreshButton, createButton, userButton;
	private TrueTypeFont ttf;
	private TrueTypeFont bigText;

	private ArrayList<Lobby> browser;
	private Thread ref;
	
	private boolean changeName;
	private boolean justChanged;
	private Color oldColor;
	
	public BrowserState(int id) {
		ID = id;
	}
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		buttons = new ArrayList<MenuButton>();
		lobbys = new ArrayList<MenuButton>();
		browser = new ArrayList<Lobby>();
		justChanged = false;
		changeName = false;
		
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		f = new Font("Arial", Font.BOLD, 14);
		ttf = new TrueTypeFont(f, true);
		
		backButton = new MenuButton("refresh", new Rectangle(Game.centerWidth - 400, Game.centerHeight + 400, 200, 50), Color.white, "Back", ttf);
		refreshButton = new MenuButton("back", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 400, 200, 50), Color.white, "Refresh", ttf);
		createButton = new MenuButton("create", new Rectangle(Game.centerWidth + 200, Game.centerHeight + 400, 200, 50), Color.white, "Host Game", ttf);
		
		userButton = new MenuButton("user", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 300, 200, 50), Color.black, "Name: " + Game.username, ttf, Color.white);
		oldColor = userButton.getBackColor();

		buttons.add(backButton);
		buttons.add(refreshButton);
		buttons.add(createButton);
		buttons.add(userButton);
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		FontUtils.drawCenter(bigText, "Game Browser", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		for (MenuButton button : lobbys) {
			button.render(gc, sb, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		for (MenuButton button : lobbys) {
			button.update(gc, sb, delta);
		}

		Input input = gc.getInput();
		
		if(justChanged) {
			input.clearKeyPressedRecord();
			justChanged = false;
		}
		
		if(changeName) {
			if(!userButton.getBackColor().equals(new Color(40, 40, 40))) {
				oldColor = userButton.getBackColor();
				userButton.setBackColor(new Color(40, 40, 40));
			}
		}
		
		if (input.isKeyPressed(Input.KEY_ESCAPE) || backButton.isMousePressed()) {
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if(refreshButton.isMousePressed()) {
			try {
				refresh();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(createButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.getState(Game.State.HOSTLOBBYSTATE.ordinal()).init(gc, sb);
			sb.enterState(Game.State.HOSTLOBBYSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		for(MenuButton button : lobbys) {
			if(button.isMousePressed()) {
				//connect to button
				System.out.println("connecting to " + browser.get(Integer.parseInt(button.getId())).getName());
				try {
				NetHandler hndlr = new NetHandler();
				if(hndlr.JoinLobby(browser.get(Integer.parseInt(button.getId()))))
					sb.enterState(Game.State.CLIENTLOBBYSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black, 100));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (userButton.isMousePressed()) {
			changeName = true;
			userButton.setText("");
		}
	}
	
	public void refresh() throws UnknownHostException, IOException {
		final NetHandler h = new NetHandler();
		Runnable r = new Runnable() {
			public void run() {
				browser.clear();
				try {
					h.GetLobbys(browser);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				lobbys.clear();
				for(int i = 0; i < browser.size(); i++) {
					try {
						Lobby tmp = browser.get(i);
						lobbys.add(new MenuButton(String.valueOf(i), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (i*50), 100, 30), Color.gray, tmp.getName() + " " + tmp.getCurPlayers() + "/" + tmp.getMaxPlayers(), ttf, Color.yellow));
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
			}
		};
		ref = new Thread(r);
		ref.start();
	}
	
	public void keyPressed(int key, char c) {
		if(changeName) {
			if(key == Input.KEY_ENTER) {
				changeName = false;
				String oldname = Game.username;
				try {
					Game.username = userButton.getText().length() < 1 ? "Player" : userButton.getText();
				}
				catch(NumberFormatException e) {
					Game.username = oldname;
				}
				userButton.setText("Name: " + Game.username);
			}
			else if(key == Input.KEY_ESCAPE) {
				changeName = false;
				justChanged = true;
				userButton.setText("Name: " + Game.username);
			}
			else if(((int) c >= 32 && (int) c <= 126) || (int) c == 229 || (int) c == 228 || (int) c == 246) {
				userButton.setText(userButton.getText() + c);
			}
		}
	}

	public int getID() {
		return ID;
	}
}
