import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Player extends Tank {
	private boolean bl = false, br = false, bu = false, bd = false;
	String Name = "Others";
	TankName tn = new TankName();
	
	protected static Image[] TankImages = null;
	protected static Map<String, Image> imgs = new HashMap<String, Image>();

	static {
		TankImages = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankL.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankLD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankLU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankR.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankRD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankRU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/gtankU.png")) };
		imgs.put("D", TankImages[0]);
		imgs.put("L", TankImages[1]);
		imgs.put("LD", TankImages[2]);
		imgs.put("LU", TankImages[3]);
		imgs.put("R", TankImages[4]);
		imgs.put("RD", TankImages[5]);
		imgs.put("RU", TankImages[6]);
		imgs.put("U", TankImages[7]);
	}

	public Player(int x, int y, Direction dir, TankClient tc) {
		super(x, y, true);
		this.tc = tc;
		this.dir = dir;
		good = true;
	}

	public void setName(String Name){
		this.Name = Name;
	}
	
	private class TankName{
		public void draw(Graphics g){
			g.setFont(new Font("Tahoma", Font.BOLD, 12));
			g.drawString(Name, x+5, y-10);
		}
	}
	
	public void draw(Graphics g) {
		if (!this.isLive()) {
			return;
		}
		switch (ptDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		}
		move();
		bound();
		bb.draw(g);
		if(tc.isTogether()) tn.draw(g);
	}

	public void move() {
		this.oldX = x;
		this.oldY = y;
		Direction oldDir = this.dir;
		switch (dir) {
		case L:
			x -= xSPEED;
			break;
		case R:
			x += xSPEED;
			break;
		case D:
			y += ySPEED;
			break;
		case U:
			y -= ySPEED;
			break;
		case LU:
			x -= xSPEED;
			y -= ySPEED;
			break;
		case LD:
			x -= xSPEED;
			y += ySPEED;
			break;
		case RU:
			x += xSPEED;
			y -= ySPEED;
			break;
		case RD:
			x += xSPEED;
			y += ySPEED;
			break;
		case STOP:
			break;
		}
		if (this.dir != Direction.STOP)
			this.ptDir = this.dir;
		if (this.dir != oldDir && tc.isTogether()) {
			TankMoveMsg msg = new TankMoveMsg(id, dir, x, y);
			tc.nc.send(msg);
		}
		tc.sound.move();
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {

		case KeyEvent.VK_LEFT:
			bl = true;
			break;
		case KeyEvent.VK_RIGHT:
			br = true;
			break;
		case KeyEvent.VK_UP:
			bu = true;
			break;
		case KeyEvent.VK_DOWN:
			bd = true;
			break;
		}
		locateDirection();
	}

	void locateDirection() {
		Direction oldDir = this.dir;
		if (bl && !br && !bu && !bd)
			dir = Direction.L;
		else if (!bl && br && !bu && !bd)
			dir = Direction.R;
		else if (!bl && !br && !bu && bd)
			dir = Direction.D;
		else if (!bl && !br && bu && !bd)
			dir = Direction.U;
		else if (bl && !br && !bu && bd)
			dir = Direction.LD;
		else if (bl && !br && bu && !bd)
			dir = Direction.LU;
		else if (!bl && br && !bu && bd)
			dir = Direction.RD;
		else if (!bl && br && bu && !bd)
			dir = Direction.RU;
		else if (!bl && !br && !bu && !bd)
			dir = Direction.STOP;
		if (tc.isTogether()) {
			TankMoveMsg msg = new TankMoveMsg(id, dir, x, y);
			tc.nc.send(msg);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if (!live) {
				x = 500;
				y = 500;
				live = true;
				life = 100;
				if (tc.isTogether()) {
					TankResurgenceMsg msg = new TankResurgenceMsg(this);
					tc.nc.send(msg);
				}
			}
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bl = false;
			break;
		case KeyEvent.VK_RIGHT:
			br = false;
			break;
		case KeyEvent.VK_UP:
			bu = false;
			break;
		case KeyEvent.VK_DOWN:
			bd = false;
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		}
		locateDirection();
	}

	public boolean superFire() {
		if (superfire > 0) {

			Direction dir[] = Direction.values();
			for (int i = 0; i < 8; i++) {
				fire(dir[i]);
			}
			superfire--;
			tc.sound.ammunition();
			return true;
		}
		tc.sound.Lose();
		return false;
	}
	
	public void resurgence() {
		if (!live && newLife > 0) {
			newLife--;
			x = 500;
			y = 500;
			live = true;
			life = 100;
			if (tc.isTogether()) {
				TankResurgenceMsg msg = new TankResurgenceMsg(this);
				tc.nc.send(msg);
			}
		}
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public void setLive(boolean live) {
		this.live = live;
		if(tc.isTogether() && tc.myTank.id == id) {
			resurgence();
		}
		else if(!tc.isTogether()) {
			resurgence();
		}
	}
	
	public boolean eatProp(Prop p) {
		if (this.live && p.isLive() && this.getRect().intersects(p.getRect())) {
			p.ate(this);
			if (tc.isTogether()) {
				BloodEatMsg msg = new BloodEatMsg(this.id);
				tc.nc.send(msg);
			}
			return true;
		}
		return false;
	}

	public void addSuperfire() {
		if (superfire < 5)
			superfire++;
	}

	public int getSuperfire() {
		return superfire;
	}

	public void addNewLife() {
		if (newLife < 5)
			newLife++;
	}
	
}
