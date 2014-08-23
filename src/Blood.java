import java.awt.*;

public class Blood extends Prop{
	
	private static Image img = tk.getImage(Blood.class.getClassLoader().getResource(
			"Images/blood.png"));
	
	public Blood() {
		super();
		probability = 90;
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
		t.setLife(100);
		this.setLive(false);
		tc.sound.eatProp();
	}
	
}
