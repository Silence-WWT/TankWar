import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	int x, y;
	int diameter[] = { 4, 10, 17, 25, 34, 44, 55, 30, 20, 5 };
	int step = 0;

	private boolean isLive = true;
	private boolean init = false;
	private TankClient tc;

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image imgs[] = {
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"Images/blast8.gif")) };

	public void draw(Graphics g) {
		if (!isLive) {
			tc.explodes.remove(this);
			return;
		}
		if (!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[step], -100, -100, null);
			}
			init = true;
		}
		if (step == imgs.length) {
			isLive = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}

}
