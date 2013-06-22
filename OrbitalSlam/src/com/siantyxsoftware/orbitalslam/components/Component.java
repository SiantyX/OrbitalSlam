package com.siantyxsoftware.orbitalslam.components;

import com.siantyxsoftware.framework.Game;

public abstract class Component {

	protected String id;
	protected Entity owner;

	public String getID() {
		return id;
	}

	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}

	public abstract void update(Game game, float delta);
}
