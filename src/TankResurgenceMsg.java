import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankResurgenceMsg implements Msg {
	// 发送复活的消息
	Tank t;
	TankClient tc;
	
	int msgtype = Msg.Tank_Resurgence_Msg;
	

	public TankResurgenceMsg(Tank t) {
		this.t = t;
	}

	public TankResurgenceMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int Udp_Port) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgtype);
			dos.writeInt(t.id);
			dos.writeInt(t.life);
			dos.writeBoolean(t.live);
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
			int life = dis.readInt();
			boolean live = dis.readBoolean();
			// System.out.println("---good:"+good+"---x:"+x+"---y:"+y+"---id:"+id+"---dir:"+dir);
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (id == t.id) {
					t.setLive(true);
					t.setLife(100);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
