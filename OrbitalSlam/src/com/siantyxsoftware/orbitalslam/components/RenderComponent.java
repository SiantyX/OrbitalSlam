package com.siantyxsoftware.orbitalslam.components;

import com.siantyxsoftware.framework.Game;

public abstract class RenderComponent extends Component {

	public RenderComponent(String id) {
		this.id = id;
	}

	public abstract void render(Game game, float delta);
}