import java.io.*;
import java.net.*;
import java.util.*;

public class TankServer {
	public static final int TCP_Port = 8888;
	public static final int UDP_Port = 6666;
	static boolean start = false;
	public int ID = 100;

	List<Client> clients = new ArrayList<Client>();

	public void start() {
		new Thread(new UDPPortThread()).start();
		try {
			ServerSocket ss = new ServerSocket(TCP_Port);
			/*
			 * if(ss==null) System.out.println("1"); else if(ss!=null)
			 * System.out.println("0");
			 */
			start = true;
			while (start) {
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int Udp_Port;
				Udp_Port = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				Client c = new Client(IP, Udp_Port);
				clients.add(c);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
				System.out.println("Address:" + s.getInetAddress() + "  Port:"
						+ s.getPort() + "----UDP Port:" + Udp_Port
						+ " connect!");
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		TankServer ts = new TankServer();
		ts.start();
	}

	private class Client {
		String IP;
		int Udp_Port;

		public Client(String IP, int Udp_Port) {
			this.IP = IP;
			this.Udp_Port = Udp_Port;
		}
	}

	private class UDPPortThread implements Runnable {
		byte[] buf = new byte[1024];

		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_Port);
			} catch (SocketException e) {
				e.printStackTrace();
			}

			System.out.println("UDP thread started! at Port:" + UDP_Port);
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					// System.out.println("a packet received!");
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,
								c.Udp_Port));
						// System.out.println(c.Udp_Port);
						ds.send(dp);
						// System.out.println("a packet has send!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
