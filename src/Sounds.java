import sun.audio.*;
import java.io.*;

public class Sounds {
	static InputStream inStart = null, inMove = null, inAmmunition = null, inLose = null, inEatProp = null,
			inFire = null, inEnemyMove = null, inFireEnd = null, inGetLife = null;
	
	static AudioStream asStart = null, asMove = null, asAmmunition = null, asLose = null, asEatProp = null,
			asFire = null, asEnemyMove = null, asFireEnd = null, asGetLife = null;
	public void init() {
		
		try {
			inStart = new FileInputStream("res/Sounds/start.wav");
			inMove = new FileInputStream("res/Sounds/move.wav");
			inAmmunition = new FileInputStream("res/Sounds/ammunition.wav");
			inLose = new FileInputStream("res/Sounds/lose.wav");
			inEatProp = new FileInputStream("res/Sounds/eatProp.wav");
			inFire = new FileInputStream("res/Sounds/fire.wav");
			inEnemyMove = new FileInputStream("res/Sounds/enemyMove.wav");
			inFireEnd = new FileInputStream("res/Sounds/fireEnd.wav");
			inGetLife = new FileInputStream("res/Sounds/getLife.wav");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			asStart = new AudioStream(inStart);
			asMove = new AudioStream(inMove);
			asAmmunition = new AudioStream(inAmmunition);
			asLose = new AudioStream(inLose);
			asEatProp = new AudioStream(inEatProp);
			asFire = new AudioStream(inFire);
			asEnemyMove = new AudioStream(inEnemyMove);
			asFireEnd = new AudioStream(inFireEnd);
			asGetLife = new AudioStream(inGetLife);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void Fire() {
		init();
		if(asFire != null) {
			AudioPlayer.player.start (asFire);
		}
		if(asFireEnd != null) {
			AudioPlayer.player.start (asFireEnd);
		}
	}
	
	public void start() {
		if(asStart != null) {
			AudioPlayer.player.start (asStart);
		}
	}
	
	public void move() {
		if(asMove != null) {
			AudioPlayer.player.start (asMove);
		}
	}
	public void ammunition() {
		if(asAmmunition != null) {
			AudioPlayer.player.start (asAmmunition);
		}
	}
	public void eatProp() {
		if(asEatProp != null) {
			AudioPlayer.player.start (asEatProp);
		}
	}
	public void Lose() {
		if(asLose != null) {
			AudioPlayer.player.start (asLose);
		}
	}
	public void getLife() {
		if(asGetLife != null) {
			AudioPlayer.player.start (asGetLife);
		}
	}
	public void enemyMove() {
		if(asEnemyMove != null) {
			AudioPlayer.player.start (asEnemyMove);
		}
	}
	
}
