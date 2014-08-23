import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
	public static final int Tank_New_Msg = 1;
	public static final int Tank_Move_Msg = 2;
	public static final int Missile_New_Msg = 3;
	public static final int Tank_Dead_Msg = 4;
	public static final int Blood_Eat_Msg = 5;
	public static final int Tank_Resurgence_Msg = 6;

	public void send(DatagramSocket ds, String IP, int Udp_Port);

	public void parse(DataInputStream dis);
}
