import java.awt.*;
import java.util.List;

public class Missile {
	int x, y, tankId;

	private static final int xSPEED = 10;
	private static final int ySPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	TankClient tc;
	Direction dir;
	private boolean isAlive = true;
	boolean good = true;
	Tank t;
	public static Toolkit tk = Toolkit.getDefaultToolkit();

	Image img = tk.getImage(Missile.class.getClassLoader().getResource(
			"Images/bullet_r.png"));

	public boolean isAlive() {
		return isAlive;
	}

	public Missile(int tankId, int x, int y, Direction dir) {
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int tankId, int x, int y, Direction dir, boolean good,
			TankClient tc) {
		this(tankId, x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	public void draw(Graphics g) {
		if (!isAlive) {
			tc.missiles.remove(this);
			return;
		}
		g.drawImage(img, x, y, null);
		move();
	}

	private void move() {
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
		if (x < 0 || y < 0 || x > 800 || y > 600)
			isAlive = false;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.isAlive && this.getRect().intersects(t.getRect())
				&& t.isLive() && this.tankId != t.id) {
			if (this.tankId != t.id) {
				t.setLife(t.getLife() - 20);
				if (t.getLife() <= 0) {
					t.setLive(false);
				}
					
			} else
				t.setLive(false);
			this.isAlive = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		} else if (this.isAlive && this.getRect().intersects(t.getRect())
				&& t.isLive() && this.good != t.isGood()) {
			if (t.isGood()) {
				t.setLife(t.getLife() - 20);
				if (t.getLife() <= 0) {
					t.setLive(false);
				}
					
			} else {
				t.setLive(false);
			}

			this.isAlive = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++)
			if (hitTank(tanks.get(i))) {
				this.isAlive = false;
				tc.sound.ammunition();
				if (tc.ammunition.born(x, y)) {

				} else if (tc.blood.born(x, y)) {
					
				} else {
					tc.tanklife.born(x, y);
				}
				return true;
			}
		return false;
	}

	public boolean hitWall(Wall w) {
		if (this.getRect().intersects(w.getRect())) {
			this.isAlive = false;
		}
		return true;
	}
}
