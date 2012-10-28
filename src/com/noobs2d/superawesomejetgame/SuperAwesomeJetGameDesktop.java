package com.noobs2d.superawesomejetgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SuperAwesomeJetGameDesktop {

    public static void main(String[] args) {
	new LwjglApplication(new SuperAwesomeJetGame(), Settings.APP_NAME, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, false);
    }

}
