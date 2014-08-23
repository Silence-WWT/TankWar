import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class MissileNewMsg implements Msg {

	TankClient tc;
	Missile m;

	int msgtype = Msg.Missile_New_Msg;

	public MissileNewMsg(Missile m) {
		this.m = m;
	}

	MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int Udp_Port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgtype);
			dos.writeInt(m.tankId);
			dos.writeInt(m.x);
			dos.writeInt(m.y);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.good);
			;
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
			if (id == tc.myTank.id)
				return;
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			Missile m = new Missile(id, x, y, dir, good, tc);
			tc.missiles.add(m);

			// System.out.println("---good:"+good+"---x:"+x+"---y:"+y+"---id:"+id+"---dir:"+dir)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
