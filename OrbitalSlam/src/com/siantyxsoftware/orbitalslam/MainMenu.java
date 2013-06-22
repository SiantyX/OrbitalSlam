package com.siantyxsoftware.orbitalslam;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.framework.Graphics;
import com.siantyxsoftware.framework.Screen;
import com.siantyxsoftware.framework.implementation.AndroidGame;
import com.siantyxsoftware.orbitalslam.components.MenuButton;

public class MainMenu extends Screen {
	public static final int ID = 2;
	private ArrayList<MenuButton> buttons;
	private MenuButton playButton, settingsButton;
	
	
	public MainMenu(Game game) {
		super(game);
		
		buttons = new ArrayList<MenuButton>();
		
		Graphics g = game.getGraphics();
		playButton = new MenuButton("play", "Play", g.getCenterWidth() -100, g.getCenterHeight() -70, 200, 50, Color.WHITE);
		buttons.add(playButton);
	
		settingsButton = new MenuButton("settings", "Settings", g.getCenterWidth() -100 , g.getCenterHeight() + 70, 200, 50, Color.WHITE);
		buttons.add(settingsButton);
		
		g.clearScreen(Color.BLACK);
	}

	@Override
	public void update(float deltaTime) {
		Assets.finished = true;
		
		for (MenuButton button : buttons) {
			button.update(game, deltaTime);
		}
		
		if (playButton.isPressed()) {
			Assets.LASTID = ID;
			Assets.finished = true;
			//sb.getState(InGame.ID).init(gc, sb);
			//Game.INGAME_MUSIC.loop();
			//Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			game.setScreen(new InGame(game));
		}
		
		if (settingsButton.isPressed()) {
			Assets.LASTID = ID;
			game.setScreen(new SettingsMenu(game));
		}
	}

	@Override
	public void paint(float deltaTime) {
		for (MenuButton button : buttons) {
			button.render(game, deltaTime);
		}
	}

	@Override
	public void pause() {
		Assets.theme.pause();

	}

	@Override
	public void resume() {
		Assets.theme.play();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		AndroidGame.mAppContext.startActivity(intent);
	}

}
