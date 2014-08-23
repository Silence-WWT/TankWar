import java.awt.*;

public class NewLife extends Prop {
	
	private static Image img = tk.getImage(Ammunition.class.getClassLoader().getResource(
			"Images/tankLife.png"));
	
	public NewLife() {
		super();
		probability = 95;
	}
	
	public void draw(Graphics g) {
		if (!live)
			return;
		if (lastTime > 0) {
			if(lastTime < 30 && (lastTime % 3) != 0) {
				g.drawImage(null, x, y, null);
			}
			else g.drawImage(img, x, y, null);
				
			lastTime--;
		} else
			live = false;
	}
	
	public void ate(Tank t) {
		t.addNewLife();
		this.setLive(false);
		tc.sound.getLife();
	}
}
