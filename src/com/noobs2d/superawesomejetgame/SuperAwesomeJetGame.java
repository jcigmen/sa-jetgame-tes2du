package com.noobs2d.superawesomejetgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class SuperAwesomeJetGame extends Game {

    @Override
    public void create() {
	setScreen(new StageScreen(this));
    }

    @Override
    public void render() {
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	super.render();
    }

}
