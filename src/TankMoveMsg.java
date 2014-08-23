
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg implements Msg {
	int id, x, y;
	Direction dir;
	TankClient tc;
	Tank t;
	int msgtype = Msg.Tank_Move_Msg;

	TankMoveMsg(int id, Direction dir, int x, int y) {
		this.id = id;
		this.dir = dir;
		this.x = x;
		this.y = y;
	}

	TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}
	TankMoveMsg(Tank t) {
		this.t = t;
		id = t.id;
		x = t.x;
		y = t.y;
		dir = t.dir;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int Udp_Port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgtype);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
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
			boolean exit = false;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (id == t.id) {
					t.x = x;
					t.y = y;
					t.dir = dir;
					exit = true;
					break;
				}
			}
			// System.out.println("---good:"+good+"---x:"+x+"---y:"+y+"---id:"+id+"---dir:"+dir)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

