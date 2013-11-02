package gamestates;

import game.Game;
import game.MenuButton;
import game.Player;

import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import networking.Hosting;
import networking.LobbyHosting;
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

public class HostLobbyState extends LobbyState implements KeyListener {
	public HostLobbyState(int id) {
		super(id);
	}
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
		
		startButton = new MenuButton("start", new Rectangle(Game.centerWidth + 100, Game.centerHeight + 400, 200, 50), Color.white, "Start", ttf);
		buttons.add(startButton);
		
		if(Game.LASTID == Game.State.BROWSERSTATE.ordinal()) {
			try {
				hosted = new LobbyHosting(hostname, 4, mbox);
				hosted.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);
		
		users.clear();
		players = hosted.getPlayers();
		
		if(startButton.isMousePressed()) {
			hosted.setAllKeys("start");
			sb.getState(Game.State.SERVERMULTIPLAYERSTATE.ordinal()).init(gc, sb);
			sb.enterState(Game.State.SERVERMULTIPLAYERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if(cancelButton.isMousePressed()) {
			hosted.close();
			sb.enterState(Game.State.BROWSERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		mbox = hosted.getBox();
		users.add(new MenuButton(players.get(0), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200, 100, 30), Color.black,
				"(Host) " + Game.username, ttf, Color.yellow));
		
		for(int i = 1; i < players.size(); i++) {
			users.add(new MenuButton(players.get(i), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (i*50), 100, 30), Color.black,
					players.get(i).split("\\@")[0].length() < 1 ? "Unknown" : players.get(i).split("\\@")[0], ttf, Color.yellow));
		}
	}
	
	public void sendText(String str) {
		hosted.addToBox(Game.username + ": " + str);
		hosted.setAllKeys("chat\n" + Game.username + ": " + str);
	}
}
/*	private String hostname;
	private ArrayList<MenuButton> buttons;
	private MenuButton startButton, cancelButton, textButton;
	private boolean justChanged;
	private boolean textChange;
	private TrueTypeFont ttf, bigText;
	//private ArrayList<Player> players;
	private ArrayList<MenuButton> users;
	private NetHandler hndlr;
	private int alpha;
	private LobbyHosting hosted;
	private CopyOnWriteArrayList<String> players;

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		hostname = NetHandler.getHostName();

		buttons = new ArrayList<MenuButton>();
		//players = new ArrayList<Player>();
		users = new ArrayList<MenuButton>();
		players = new CopyOnWriteArrayList<String>();

		textChange = false;
		justChanged = false;

		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);

		f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		startButton = new MenuButton("start", new Rectangle(Game.centerWidth + 100, Game.centerHeight + 400, 200, 50), Color.white, "Start", ttf);
		cancelButton = new MenuButton("cancel", new Rectangle(Game.centerWidth - 300, Game.centerHeight + 400, 200, 50), Color.white, "Cancel", ttf);
		textButton = new MenuButton("text", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 300, 200, 50), Color.black, "|...", ttf);

		buttons.add(startButton);
		buttons.add(cancelButton);
		buttons.add(textButton);


		//--------------------------
		if(Game.LASTID == BrowserState.ID) {
			try {
				hosted = new LobbyHosting(hostname, 4);
				hosted.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		FontUtils.drawCenter(bigText, hostname, Game.centerWidth - 300, Game.centerHeight/6, 600);

		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		for (MenuButton button : users) {
			button.render(gc, sb, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		for (MenuButton button : users) {
			button.update(gc, sb, delta);
		}

		Input input = gc.getInput();

		if(cancelButton.isMousePressed()) {
			hosted.close();
			sb.enterState(BrowserState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if(startButton.isMousePressed()) {
			sb.enterState(MultiplayerState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if(textButton.isMousePressed()) {
			textChange = true;
			textButton.setText("");
		}
		
		users.clear();
		players = hosted.getPlayers();
		for(String player : players) {
			for(MenuButton user : users) {
				if(player.equals(user.getId())) {
					break;
				}
				users.add(new MenuButton(player, new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (users.size()*50), 100, 30), Color.black, player, ttf, Color.yellow));
			}
		}

		// note: kom ihåg att closa client när gejmet startas
	}

	public void keyPressed(int key, char c) {
		if(textChange) {
			if(key == Input.KEY_ENTER) {
				textChange = false;
				sendText(textButton.getText());
				textButton.setText("|...");
			}
			else if(key == Input.KEY_ESCAPE) {
				textChange = false;
				justChanged = true;
				textButton.setText("|...");
			}
			else if(((int) c >= 32 && (int) c <= 126) || (int) c == 229 || (int) c == 228 || (int) c == 246) {
				textButton.setText(textButton.getText() + c);
			}
		}
	}

	public void sendText(String str) {

	}

	public int getID() {
		return ID;
	}
*/
