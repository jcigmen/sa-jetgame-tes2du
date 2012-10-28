package com.noobs2d.superawesomejetgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

    static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/spritesheet/SPRITESHEET1.pack"));
    static final TextureAtlas entities = new TextureAtlas(Gdx.files.internal("data/ENTITIES.pack"));
    static final Texture explode1 = new Texture(Gdx.files.internal("data/EXPLODE1.png"));
    static final Texture explode2 = new Texture(Gdx.files.internal("data/EXPLODE2.png"));
}
