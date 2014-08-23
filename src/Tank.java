import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	protected int x, y;
	protected int oldX, oldY;
	protected int life = 100;
	
	int id;
	int superfire = 0;
	int newLife = 0;
	String Name = "Others";
	
	protected static final int xSPEED = 5;
	protected static final int ySPEED = 5;
	public static final int WIDTH = 60;
	public static final int HEIGHT = 60;

	TankClient tc;
	Wall w;
	
	protected BloodBar bb = new BloodBar();
	
	protected boolean good;
	boolean live = true;

	Direction dir = Direction.STOP;
	protected Direction ptDir = Direction.D;

	protected static Toolkit tk = Toolkit.getDefaultToolkit();

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.dir = dir;
	}

	public void draw(Graphics g) {
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

public void setName(String Name){
		
	}
	
	public void bound() {
		if (x < 0)
			x = 0;
		else if (x > tc.GAME_WIDTH - 50)
			x = tc.GAME_WIDTH - 50;
		else if (y < 30)
			y = 30;
		else if (y > tc.GAME_HEIGHT - 50)
			y = tc.GAME_HEIGHT - 50;
	}

	public void move() {
	}

	public void keyPressed(KeyEvent e) {
	}

	void locateDirection() {
	}

	public void keyReleased(KeyEvent e) {
	}

	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id, x, y, ptDir, good, tc);
		tc.missiles.add(m);
		tc.sound.Fire();
		if (tc.isTogether()) {
			MissileNewMsg msg = new MissileNewMsg(m);
			tc.nc.send(msg);
		}
		return m;
	}

	public Missile fire(Direction dir) {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id, x, y, dir, good, tc);
		tc.missiles.add(m);
		if (tc.isTogether()) {
			MissileNewMsg msg = new MissileNewMsg(m);
			tc.nc.send(msg);
		}
		return m;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isGood() {
		return good;
	}

	public void stay() {
		this.x = this.oldX;
		this.y = this.oldY;
	}

	public boolean hitWall(Wall w) {
		if (this.getRect().intersects(w.getRect())) {
			stay();
		}
		return true;
	}

	public boolean hitTanks(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (t.isLive() && this.live
						&& this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}

		}
		return false;
	}

	public boolean superFire() {
		return false;
	}
	
	public void resurgence() {
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean eatProp(Prop p) {
		return false;
	}

	public void addSuperfire() {
	}

	public int getSuperfire() {
		return superfire;
	}
	
	public int  getNewLife() {
		return newLife;
	}
	
	public void addNewLife() {
	}
	
	protected class BloodBar{
		public void draw(Graphics g){
			if(tc.myTank.id != id) {
				Color c = g.getColor();
				g.setColor(Color.yellow);
				g.drawRect(x, y-10, WIDTH, 10);
				int w = WIDTH*life/100;
				g.fillRect(x, y-10, w, 10);
				//g.drawString("id:"+id, x, y-10);
				g.setColor(c);
			}
			
		}
	}
	
}
