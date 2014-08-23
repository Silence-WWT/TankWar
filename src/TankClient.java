import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;


public class TankClient extends Frame {
	int x = 50, y = 50;
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	int level = 0;

	private boolean gameAlone = false;
	private boolean gameTogether = false;

	Tank myTank = new Player(500, 500, Direction.STOP, this);
	Wall w1 = new Wall(100, 250, 48, 200, this);
	Wall w2 = new Wall(600, 150, 48, 200, this);
	
	Prop blood = new Blood();
	Prop ammunition = new Ammunition();
	Prop tanklife = new NewLife();
	
	NetClient nc = null;
	
	Sounds sound = new Sounds();
	
	ConnDialog dlg = null;
	Dialog choose = new Dialog(this, "请选择游戏模式！");

	public static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image img = tk.getImage(TankClient.class.getClassLoader()
			.getResource("Images/grass.png"));
	private static Image imgAmm = tk.getImage(TankClient.class.getClassLoader()
			.getResource("Images/ammunition.png"));
	private static Image BloodBarImg = tk.getImage(TankClient.class
			.getClassLoader().getResource("Images/bloodBar.png"));
	private static Image newLife = tk.getImage(TankClient.class
			.getClassLoader().getResource("Images/tankLife.png"));
	
	private static Image BloodBarImgs[] = null;

	static {
		BloodBarImgs = new Image[] {
				null,
				tk.getImage(TankClient.class.getClassLoader().getResource(
						"Images/blood20%.png")),
				tk.getImage(TankClient.class.getClassLoader().getResource(
						"Images/blood40%.png")),
				tk.getImage(TankClient.class.getClassLoader().getResource(
						"Images/blood60%.png")),
				tk.getImage(TankClient.class.getClassLoader().getResource(
						"Images/blood80%.png")),
				tk.getImage(TankClient.class.getClassLoader().getResource(
						"Images/blood100%.png")), };
	}

	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();

	public void paint(Graphics g) {
		g.setFont(new Font("Tahoma", Font.BOLD, 22));
		g.drawString("LEVEL " + level, 350, 50);
		if (gameAlone && tanks.size() <= 0) {
			level++;
			for (int i1 = 0; i1 < 10; i1++) {
				tanks.add(new Robot(50 + (i1 + 1) * 75, 50, Direction.D, this));
			}
		}
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTank(myTank);
			m.hitTanks(tanks);
			m.draw(g);
			m.hitWall(w1);
			m.hitWall(w2);
		}
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
			t.hitWall(w1);
			t.hitWall(w2);
			t.hitTanks(tanks);
		}
		
		
			myTank.hitWall(w1);
			myTank.hitWall(w2);
			
		
		
		/*g.drawString("Missiles Counts:" + missiles.size(), 10, 50);
		g.drawString("Explodes Counts:" + explodes.size(), 10, 60);
		g.drawString("Tanks Counts:" + tanks.size(), 10, 70);
		g.drawString("Tanks Life:" + myTank.getLife(), 10, 80);*/
		if (blood.isLive()) {
			blood.draw(g);
			myTank.eatProp(blood);
		}

		if (ammunition.isLive()) {
			ammunition.draw(g);
			myTank.eatProp(ammunition);
		}
		
		if(tanklife.isLive()) {
			tanklife.draw(g);
			myTank.eatProp(tanklife);
		}
		
		myTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		if (!myTank.isLive() && isTogether()) {
			TankDeadMsg msg = new TankDeadMsg(myTank.id);
			this.nc.send(msg);
		}
	}

	Image offScreenImage = null;

	public void update(Graphics g) {
		if (offScreenImage == null)
			offScreenImage = this.createImage(GAME_WIDTH, GAME_WIDTH);
		Graphics goff = offScreenImage.getGraphics();
		goff.drawImage(img, 0, 0, null);
		for (int i = 0; i < myTank.getSuperfire(); i++) {
			goff.drawImage(imgAmm, 620 + 35 * i, 560, null);
		}
		
		for(int i = 0; i < myTank.getNewLife(); i++) {
			goff.drawImage(newLife, 10 + 35 * i, 560, null);
		}
		
		goff.drawImage(BloodBarImg, 180, 550, null);
		goff.drawImage(BloodBarImgs[myTank.getLife() / 20], 226, 567, null);
		paint(goff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void launchFrame() {
		new ChooseDialog();
		sound.init();
		if (gameAlone) {
			for (int i = 0; i < 10; i++) {
				tanks.add(new Tank(50 + (i + 1) * 40, 50, false, Direction.D,
						this));

			}
		}

		this.setLocation(50, 50);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		this.setBackground(Color.gray);
		this.setTitle("TankWar");
		new Thread(new PaintThread()).start();
		this.addKeyListener(new KeyMonitor());
		// nc.connect("192.168.1.112", 8888);
	}

	public boolean isTogether() {
		return gameTogether;
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}

		}

	}

	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_C) {
				// dlg.setVisible(true);
			} else {
				myTank.keyPressed(e);
			}
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	// 游戏开始时选择游戏模式
	private class ChooseDialog {
		Button alone = new Button("单人模式");
		Button together = new Button("多人对战");
		Panel p = new Panel();

		public ChooseDialog() {
			choose.setVisible(true);
			choose.setBounds(300, 250, 200, 200);
			choose.add(p);
			p.setBounds(0, 0, 200, 200);
			p.setLayout(null);
			p.add(alone);
			p.add(together);
			alone.setBounds(40, 30, 100, 40);
			together.setBounds(40, 100, 100, 40);
			alone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gameAlone = true;
					gameTogether = false;
					choose.setVisible(false);
					sound.start();
				}
			});
			together.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gameAlone = false;
					gameTogether = true;
					nc = new NetClient(TankClient.this);
					choose.setVisible(false);
					dlg = new ConnDialog();
					dlg.setVisible(true);
					sound.start();
				}
			});

		}

	}

	class ConnDialog extends Dialog {
		TextField tfIP = new TextField("192.168.1.138", 12);
		TextField tfPort = new TextField("" + TankServer.TCP_Port, 4);
		TextField tfCP = new TextField("10000", 5);
		TextField tfName = new TextField("请输入用户名", 12);
		Button b = new Button("确定");

		ConnDialog() {
			super(TankClient.this, true);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}

			});
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("Client UDP Port:"));
			this.add(tfCP);
			this.add(new Label("Client Name:"));
			this.add(tfName);
			this.add(b);
			this.pack();
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
					String IP = tfIP.getText().trim();
					String Name = tfName.getText().trim();
					myTank.setName(Name);
					int port = Integer.parseInt(tfPort.getText().trim());
					int CP = Integer.parseInt(tfCP.getText().trim());
					nc.setUdp_Port(CP);
					nc.connect(IP, port);
				}

			});
		}
	}

}
