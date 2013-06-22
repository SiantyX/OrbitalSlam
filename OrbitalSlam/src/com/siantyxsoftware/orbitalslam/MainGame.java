package com.siantyxsoftware.orbitalslam;

import com.siantyxsoftware.framework.Screen;
import com.siantyxsoftware.framework.implementation.AndroidGame;

public class MainGame extends AndroidGame {
	@Override
	public Screen getInitScreen() {
		return new Loading(this);
	}
	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}
}
