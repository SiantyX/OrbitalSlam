package networking;

import game.Game;
import gamestates.ClientLobbyState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetHandler {
	private final int port = 7661;
	private final String ipaddr = "siantyxserver.servegame.com";
	private SocketChannel sc;
	private Lobby currentLobby;

	public NetHandler() throws UnknownHostException, IOException {
		sc = SocketChannel.open();
	}

	public boolean JoinLobby(Lobby l) throws IOException {
		currentLobby = l;

		try {
			if(sc.isConnected()) return true;
			sc = SocketChannel.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress(currentLobby.getIpAddress(), 7662));
			long oldTime = System.currentTimeMillis();
			while(!sc.finishConnect() && ((System.currentTimeMillis() - oldTime) < 5000)) {
			}
			if(!sc.finishConnect()) {
				System.out.println("Couldn't connect to server " + currentLobby.getIpAddress() + " at port " + port);
				return false;
			}
			
			// connected
			ClientLobbyState.hndlr = this;
			CharBuffer buffer = CharBuffer.wrap("name\n" + Game.username);
			System.out.println("Sending username");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}
			
			return true;
		}
		catch (ConnectException e) {
			System.out.println("Couldn't connect to server " + currentLobby.getIpAddress() + " at port " + port);
			return false;
		}
	}
	
	public void updateClientLobby(CopyOnWriteArrayList<String> players) {
		try {
			while (currentLobby != null) {
				ByteBuffer inbuffer = ByteBuffer.allocate(1024);
				int count = 0;
				String msg = "";
				while((count = sc.read(inbuffer)) > 0) {
					inbuffer.flip();
					msg += Charset.defaultCharset().decode(inbuffer);
				}
				inbuffer.clear();
				if(msg.length() < 1) continue;
				
				System.out.println("Server says: " + msg);
				
				String[] parts = msg.split("\\n");
				if(msg.equals("kick")) {
					break;
				}
				else if(msg.equals("start")) {
					// start game trololol
				}
				else if(parts[0].equals("names")) {
					players.clear();
					for(int i = 1; i < parts.length; i++) {
						if(!parts[i].equals(""))
							players.add(parts[i]);
					}
				}
				else if(parts[0].equals("chat")) {
					// fixa chat
				}
			}
		}
		catch (IOException e) {
			System.out.println("!update client error!");
			e.printStackTrace();
		}
	}

	public boolean connectToMainServer() throws IOException {
		try {
			if(sc.isConnected()) return true;
			sc = SocketChannel.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress(ipaddr, port));
			long oldTime = System.currentTimeMillis();
			while(!sc.finishConnect() && ((System.currentTimeMillis() - oldTime) < 5000)) {
			}
			if(!sc.finishConnect()) {
				System.out.println("Couldn't connect to server " + ipaddr + " at port " + port);
				return false;
			}
			return true;
		}
		catch (ConnectException e) {
			System.out.println("Couldn't connect to server " + ipaddr + " at port " + port);
			return false;
		}
	}

	public void GetLobbys(ArrayList<Lobby> browser) throws IOException {
		if(connectToMainServer()) {
			CharBuffer buffer = CharBuffer.wrap("servers");
			System.out.println("Requesting lobby list.");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}
			while (true) {
				ByteBuffer inbuffer = ByteBuffer.allocate(1024);
				int count = 0;
				String msg = "";
				while((count = sc.read(inbuffer)) > 0) {
					inbuffer.flip();
					msg += Charset.defaultCharset().decode(inbuffer);
				}
				inbuffer.clear();
				if(msg.length() < 1) continue;

				System.out.println("Server says: " + msg);
				if(msg.equals("eof")) {
					break;
				}
				else {
					browser.add(new Lobby(msg));
				}
			}
			try {
				sc.socket().close();
				sc.close();
			}
			catch (IOException e) {
				System.out.println("Could not close server.");
			}
		}
	}

	public void updateHost(String name, int curp, int maxp) throws IOException {
		if(connectToMainServer()) {
			CharBuffer buffer = CharBuffer.wrap("host_" + 7661 + " " + name + " " + curp + " " + maxp);
			System.out.println("Sending host update");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}
		}
	}

	public void close() {
		try {
			currentLobby = null;
			sc.socket().close();
			sc.close();
		} catch (IOException e) {
			System.out.println("Couldn't close socket");
		}
	}

	public static String getHostName() {
		String hostname = "Unknown";

		try
		{
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Hostname can not be resolved");
		}
		return hostname;
	}
}
