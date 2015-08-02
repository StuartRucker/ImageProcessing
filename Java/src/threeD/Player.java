package threeD;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;


public class Player {
	private float[] player = {0, 0, 20};
	private float playerVelocity[] = {0, 0, 0};

	
	//vector of looking
	private float[] horDeg = {10, 0};
	private float verDeg = 0;
	
	//constants
	public static final int LEFT = 0, RIGHT = 1, FORWARD = 2, BACK = 3, UP = 4, DOWN = 5;
	public static float GRAVITY = .5f;
	public static float PLAYERHEIGHT = 20; // Default 20
	public static float PLAYERJUMP = 5; // Default 5
	

	
	private float  PLAYERSPEED = .14f; // 10*SPEED of player - Default .14f
	private float def[] = {15, 15, 20, 0, 0, 0}; //default values for constructor
	

	public Player() {
		this(15, 15, 20, 0, 0, 0);
	}

	public Player(float a, float b, float c, float d, float e, float f) {
		player[0] = a;
		player[1] = b;
		player[2] = c;
		playerVelocity[0] = d;
		playerVelocity[1] = e;
		playerVelocity[2] = f;
	}
	public void init(){
		player[0] = def[0];
		player[1] = def[1];
		player[2] = def[2];
		playerVelocity[0] = def[3];
		playerVelocity[1] = def[4];
		playerVelocity[2] = def[5];
	}

	//move the player based on mouse input
	public void move(int constant) {
		switch (constant) {
		case 0:
			playerVelocity[1] += horDeg[0] * PLAYERSPEED;
			playerVelocity[0] -= horDeg[1] * PLAYERSPEED;
			break;
		case 1:
			playerVelocity[1] -= horDeg[0] * PLAYERSPEED;
			playerVelocity[0] += horDeg[1] * PLAYERSPEED;
			break;
		case 2:
			playerVelocity[0] += horDeg[0] * PLAYERSPEED;
			playerVelocity[1] += horDeg[1] * PLAYERSPEED;
			break;
		case 3:
			playerVelocity[0] -= horDeg[0] * PLAYERSPEED;
			playerVelocity[1] -= horDeg[1] * PLAYERSPEED;
			break;
		case 4:
			playerVelocity[2]  = PLAYERSPEED*10;
			break;
		case 5: 
			playerVelocity[2]  = -PLAYERSPEED*10;
			break;
		}
		
		for(int i = 0; i < 3; i ++){
			player[i] += playerVelocity[i];
			playerVelocity[i] = 0;
		}
	}
	

	public void look() {
		GLU.gluLookAt(player[0], player[1], player[2] + PLAYERHEIGHT, player[0] + horDeg[0] * 10, player[1] + horDeg[1] * 10, player[2]+verDeg*100, 0, 0, 1);
	}

	//turn the player
	public void rotatex(float deg) {
		float temp = horDeg[0];
		horDeg[0] = (float) (horDeg[0] * Math.cos(deg) - horDeg[1] * Math.sin(deg));
		horDeg[1] = (float) (temp * Math.sin(deg) + horDeg[1] * Math.cos(deg));
	}

	//look up and down
	public void rotatey(float deg) {
		verDeg = verDeg - (float)Math.sin(deg);
	}


	public float getPosX() {
		return player[0];
	}
	public float getPosY() {
		return player[1];
	}
	public float getPosZ() {
		return player[2];
	}
}
