package com.noobs2d.superawesomejetgame;

import java.util.ArrayList;

import aurelienribon.tweenengine.equations.Bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.noobs2d.scene2d.DynamicAnimation;
import com.noobs2d.tween.TweenableObject;

public class Mob extends Jet {

    ArrayList<Mob> mobs;
    ArrayList<Projectile> mobProjectiles;
    ParticleEffect effect;
    Jet jet;
    boolean runToJet;

    public Mob(Jet jet, ArrayList<Mob> mobs, ArrayList<Projectile> mobProjectiles, Camera camera, AtlasRegion region) {
	super(camera);
	this.jet = jet;
	this.mobs = mobs;
	this.mobProjectiles = mobProjectiles;
	this.camera = camera;
	projectiles = new ArrayList<Projectile>();

	effect = new ParticleEffect();
	effect.load(Gdx.files.internal("data/TRIANGLE"), Gdx.files.internal("data"));

	listener = new JetListener(this);

	idle = new TweenableObject<DynamicAnimation>(new DynamicAnimation(0.2f, region));
	idle.instance.setX(64 + MathUtils.random(700));
	idle.instance.setY(camera.position.y + 500 + MathUtils.random(300));
	idle.instance.addListener(listener);

	explode = new TweenableObject<DynamicAnimation>(new DynamicAnimation(Assets.explode1, 0, 0, 64, 64, 5, 25, 0.04f));
	explode.instance.setX(idle.instance.getX());
	explode.instance.setY(idle.instance.getY());

	state = JetState.IDLE;

	invincible = false;
	moveSpeed = MathUtils.random(2f);
	runToJet = MathUtils.randomBoolean();
	lifePoints = 25 + MathUtils.random(20);
	projectileDelay = 1 + MathUtils.random(3f);
    }

    @Override
    public void explode() {
	super.explode();
	stateTime = 0;
	//	idle.tweenManager.update(1000);
	if (idle.instance.getName() == "CHARLES" || idle.instance.getName() == "ARJAY" || idle.instance.getName() == "CHAS") {
	    idle.instance.setColor(1f, 1f, 1f, 1f);
	    idle.interpolateColorRGB(1f, 0f, 0f, 250, true);
	    idle.interpolateColorRGB(1f, 1f, 1f, 250, true).delay(250);
	} else if (idle.instance.getName() == "POGS") {
	    idle.instance.setScale(1f, 1f);
	    idle.interpolateScaleXY(1.35f, 1.35f, Bounce.OUT, 500, true);
	    idle.interpolateScaleXY(1f, 1f, 500, true).delay(500);
	} else if (idle.instance.getName() == "EMAN") {
	    idle.instance.setRotation(0);
	    idle.interpolateRotation(MathUtils.random(360), Bounce.OUT, 500, true);
	    idle.interpolateRotation(0f, 500, true).delay(500);
	}
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
	super.render(batch, delta);
	if (getState().equals(idle) && runToJet && jet.idle.instance.getY() < idle.instance.getY())
	    if (jet.idle.instance.getX() < idle.instance.getX())
		idle.instance.setX(idle.instance.getX() - moveSpeed);
	    else if (jet.idle.instance.getX() > idle.instance.getX())
		idle.instance.setX(idle.instance.getX() + moveSpeed);
	if (getState().equals(idle))
	    idle.instance.setY(idle.instance.getY() - moveSpeed);
	boolean onScreen = new Rectangle(idle.instance.getX(), idle.instance.getY(), idle.instance.getWidth(), idle.instance.getHeight()).overlaps(new Rectangle(camera.position.x, camera.position.y, camera.viewportWidth / 2, camera.viewportHeight / 2));
	if (onScreen && projectileStateTime > projectileDelay) {
	    float targetX = runToJet ? jet.idle.instance.getX() - idle.instance.getX() : 0;
	    Projectile p = new Projectile(Assets.entities.findRegion("PROJECTILE"), jet, camera, projectiles, targetX, -1000, idle.instance.getX(), idle.instance.getY());
	    p.speed = .35f + (float) Math.random();
	    mobProjectiles.add(p);
	    projectileStateTime = 0;
	}

	effect.setPosition(idle.instance.getX(), idle.instance.getY());
	if (idle.instance.getName() == "KING" && state == JetState.EXPLODE)
	    effect.draw(batch, delta);
    }

    @Override
    protected void updateExploding(float delta) {
	if (stateTime >= .9f)
	    mobs.remove(this);
    }

}
