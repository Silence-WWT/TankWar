import java.awt.*;
import java.util.*;

public class Prop {
	protected int x, y;
	protected static final int WIDTH = 20;
	protected static final int HEIGHT = 20;

	protected int lastTime = 0;
	TankClient tc;
	boolean live = false;
	int probability = 0;
	
	protected static Random r = new Random();
	protected static Toolkit tk = Toolkit.getDefaultToolkit();

	public void draw(Graphics g) {
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean born(int x, int y) {
		if (!live && r.nextInt(100) > probability) {
			this.x = x;
			this.y = y;
			lastTime = 85;
			live = true;
			return true;
		}
		return false;
	}

	public void ate(Tank t) {

	}

}
