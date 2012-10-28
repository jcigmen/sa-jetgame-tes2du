package com.noobs2d.superawesomejetgame;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class JetListener extends InputListener {

    Jet jet;

    public boolean[] buttons = new boolean[64];

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;

    public static final int RIGHT = 3;

    public static final int SHOOT = 4;

    public JetListener(Jet jet) {
	this.jet = jet;
    }

    public void set(int key, boolean down) {
	int button = -1;

	if (key == Keys.W)
	    button = UP;
	if (key == Keys.A)
	    button = LEFT;
	if (key == Keys.S)
	    button = DOWN;
	if (key == Keys.D)
	    button = RIGHT;
	if (key == Keys.J)
	    button = SHOOT;

	if (button >= 0)
	    buttons[button] = down;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
	System.out.println("JET DOWN!");
	return super.touchDown(event, x, y, pointer, button);
    }

}
