import java.awt.*;

public class Wall {
	int x, y, w, h;
	TankClient tc;

	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}

	public static Toolkit tk = Toolkit.getDefaultToolkit();

	Image img = tk.getImage(Missile.class.getClassLoader().getResource(
			"Images/wall1.jpg"));

	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

}
