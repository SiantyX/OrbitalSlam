package networking;

import game.Game;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import components.SXTimer;

public class LobbyHosting extends Hosting {
	private CopyOnWriteArrayList<String> players;

	public LobbyHosting(String hostname, int maxPlayers) throws IOException {
		super(hostname, maxPlayers);
		players = new CopyOnWriteArrayList<String>();
		players.add(Game.username + "@127.0.0.1");
	}

	protected void beforeSelect() {
		if(timer.isTriggered() >= 0) {
			try {
				hndlr.updateHost(hostname, players.size(), maxPlayers);
			} catch (IOException e) {
				System.out.println("Couldn't update server with host data.");
			}
		}
	}

	protected void accept() {
		try {
			if(selector.keys().size() < Game.MAX_PLAYERS) {
				SocketChannel client = server.accept();
				client.configureBlocking(false);
				client.socket().setTcpNoDelay(true);
				client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				System.out.println("Client accepted: " + client.getRemoteAddress().toString());
				players.add("@" + client.socket().getInetAddress().getHostAddress());
			}
			else {
				System.out.println("Game is full");
			}
		}
		catch (Exception e) {

		}
	}

	protected void read(SelectionKey key) {
		try {
			String message = readIncomingMessage(key);
			String parts[] = message.split("\\n");
			
			String wholemsg = "";
			if(parts.length > 2) {
				for(int i = 1; i < parts.length; i++) {
					wholemsg += parts[i];
				}
			}

			if(parts[0].equals("chat")) {
				// display chat message n send it
				for (SelectionKey k : selector.keys()) {
					addAttach(key, message);
				}
			}
			
			if(parts[0].equals("name")) {
				// change list of names and send name update to everyone
				removePlayer(key);
				players.add(wholemsg + "@" + ((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress());
				
				for(SelectionKey k : selector.keys()) {
					addAttach(k, message);
				}
			}
		} 
		catch (IOException e) {
			removePlayer(key);
			e.printStackTrace();
		}
	}

	protected void write(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			if(key.attachment() != null) {
				String atch = popAttach(key);
				String[] parts = atch.split("\\n");

				if(parts[0].equals("chat")) {
					writeMessage(key, atch);
				}
				else if(parts[0].equals("name")) {
					// send name update
					String tmp = "names\n";
					for(String s : players) {
						String[] p = s.split("\\@");
						String str = p[0];
						if(p[1].equals("127.0.0.1")) {
							tmp += "(Host) ";
						}
						tmp += (str.length() < 1 ? "Unknown" : str) + "\n";
					}
					writeMessage(key, tmp);
				}
			}
		}
		catch (IOException e) {
			removePlayer(key);
			e.printStackTrace();
		}
	}
	
	// extra methods -------------------------------------------
	////////////////////////////////////////////////////////////
	
	protected String popAttach(SelectionKey key) {
		if(key.attachment() == null) return "";
		ArrayList<String> atchs = (ArrayList<String>) key.attachment();
		
		String tmp = atchs.get(0);
		atchs.remove(0);
		return tmp;
	}
	
	protected void addAttach(SelectionKey key, String msg) {
		if(key.attachment() == null) {
			ArrayList<String> atchs = new ArrayList<String>();
			atchs.add(msg);
			key.attach(atchs);
		}
		else {
			((ArrayList<String>)key.attachment()).add(msg);
		}
	}
	
	protected boolean removePlayer(SelectionKey key) {
		String ipaddr = ((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress();
		for(String s : players) {
			if(s.split("\\@")[1].equals(ipaddr)) {
				players.remove(s);
				key.cancel();
				return true;
			}
		}
		key.cancel();
		return false;
	}
	
	public CopyOnWriteArrayList<String> getPlayers() {
		return players;
	}
}
