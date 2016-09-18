package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

public class Character {
	private int fallSpeed = 3;
	private int velx;
	private int vely;
	private int x;
	private int y;
	private int w = 45;
	private int h = 100;
	private Hitbox hurtbox;
	private ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	public boolean isGrounded = true;
	private int keyLeft;
	private int keyRight;
	private int keyJump;
	private int keyAttack;
	private int jumpHeight = 30;
	private int runSpeed = 15;
	HashSet<Integer> keysPressed = new HashSet<Integer>();

	public Character(int posx, int posy, int leftKey, int rightKey, int jumpKey, int attackKey) {
		x = posx;
		y = posy;
		keyLeft = leftKey;
		keyRight = rightKey;
		keyJump = jumpKey;
		keyAttack = attackKey;
		hurtbox = new Hitbox(x, y, w, h, Game.TYPE_HURTBOX);
	}

	public void draw(Graphics g) {
		fall();
		handleInput();
		move();
		translateHitboxes();
		g.fillRect(x, y, w, h);
	}

	public void move() {
		x += velx;
		if (new Rectangle(x, y + vely, w, h).intersects(Game.GROUND_HITBOX.getRect()))
			y = (int) Game.GROUND_HITBOX.getRect().getY() - h;
		else
			y += vely;
	}

	public void handleInput() {
		if (isPressing(keyJump) && isGrounded)
			vely -= jumpHeight;
	}

	public void fall() {
		if (!isGrounded) {
			vely += fallSpeed;
		} else
			vely = 0;
	}
	public boolean checkIfInSpecificHitBox(Hitbox box) {
		if (hurtbox.getRect().intersects(box.getRect()))
			return true;
		return false;
	}

	public boolean isPressing(int key) {
		if (keysPressed.contains(key))
			return true;
		return false;
	}

	public void translateHitboxes() {
		hurtbox.updateLocation(x, y, w, h);
		for (Hitbox box : hitboxes) {
			if (box.getClass() == AttackHitbox.class) {
				AttackHitbox tempbox = (AttackHitbox) box;
				box.updateLocation(x + tempbox.getlocalX(), y + tempbox.getlocalY(), box.getWidth(), box.getHeight());
			}
		}
	}

	public Hitbox getHitbox() {
		return hurtbox;
	}
	//some stock getter and setter methods
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVelX() {
		return vely;
	}

	public int getVelY() {
		return velx;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}
	public int getJumpHeight(){
		return jumpHeight;
	}
	public int getRunSpeed(){
		return runSpeed;
	}
	private void setJumpHeight(int newJumpHeight){
		jumpHeight = newJumpHeight;
	}
	private void setRunSpeed(int newRunSpeed){
		runSpeed = newRunSpeed;
	}
}
