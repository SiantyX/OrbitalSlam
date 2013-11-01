package gamestates;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.AnchorMap;
import game.maps.GameMap;
import game.maps.RandomFunkyMap;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class InGameState extends BasicGameState implements KeyListener {
	private final int ID;

	private int keyBinds[];
	private GameMap map;
	public static ArrayList<Player> players;
	static int numLocalPlayers = 2;
	private static boolean numPlayersChanged = false;

	public static boolean finished = true;

	private TrueTypeFont ttf;
	private TrueTypeFont scoreFont;

	private ArrayList<Player> playersAlive;

	private static double countDown;
	private static boolean onCountDown;
	
	private int scoreLimit;

	private Image bg;
	
	private ViewPort vp;

	public InGameState(int id) {
		ID = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		if(!finished) {
			reInit(gc, sb);
			return;
		}

		bg = new Image("res/orbitalbg1.jpg");
		
		vp = new ViewPort(new Vector2f(Game.WIDTH, Game.HEIGHT));

		playersAlive = new ArrayList<Player>();
		map = ((BeforeGameState)sb.getState(Game.State.BEFOREGAMESTATE.ordinal())).getMap();

		players = new ArrayList<Player>();

		if(numLocalPlayers > map.getNumPlayers()) numLocalPlayers = map.getNumPlayers();

		Player.anchorList = map.getAnchors();
		// players
		for(int i = 0; i < numLocalPlayers; i++) {
			Player p = new Player(i, map);
			p.KEYBIND = keyBinds[i];
			players.add(p);
			playersAlive.add(players.get(i));
		}

		// font for winner
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);

		scoreFont = new TrueTypeFont(new Font("Arial", Font.BOLD, 18), true);

		finished = false;

		DisplayModeState.OLD_WIDTH = Game.WIDTH;
		DisplayModeState.OLD_HEIGHT = Game.HEIGHT;

		countDown = 3 * 1000;
		onCountDown = true;
	}

	/**
	 * Runs if the game is initiated when not finished.
	 * Happens if you change resolution.
	 * 
	 * @param gc
	 * @param sb
	 * @throws SlickException
	 */
	private void reInit(GameContainer gc, StateBasedGame sb) throws SlickException {
		for(Player player : players) {
			Entity e = player.getEntity();
			Vector2f v = new Vector2f(e.getPosition().x/DisplayModeState.OLD_WIDTH * Game.WIDTH, e.getPosition().y/DisplayModeState.OLD_HEIGHT * Game.HEIGHT);
			e.setPosition(v);
			e.setScale(Player.stdScale*Game.WIDTH);
		}

		ArrayList<Entity> anchors = map.getAnchors();
		for(Entity e : anchors) {
			Vector2f v = new Vector2f(e.getPosition().x/DisplayModeState.OLD_WIDTH * Game.WIDTH, e.getPosition().y/DisplayModeState.OLD_HEIGHT * Game.HEIGHT);
			e.setPosition(v);
		}
	}

	private void newRound() throws SlickException {
		ArrayList<Integer> tmpAL = new ArrayList<Integer>();

		for(Player player : players) {
			tmpAL.add(player.getScore());
		}

		// players
		playersAlive.clear();
		players.clear();
		for(int i = 0; i < numLocalPlayers; i++) {
			Player p = new Player(i, map);
			p.setScore(tmpAL.get(i));
			p.KEYBIND = keyBinds[i];
			players.add(p);
			playersAlive.add(players.get(i));
		}

		startCountDown();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		bg.draw(0, 0, (float) Game.WIDTH/2560);

		map.render(gc, sb, g, vp);

		if(players.isEmpty()) return;
		for(Player player : players) {
			player.render(gc, sb, g, vp);
		}

		FontUtils.drawCenter(scoreFont, "Score limit: " + scoreLimit, 10, 10, 200);

		for(int i = 0; i < numLocalPlayers; i++) {
			FontUtils.drawCenter(scoreFont, "Player " + (i+1) + ": " + players.get(i).getScore(),map.getScorePlacementX(i), map.getScorePlacementY(), 100, Player.PLAYER_COLORS[i]);
		}

		if(onCountDown) {
			FontUtils.drawCenter(scoreFont, "Press F1 - F8 to change number of players", Game.centerWidth - 300, 10, 600);
			FontUtils.drawCenter(ttf, new Integer((((int)countDown/1000) + 1) == 4 ? 3 : (((int)countDown/1000) + 1)).toString(), Game.centerWidth, Game.centerHeight - 100, 20);
		}

		if(finished) {
			g.setColor(new Color(0, 0, 0, 125));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setColor(Color.white);
			if(sb.getCurrentStateID() != Game.State.AFTERGAMESTATE.ordinal()) {
				if(playersAlive.size() < 1) {
					FontUtils.drawCenter(ttf, "It's a Draw!", Game.centerWidth - 200, Game.centerHeight - 25, 400);
				}
				else {
					FontUtils.drawCenter(ttf, playersAlive.get(0).toString() + " Wins!", Game.centerWidth - 200, Game.centerHeight - 25, 400, Player.PLAYER_COLORS[players.indexOf(playersAlive.get(0))]);
				}
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		if(finished)
			return;
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			Game.LASTID = getID();
			finished = false;
			Game.MENU_MUSIC.loop();
			Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(Game.State.PAUSEMENUSTATE.ordinal());
		}

		if(numPlayersChanged) {
			finished = true;
			numPlayersChanged = false;
			init(gc, sb);
		}

		// 3 sec countdown stop update
		if(onCountDown) {
			countDown -= delta;
			if(countDown <= 0) {
				onCountDown = false;
			}
			input.clearKeyPressedRecord();
			return;
		}

		if(players.isEmpty()) return;
		
		if(!playersAlive.isEmpty()) {
			ArrayList<Player> tmpPlayers = new ArrayList<Player>();
			for(Player player : playersAlive) {
				tmpPlayers.add(player);
			}
			for(Player player : tmpPlayers) {
				if(player.isDead()) {
					playersAlive.remove(player);
				}
				else {
					player.update(gc, sb, delta);
				}
			}
		}
		map.update(gc, sb, delta);
		deathCheck();

		ArrayList<Player> winners = new ArrayList<Player>();

		if((playersAlive.size() == 1 && numLocalPlayers > 1) || (playersAlive.size() < 1)) {
			for(Player player : players) {
				if(player.getScore() >= scoreLimit) {
					winners.add(player);
				}
			}

			if(!winners.isEmpty()) {
				Player bestScore = winners.get(0);
				for(Player p : winners) {
					if(p.getScore() > bestScore.getScore()) {
						bestScore = p;
					}
				}
				// player wins
				// playersAlive.get(0)
				Game.LASTID = getID();
				finished = true;
				sb.getState(Game.State.AFTERGAMESTATE.ordinal()).init(gc, sb);
				sb.enterState(Game.State.AFTERGAMESTATE.ordinal(), new FadeOutTransition(Color.black, 2000), new FadeInTransition(Color.black,
						2000));
			}
			else {
				map.reset();
				newRound();
			}
		}

		// check for collision
		if(!playersAlive.isEmpty() && playersAlive.size() > 1) {
			for(int i = 0; i < playersAlive.size() - 1; i++) {
				for(int j = i+1; j < playersAlive.size(); j++) {
					if(playersAlive.get(i).getEntity().collisionCircle(playersAlive.get(j).getEntity())) {
						playersAlive.get(i).collision(playersAlive.get(j));
					}
				}
			}
		}
	}


	private void deathCheck() {
		// check if dead
		for(Player player : playersAlive) {
			if(player.deathCheck()) {
				for(Player otherPlayer : playersAlive) {
					if(otherPlayer.equals(player)) continue;
					otherPlayer.addScore(1);
				}
				// SCREEN FLASH HERE
			}
		}
	}

	public static void startCountDown() {
		countDown = 3000;
		onCountDown = true;
	}
	
	public ArrayList<Player> getPlayers(){
		return playersAlive;
	}

	public void keyPressed(int key, char c) {
		if(key >= Input.KEY_F1 && key <= Input.KEY_F8) {
			numLocalPlayers = key - Input.KEY_F1 + 1;
			numPlayersChanged = true;
		}
	}
	
	public void setControls(int keyBinds[]) {
		for(int i = 0; i < numLocalPlayers; i++) {
			players.get(i).KEYBIND = keyBinds[i];
		}
	}
	
	public void setKeyBinds(int keyBinds[]) {
		this.keyBinds = keyBinds;
	}
	
	public void setScoreLimit(int score) {
		scoreLimit = score;
	}
	
	public void setNumPlayers(int num) {
		numLocalPlayers = num;
	}
	
	@Override
	public int getID() {
		return ID;
	}
}
