import java.awt.Graphics;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.OutputStreamWriter; 

public class TankNewMsg implements Msg {
	Tank t;
	TankClient tc;
	int msgtype = Msg.Tank_New_Msg;

	public TankNewMsg(Tank t) {
		this.t = t;
	}

	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int Udp_Port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgtype);
			dos.writeInt(t.id);
			dos.writeInt(t.x);
			dos.writeInt(t.y);
			dos.writeInt(t.dir.ordinal());
			dos.writeUTF(t.Name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(IP, Udp_Port));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (tc.myTank.id == id)
				return;
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			String name = dis.readUTF();	
			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (t.id == id) {
					exist = true;
					t.Name = name;
					break;
				}
			}
			if (!exist) {
				Tank t = new Player(x, y, dir, tc);
				t.id = id;
				t.Name = name;
				tc.tanks.add(t);
				Msg tnmsg = new TankNewMsg(tc.myTank);
				tc.nc.send(tnmsg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}