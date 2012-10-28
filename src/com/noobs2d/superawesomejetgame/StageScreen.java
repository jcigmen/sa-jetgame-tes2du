package com.noobs2d.superawesomejetgame;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.noobs2d.scene2d.DynamicScreen;
import com.noobs2d.superawesomejetgame.Jet.JetState;

public class StageScreen extends DynamicScreen {

    ArrayList<Mob> mobs;
    ArrayList<Projectile> mobProjectiles;
    Jet jet;

    TileMapRenderer tileMapRenderer;
    TiledMap tileMap;

    TileAtlas tileAtlas;

    float stateTime;

    public StageScreen(Game game) {
	super(game);
	mobs = new ArrayList<Mob>();
	mobProjectiles = new ArrayList<Projectile>();
	jet = new Jet(stage.getCamera());

	tileMap = TiledLoader.createMap(Gdx.files.internal("data/tilemap/TREE-GRASS.tmx"));
	tileAtlas = new SimpleTileAtlas(tileMap, Gdx.files.internal("data/tilemap/TREE-GRASS"));//TileAtlas(tileMap, Gdx.files.internal("data/tilemap"));

	tileMapRenderer = new TileMapRenderer(tileMap, tileAtlas, 20, 100);
    }

    public Stage getStage() {
	return stage;
    }

    @Override
    public boolean keyDown(int keycode) {
	jet.listener.set(keycode, true);
	return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
	jet.listener.set(keycode, false);
	return super.keyUp(keycode);
    }

    @Override
    public void render(float delta) {
	if (stage.getCamera().position.y >= 1600)
	    System.out.println("FINISHED!");
	else {
	    System.out.println(Gdx.graphics.getFramesPerSecond());
	    //	System.out.println(stage.getCamera().position.x);
	    //	System.out.println(jet.idle.instance.getX());
	    //	System.out.println(tileMapRenderer.getMapWidthUnits() + "\t" + tileMapRenderer.getMapHeightUnits());
	    //	System.out.println(tileMapRenderer.getMapHeightUnits() * delta / 150);
	    //	    System.out.println((int) MathUtils.random(5f));

	    jet.idle.instance.setY(jet.idle.instance.getY() + tileMapRenderer.getMapHeightUnits() * delta / 150);
	    jet.explode.instance.setY(jet.idle.instance.getY() + tileMapRenderer.getMapHeightUnits() * delta / 150);

	    stage.getCamera().position.y += tileMapRenderer.getMapHeightUnits() * delta / 150;
	}

	//	float tileMapX = 0;
	//	float tileMapY = stage.getCamera().position.y;
	//	tileMapRenderer.getProjectionMatrix().set(stage.getCamera().combined);
	//	tileMapRenderer.render(tileMapX, tileMapY, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
	tileMapRenderer.render((OrthographicCamera) stage.getCamera());

	stage.getCamera().update();
	stage.getSpriteBatch().setProjectionMatrix(stage.getCamera().combined);
	stage.getSpriteBatch().begin();
	jet.render(stage.getSpriteBatch(), delta);
	stage.getSpriteBatch().end();

	stage.getSpriteBatch().begin();
	for (int i = 0; i < mobProjectiles.size(); i++)
	    mobProjectiles.get(i).render(stage.getSpriteBatch(), delta);
	stage.getSpriteBatch().end();

	stage.getSpriteBatch().begin();
	for (int i = 0; i < mobs.size(); i++)
	    mobs.get(i).render(stage.getSpriteBatch(), delta);
	stage.getSpriteBatch().end();

	stateTime += delta;
	if (stateTime >= 1) {
	    int type = MathUtils.random(6);
	    Mob mob = null;
	    switch (type) {
		case 0:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("KING"));
		    mob.idle.instance.setName("KING");
		    mobs.add(mob);
		    break;
		case 1:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("CHAS"));
		    mob.idle.instance.setName("CHAS");
		    mobs.add(mob);
		    break;
		case 2:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("CHARLES"));
		    mob.idle.instance.setName("CHARLES");
		    mobs.add(mob);
		    break;
		case 3:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("EMAN"));
		    mob.idle.instance.setName("EMAN");
		    mobs.add(mob);
		    break;
		case 4:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("ARJAY"));
		    mob.idle.instance.setName("ARJAY");
		    mobs.add(mob);
		    break;
		case 5:
		    mob = new Mob(jet, mobs, mobProjectiles, stage.getCamera(), Assets.entities.findRegion("POGS"));
		    mob.idle.instance.setName("POGS");
		    mobs.add(mob);
		    break;
		default:
		    assert false;
		    break;
	    }
	    stateTime = 0;
	}

	for (int i = 0; i < jet.projectiles.size(); i++)
	    for (int j = 0; j < mobs.size(); j++)
		if (mobs.get(j).state != JetState.EXPLODE && jet.projectiles.get(i).model.hit(mobs.get(j).idle.instance) != null) {
		    mobs.get(j).explode();
		    jet.projectiles.remove(i);
		    j = mobs.size();
		}

	for (int i = 0; i < mobProjectiles.size(); i++)
	    if (jet.state != JetState.EXPLODE && mobProjectiles.get(i).model.hit(jet.explode.instance) != null) {
		jet.state = JetState.EXPLODE;
		jet.explode.instance.frameIndex = 0;
		jet.stateTime = 0;
		mobProjectiles.remove(i);
	    }

	for (int i = 0; i < mobs.size(); i++)
	    if (jet.state != JetState.EXPLODE && jet.explode.instance.hit(mobs.get(i).explode.instance) != null) {
		mobs.get(i).state = JetState.EXPLODE;
		jet.state = JetState.EXPLODE;
		jet.explode.instance.frameIndex = 0;
		jet.stateTime = 0;
	    }

    }

    @Override
    public void resize(int width, int height) {
	// TODO Auto-generated method stub
    }

    @Override
    public void show() {
	super.show();
	stage.getCamera().position.x += 100;

    }
}
