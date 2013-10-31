package gamestates;

import game.Entity;
import game.Game;
import game.Player;
import game.maps.AnchorMap;

import java.awt.Font;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import networking.Lobby;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public abstract class MultiplayerState extends BasicGameState {
	private final int ID;

	public static CopyOnWriteArrayList<String> names;

	// INGAME SPECIFIC

	private int keyBinds[];
	private AnchorMap map;
	public static ArrayList<Player> players;
	static int numLocalPlayers = 2;
	private static boolean numPlayersChanged = false;

	public static boolean finished = true;

	private TrueTypeFont ttf;
	private TrueTypeFont scoreFont;

	private ArrayList<Player> playersAlive;

	private static double countDown;
	private static boolean onCountDown;

	public MultiplayerState(int id) {
		ID = id;
	}

	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		if(names != null) {
			playersAlive = new ArrayList<Player>();
			map = new AnchorMap();
			players = new ArrayList<Player>();

			//if(names.size() > map.getNumPlayers()) numLocalPlayers = map.getNumPlayers();

			Player.anchorList = map.getEntities();
			// players

			for(int i = 0; i < names.size(); i++) {
				Player p = new Player(i, map);
				if(names.get(i).equals(Game.username))
					p.KEYBIND = keyBinds[i];
				else
					p.KEYBIND = 9999;
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
	}

	private void newRound() throws SlickException {
		ArrayList<Integer> tmpAL = new ArrayList<Integer>();

		for(Player player : players) {
			tmpAL.add(player.getScore());
		}

		// players
		playersAlive.clear();
		players.clear();
		for(int i = 0; i < names.size(); i++) {
			Player p = new Player(i, map);
			if(names.get(i).equals(Game.username))
				p.KEYBIND = keyBinds[i];
			else
				p.KEYBIND = 9999;
			p.setScore(tmpAL.get(i));
			players.add(p);
			playersAlive.add(players.get(i));
		}

		startCountDown();
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		map.render(gc, sb, g);

		if(players.isEmpty()) return;
		for(Player player : players) {
			player.render(gc, sb, g);
		}

		FontUtils.drawCenter(scoreFont, "Score limit: " + Game.SCORE_LIMIT, 10, 10, 200);

		for(int i = 0; i < names.size(); i++) {
			FontUtils.drawCenter(scoreFont, names.get(i) + (i+1) + ": " + players.get(i).getScore(), map.getStartPosX() + i * ((Game.WIDTH - map.getStartPosX()*2) / (map.getNumPlayers()-1)) - Game.WIDTH/14, 40, 100, Player.PLAYER_COLORS[i]);
		}

		if(onCountDown) {
			//FontUtils.drawCenter(scoreFont, "Press F1 - F8 to change number of players", Game.centerWidth - 300, 10, 600);
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

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
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

		deathCheck();

		ArrayList<Player> winners = new ArrayList<Player>();

		if((playersAlive.size() == 1 && numLocalPlayers > 1) || (playersAlive.size() < 1)) {
			for(Player player : players) {
				if(player.getScore() >= Game.SCORE_LIMIT) {
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
				newRound();
			}
		}

		// check for collision
		if(!playersAlive.isEmpty() && playersAlive.size() > 1) {
			for(int i = 0; i < playersAlive.size() - 1; i++) {
				for(int j = i+1; j < playersAlive.size(); j++) {
					if(collisionCircle(playersAlive.get(i).getEntity(), playersAlive.get(j).getEntity())) {
						playersAlive.get(i).collision(playersAlive.get(j));
					}
				}
			}
		}
	}

	private boolean collisionCircle(Entity e1, Entity e2) {
		float radii = e1.getRadius() + e2.getRadius();
		float dx = e2.getPosition().x + e2.getRadius() - e1.getPosition().x - e1.getRadius();
		float dy = e2.getPosition().y + e2.getRadius() - e1.getPosition().y - e1.getRadius();
		if( dx * dx + dy * dy < radii * radii){
			return true;
		}
		return false;
	}

	private void deathCheck() {
		// check if dead
		for(Player player : playersAlive) {
			if(player.getEntity().getCenterPosition().x < 0 || player.getEntity().getCenterPosition().x > Game.WIDTH
					|| player.getEntity().getCenterPosition().y < 0 || player.getEntity().getCenterPosition().y > Game.HEIGHT) {
				player.die();
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
	
	public void setControls(int keyBinds[]) {
		this.keyBinds = keyBinds;
		
		for(int i = 0; i < numLocalPlayers; i++) {
			players.get(i).KEYBIND = keyBinds[i];
		}
	}

	public int getID() {
		return ID;
	}
}
