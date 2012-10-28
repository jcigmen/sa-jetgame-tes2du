package com.noobs2d.superawesomejetgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.noobs2d.scene2d.DynamicSprite;

public class Projectile {

    ArrayList<Projectile> projectiles;
    Camera camera;
    DynamicSprite model;
    Jet jet;

    float speed = 1f;
    float targetX;
    float targetY;
    float totalTraversedY;

    public Projectile(AtlasRegion region, Jet jet, Camera camera, ArrayList<Projectile> projectiles, float targetX, float targetY, float offsetX, float offsetY) {
	this.jet = jet;
	this.camera = camera;
	this.projectiles = projectiles;
	this.targetX = targetX;
	this.targetY = targetY;
	model = new DynamicSprite(region, offsetX, offsetY);
	model.setScale(2f, 2f);
    }

    public Projectile(Jet jet, Camera camera, ArrayList<Projectile> projectiles, float targetX, float targetY, float offsetX, float offsetY) {
	this.jet = jet;
	this.camera = camera;
	this.projectiles = projectiles;
	this.targetX = targetX;
	this.targetY = targetY;
	model = new DynamicSprite(Assets.atlas.findRegion("RAIDEN-PROJECTILE"), offsetX, offsetY);
	model.setWidth(17);
	model.setHeight(48);
    }

    public void render(SpriteBatch batch, float delta) {
	model.draw(batch, 1f);
	model.setX(model.getX() + targetX * delta * speed);
	model.setY(model.getY() + targetY * delta * speed);
	totalTraversedY += targetY * delta * speed;
	if (model.getY() >= camera.position.y + Settings.SCREEN_HEIGHT / 2)
	    projectiles.remove(this);
    }
}
