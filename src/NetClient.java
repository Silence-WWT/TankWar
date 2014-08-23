import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {

	private static int Udp_Port_Start = 2002;
	private int Udp_Port1 = Udp_Port_Start;

	private int Udp_Port;
	private String IP = null;

	public int getUdp_Port() {
		return Udp_Port1;
	}

	public void setUdp_Port(int udp_Port) {
		Udp_Port1 = udp_Port;
	}

	Socket s = null;
	TankClient tc;
	DatagramSocket ds = null;
	TankServer ts;

	public NetClient(TankClient tc) {
		this.tc = tc;
	}

	public void connect(String IP, int Port) {
		try {
			this.IP = IP;
			s = new Socket(IP, Port);
			ds = new DatagramSocket(Udp_Port1);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(Udp_Port1);
			
			System.out.println("Connect!");
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int ID = dis.readInt();
			tc.myTank.id = ID;
System.out.println(ID);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UdpRecvThread()).start();
	}

	public void send(Msg msg) {
		msg.send(ds, IP, TankServer.UDP_Port);
	}

	private class UdpRecvThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {

			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					// System.out.println("a packet received from Server!");
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0,
					buf.length);
			DataInputStream dis = new DataInputStream(bais);
			try {
				int msgtype = dis.readInt();
				Msg msg = null;
				switch (msgtype) {
				case Msg.Tank_Move_Msg:
					msg = new TankMoveMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.Tank_New_Msg:
					msg = new TankNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.Missile_New_Msg:
					msg = new MissileNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.Tank_Dead_Msg:
					msg = new TankDeadMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.Blood_Eat_Msg:
					msg = new BloodEatMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.Tank_Resurgence_Msg:
					msg = new TankResurgenceMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
