package networking;

import game.Game;
import game.MessageBox;
import game.Player;
import gamestates.ClientLobbyState;
import gamestates.ClientMultiplayerState;
import gamestates.MultiplayerState;

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
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetHandler {
	private final int port = 7661;
	private final String ipaddr = "siantyxserver.servegame.com";
	private SocketChannel sc;
	public Lobby currentLobby;
	public boolean started;

	public NetHandler() throws UnknownHostException, IOException {
		sc = SocketChannel.open();
		started = false;
	}

	public boolean JoinLobby(Lobby l) throws IOException {
		try {
			if(sc.isConnected()) return true;
			sc = SocketChannel.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress(l.getIpAddress(), port));
			long oldTime = System.currentTimeMillis();
			while(!sc.finishConnect() && ((System.currentTimeMillis() - oldTime) < 5000)) {
			}
			if(!sc.finishConnect()) {
				System.out.println("Couldn't connect to server " + l.getIpAddress() + " at port " + port);
				return false;
			}

			// connected
			ClientLobbyState.hndlr = this;
			CharBuffer buffer = CharBuffer.wrap("!name\n" + Game.username);
			System.out.println("Sending username");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}

			currentLobby = l;
			return true;
		}
		catch (ConnectException e) {
			System.out.println("Couldn't connect to server " + currentLobby.getIpAddress() + " at port " + port);
			return false;
		}
	}

	public void sendChatUpdate(String str) {
		try {
			ClientLobbyState.hndlr = this;
			CharBuffer buffer = CharBuffer.wrap("!chat\n" + str);
			System.out.println("Sending chat update");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}
		}
		catch (IOException e) {
			System.out.println("Couldn't send chat update.");
			e.printStackTrace();
		}
	}

	public void sendHookUpdate() {
		try {
			ClientLobbyState.hndlr = this;
			CharBuffer buffer = CharBuffer.wrap("!hook");
			System.out.println("Sending hook update.");
			while(buffer.hasRemaining()) {
				sc.write(Charset.defaultCharset().encode(buffer));
			}
		}
		catch (IOException e) {
			System.out.println("Couldn't send hook update.");
			e.printStackTrace();
		}
	}

	public void updateClientLobby(CopyOnWriteArrayList<String> players, MessageBox mbox) {
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

				//System.out.println("Server says: " + msg);

				String[] packages = splitPackages(msg);
				for(String pak : packages) {
					String[] parts = splitInfo(pak);

					//handleInfo
					if(parts[0].equals("kick")) {
						break;
					}
					else if(parts[0].equals("start")) {
						started = true;
					}
					else if(parts[0].equals("names")) {
						players.clear();
						ClientMultiplayerState.names.clear();
						for(int i = 1; i < parts.length; i++) {
							if(!parts[i].equals("")) {
								players.add(parts[i]);
								ClientMultiplayerState.names.add(parts[i]);
							}
						}
					}
					else if(parts[0].equals("chat")) {
						String wholemsg = "";
						for(int i = 1; i < parts.length; i++) {
							wholemsg += parts[i];
						}
						mbox.addMessage(wholemsg);
					}
					else if(parts[0].equals("hook")) {
						Player p = MultiplayerState.players.get(Integer.parseInt(parts[1]));
						if(parts[2].equals("true")) {
							p.setHooked(false);
							p.hook();
						}
						else {
							p.setHooked(false);
						}
						p.setDx(Float.parseFloat(parts[5]));
						p.setDy(Float.parseFloat(parts[6]));
						p.getPosition().x = Float.parseFloat(parts[3]);
						p.getPosition().y = Float.parseFloat(parts[4]);
					}

					else if(parts[0].equals("pos")) {
						if(!(MultiplayerState.players == null) && !MultiplayerState.players.isEmpty()) {
							Player p = MultiplayerState.players.get(Integer.parseInt(parts[1]));
							p.setDx(Float.parseFloat(parts[4]));
							p.setDy(Float.parseFloat(parts[5]));
							p.getPosition().x = Float.parseFloat(parts[2]);
							p.getPosition().y = Float.parseFloat(parts[3]);
						}
					}
				}
			}
		}
		catch (IOException e) {
			System.out.println("Disconnected from server.");
			//e.printStackTrace();
		}
		finally {
			close();
		}
	}

	public String[] splitPackages(String msg) {
		String[] tmp = msg.split("\\!");
		String[] rSplit = new String[tmp.length-1];
		for(int i = 1; i < tmp.length; i++) {
			rSplit[i-1] = tmp[i];
		}

		return rSplit;
	}

	public String[] splitInfo(String msg) {
		return msg.split("\\n");
	}

	public void handlePos(String[] parts) {
		Player p = MultiplayerState.players.get(Integer.parseInt(parts[1]));
		p.setDx(Float.parseFloat(parts[4]));
		p.setDy(Float.parseFloat(parts[5]));
		p.getPosition().x = Float.parseFloat(parts[2]);
		p.getPosition().y = Float.parseFloat(parts[3]);
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
		catch (UnresolvedAddressException e) {
			System.out.println("Couldn't connect to DNS server for " + ipaddr + " at port " + port);
			return false;
		}
	}

	public void GetLobbys(ArrayList<Lobby> browser) throws IOException {
		if(connectToMainServer()) {
			CharBuffer buffer = CharBuffer.wrap("!servers");
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
			CharBuffer buffer = CharBuffer.wrap("!host_" + 7661 + " " + name + " " + curp + " " + maxp);
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
