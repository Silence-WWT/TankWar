import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Robot extends Tank {
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;

	String Name = "Others";
	TankName tn = new TankName();

	
	protected static Image[] TankImages = null;
	protected static Map<String, Image> imgs = new HashMap<String, Image>();
	static {
		TankImages = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankL.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankLD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankLU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankR.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankRD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankRU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource(
						"Images/htankU.png")) };
		imgs.put("D", TankImages[0]);
		imgs.put("L", TankImages[1]);
		imgs.put("LD", TankImages[2]);
		imgs.put("LU", TankImages[3]);
		imgs.put("R", TankImages[4]);
		imgs.put("RD", TankImages[5]);
		imgs.put("RU", TankImages[6]);
		imgs.put("U", TankImages[7]);
	}

	public Robot(int x, int y, Direction dir, TankClient tc) {
		super(x, y, false);
		this.tc = tc;
		this.dir = dir;
		good = false;
	}

	private class TankName{
		public void draw(Graphics g){
			g.setFont(new Font("Tahoma", Font.BOLD, 12));
			g.drawString(Name, x+5, y-10);
		}
	}
	
	public void draw(Graphics g) {
		if (!live) {
			tc.tanks.remove(this);
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
		if(tc.isTogether()){
			tn.draw(g);
		}
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
		Direction dirs[] = Direction.values();
		if (step == 0) {
			step = r.nextInt(12) + 3;
			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
		}
		step--;
		if (r.nextInt(80) > 75)
			this.fire();
		tc.sound.enemyMove();
	}

	public void stay() {
		this.x = this.oldX;
		this.y = this.oldY;
	}

	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id, x, y, ptDir, good, tc);
		tc.missiles.add(m);
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

}
