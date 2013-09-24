package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class InGameHosting {
	private DatagramSocket server;
	private final int port = 7661;
	private final int bSize = 512;
	public static ArrayList<InetAddress> ipaddrs;

	public InGameHosting() {
		try {
			server = new DatagramSocket(port);
		}
		catch(IOException e) {
			System.out.println("Couldn't listen to " + port);
		}
	}

	public void recieve() {
		try {
			byte[] buffer = new byte[bSize];
			DatagramPacket packet = new DatagramPacket(buffer, bSize);
			while(true) {
				buffer = new byte[bSize];
				packet = new DatagramPacket(buffer, bSize);
				server.receive(packet);
			}
		}
		catch(IOException e) {
			// recieve error
		}
	}
}
