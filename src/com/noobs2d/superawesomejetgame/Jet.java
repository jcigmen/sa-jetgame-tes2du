package com.noobs2d.superawesomejetgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noobs2d.scene2d.DynamicAnimation;
import com.noobs2d.tween.TweenableObject;

public class Jet {

    // duration of states in millis
    static final float BANKING = 1f;

    static final float EXPLODE = .9f;
    static final float INVINCIBLE = 3f;

    public enum JetState {
	BANKING_LEFT, BANKING_RIGHT, EXPLODE, IDLE
    }

    ArrayList<Projectile> projectiles;

    TweenableObject<DynamicAnimation> idle;
    TweenableObject<DynamicAnimation> bankingLeft;
    TweenableObject<DynamicAnimation> bankingRight;
    TweenableObject<DynamicAnimation> explode;

    JetListener listener;
    JetState state;

    Camera camera;

    boolean invincible;
    float lifePoints;
    float projectileStateTime; // cooldown before next wave of projectiles
    float projectileDelay;
    float stateTime;
    float invincibleStateTime;
    float moveSpeed;

    public Jet(Camera camera) {
	this.camera = camera;
	projectiles = new ArrayList<Projectile>();

	listener = new JetListener(this);

	idle = new TweenableObject<DynamicAnimation>(new DynamicAnimation(0.2f, Assets.atlas.findRegion("RAIDEN-01")));
	idle.instance.setX(400);
	idle.instance.setY(-64);
	idle.instance.setWidth(64);
	idle.instance.setHeight(94);
	idle.instance.addListener(listener);
	idle.interpolateY(90, 500, true);

	explode = new TweenableObject<DynamicAnimation>(new DynamicAnimation(Assets.explode1, 0, 0, 64, 64, 5, 25, 0.04f));
	explode.instance.setX(Settings.SCREEN_WIDTH / 2);
	explode.instance.setY(-64);
	explode.interpolateY(90, 500, true);

	state = JetState.IDLE;

	lifePoints = 0;
	projectileDelay = 0.045f;
	invincible = true;
	moveSpeed = 7f;
    }

    public void explode() {
	lifePoints--;
	if (lifePoints <= 0)
	    state = JetState.EXPLODE;
    }

    public TweenableObject<DynamicAnimation> getState() {
	switch (state) {
	    case BANKING_LEFT:
		return idle;
	    case BANKING_RIGHT:
		return idle;
	    case EXPLODE:
		return explode;
	    case IDLE:
		return idle;
	    default:
		assert false;
		return null;
	}
    }

    public void moveDown() {
	// move jet
	float modelY = idle.instance.getY();
	float edge = camera.position.y - Settings.SCREEN_HEIGHT / 2 + idle.instance.getHeight() / 2;
	if (modelY - moveSpeed >= edge)
	    idle.instance.setY(modelY - moveSpeed);
	else if (modelY - (modelY - edge) >= edge)
	    idle.instance.setY(modelY - (modelY - edge));

	// move projectiles
	//	for (int i = 0; i < primary.size(); i++)
	//	    primary.get(i).instance.setY(primary.get(i).instance.getY() - MOVE_SPEED);
    }

    public void moveLeft() {
	// move cam
	if (camera.position.x - moveSpeed / 2 >= 300)
	    camera.position.x -= moveSpeed / 2;
	else if (camera.position.x - 300 < moveSpeed / 2)
	    camera.position.x -= camera.position.x - 300;
	else if (camera.position.x - moveSpeed / 2 <= 300)
	    camera.position.x = 300;

	// move jet
	float modelX = idle.instance.getX();
	float edge = idle.instance.getWidth() / 2;
	if (modelX - moveSpeed >= edge)
	    idle.instance.setX(modelX - moveSpeed);
	else if (modelX - (modelX - edge) >= edge)
	    idle.instance.setX(modelX - (modelX - edge));

	// move projectiles
	//	for (int i = 0; i < primary.size(); i++)
	//	    primary.get(i).instance.setX(primary.get(i).instance.getX() - MOVE_SPEED);
	for (int i = 0; i < projectiles.size(); i++)
	    projectiles.get(i).model.setX(projectiles.get(i).model.getX() - moveSpeed);
    }

    public void moveRight() {
	// move cam
	if (camera.position.x < 500 && camera.position.x + moveSpeed / 2 <= 500)
	    camera.position.x += moveSpeed / 2;
	else if (camera.position.x < 500 && 500 - camera.position.x < moveSpeed / 2)
	    camera.position.x += 500 - camera.position.x;
	else if (camera.position.x + moveSpeed / 2 >= 500)
	    camera.position.x = 500;
	// move jet
	float modelX = idle.instance.getX();
	float edge = 800 - idle.instance.getWidth() / 2;
	if (modelX + moveSpeed <= edge)
	    idle.instance.setX(modelX + moveSpeed);
	else if (modelX + edge - modelX <= edge)
	    idle.instance.setX(modelX + edge - modelX);

	// move projectiles
	//	for (int i = 0; i < primary.size(); i++)
	//	    primary.get(i).instance.setX(primary.get(i).instance.getX() + MOVE_SPEED);
	for (int i = 0; i < projectiles.size(); i++)
	    projectiles.get(i).model.setX(projectiles.get(i).model.getX() + moveSpeed);
    }

    public void moveUp() {
	// move jet
	float modelY = idle.instance.getY();
	float edge = camera.position.y + Settings.SCREEN_HEIGHT / 2 - idle.instance.getHeight() / 2;
	if (modelY + moveSpeed <= edge)
	    idle.instance.setY(modelY + moveSpeed);
	else if (modelY + edge - modelY <= edge)
	    idle.instance.setY(modelY + edge - modelY);

	// move projectiles
	//	for (int i = 0; i < primary.size(); i++)
	//	    primary.get(i).instance.setY(primary.get(i).instance.getY() + MOVE_SPEED);
	for (int i = 0; i < projectiles.size(); i++)
	    projectiles.get(i).model.setY(projectiles.get(i).model.getY() + moveSpeed);
    }

    public void render(SpriteBatch batch, float delta) {
	// draw the bullets and shit
	for (int i = projectiles.size() - 1; i >= 0; i--)
	    projectiles.get(i).render(batch, delta);

	// draw the jet itself
	getState().instance.draw(batch, idle.instance.getColor().a);
	getState().instance.act(delta);
	getState().updateTween();

	explode.instance.setX(idle.instance.getX());
	explode.instance.setY(idle.instance.getY());

	// apply input values
	if (state != JetState.EXPLODE) {
	    if (listener.buttons[JetListener.DOWN])
		moveDown();
	    if (listener.buttons[JetListener.UP])
		moveUp();
	    if (listener.buttons[JetListener.LEFT])
		moveLeft();
	    if (listener.buttons[JetListener.RIGHT])
		moveRight();
	    if (listener.buttons[JetListener.SHOOT])
		shoot();
	}

	projectileStateTime += delta;
	stateTime += delta;

	switch (state) {
	    case BANKING_LEFT:
		updateBankingLeft(delta);
		break;
	    case BANKING_RIGHT:
		updateBankingRight(delta);
		break;
	    case EXPLODE:
		updateExploding(delta);
		break;
	    case IDLE:
		break;
	    default:
		assert false;
		break;
	}

	if (invincible && invincibleStateTime <= INVINCIBLE) {
	    invincibleStateTime += delta;
	    int decimal = Integer.parseInt(Float.toString(invincibleStateTime).charAt(2) + "");
	    if (decimal % 2 == 0)
		getState().instance.getColor().a = .75f;
	    else if (decimal % 2 != 0)
		getState().instance.getColor().a = .5f;
	} else {
	    getState().instance.getColor().a = 1f;
	    invincible = false;
	}
    }

    public void shoot() {
	if (state != JetState.EXPLODE && projectileStateTime > projectileDelay) {
	    float top = idle.instance.getY() + idle.instance.getHeight() / 2;
	    Projectile p = new Projectile(this, camera, projectiles, 0, idle.instance.getY() + 1000, idle.instance.getX(), top);
	    projectiles.add(p);

	    p = new Projectile(this, camera, projectiles, 120, idle.instance.getY() + 1000, idle.instance.getX(), top);
	    p.speed = .9f;
	    projectiles.add(p);

	    p = new Projectile(this, camera, projectiles, 260, idle.instance.getY() + 1000, idle.instance.getX(), top);
	    p.speed = .8f;
	    projectiles.add(p);

	    p = new Projectile(this, camera, projectiles, -120, idle.instance.getY() + 1000, idle.instance.getX(), top);
	    p.speed = .9f;
	    projectiles.add(p);

	    p = new Projectile(this, camera, projectiles, -260, idle.instance.getY() + 1000, idle.instance.getX(), top);
	    p.speed = .8f;
	    projectiles.add(p);

	    projectileStateTime = 0;
	}
    }

    protected void updateBankingLeft(float delta) {
	if (stateTime >= BANKING) {
	    stateTime = 0;
	    state = JetState.IDLE;
	}
    }

    protected void updateBankingRight(float delta) {
	if (stateTime >= BANKING) {
	    stateTime = 0;
	    state = JetState.IDLE;
	}
    }

    protected void updateExploding(float delta) {
	if (stateTime >= EXPLODE) {
	    stateTime = 0;
	    state = JetState.IDLE;
	}
    }

    protected void updateIdling(float delta) {

    }
}
