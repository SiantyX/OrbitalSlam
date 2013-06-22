package com.siantyxsoftware.orbitalslam;

import android.graphics.Color;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.framework.Graphics;
import com.siantyxsoftware.framework.Screen;
import com.siantyxsoftware.orbitalslam.components.AnchorMap;
import com.siantyxsoftware.orbitalslam.components.Entity;
import com.siantyxsoftware.orbitalslam.components.Player;

public class InGame extends Screen {
	public static final int ID = 1;
	private AnchorMap map;
	public static Player player;
	
	private static double countDown;
	private static boolean onCountDown;
	
	private float test;
	
	public InGame(Game game) {
		super(game);
		game.getGraphics().clearScreen(Color.BLACK);
		
		test = 0;
		/*if(!Assets.finished) {
			reInit(game);
			return;
		}*/

		map = new AnchorMap(game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		Player.anchorList = map.getEntities();
		// players
			
		player = new Player(0, map);

		Assets.finished = false;

		countDown = 3 * 100;
		onCountDown = true;

	}

	@Override
	public void update(float deltaTime) {
		if(Assets.finished) return;
		
		/*
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			Game.LASTID = getID();
			finished = false;
			Game.MENU_MUSIC.loop();
			Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(PauseMenuState.ID);
		}*/
		
		// 3 sec countdown stop update
		if(onCountDown) {
			countDown -= deltaTime;
			if(countDown <= 0) {
				onCountDown = false;
			}
			return;
		}

		player.update(game, deltaTime);		
		
		deathCheck();
		
		if(player.isDead()) {
			Assets.LASTID = ID;
			Assets.finished = true;
			game.setScreen(new MainMenu(game));
		}

		// check for collision
		/*if(!playersAlive.isEmpty() && playersAlive.size() > 1) {
			for(int i = 0; i < playersAlive.size() - 1; i++) {
				for(int j = i+1; j < playersAlive.size(); j++) {
					if(collisionCircle(playersAlive.get(i).getEntity(), playersAlive.get(j).getEntity())) {
						playersAlive.get(i).collision(playersAlive.get(j));
					}
				}
			}
		}*/
	}

	@Override
	public void paint(float deltaTime) {
		if(player.isDead()) return;
		Graphics g = game.getGraphics();
		g.clearScreen(Color.BLACK);
		
		map.render(game, deltaTime);

		
		player.render(game, deltaTime);
		
		if(onCountDown) {
			Assets.mainPaint.setColor(Color.WHITE);
			Assets.mainPaint.setTextSize(50);
			g.drawCenterString(Integer.valueOf((((int)countDown/100) + 1) == 4 ? 3 : (((int)countDown/100) + 1)).toString(), g.getCenterWidth(), g.getCenterHeight() - 100, Assets.mainPaint);
		}
		
		if(Assets.finished) {
			g.clearScreen(Color.BLACK);
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub
		game.setScreen(new MainMenu(game));
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
		Graphics g = game.getGraphics();
		if(player.getEntity().getCenterPosition().x < 0 || player.getEntity().getCenterPosition().x > g.getWidth()
				|| player.getEntity().getCenterPosition().y < 0 || player.getEntity().getCenterPosition().y > g.getHeight()) {
			player.die();
		}
	}
}
