package networking;

import game.Game;
import game.MessageBox;

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
	private MessageBox mbox;

	public LobbyHosting(String hostname, int maxPlayers, MessageBox mbox) throws IOException {
		super(hostname, maxPlayers);
		players = new CopyOnWriteArrayList<String>();
		players.add(Game.username + "@127.0.0.1");
		this.mbox = mbox;
	}

	protected void beforeSelect() {
		Runnable r = new Runnable() {
			public void run() {
				while(!closing) {
					if(timer.isTriggered() >= 0) {
						try {
							hndlr.updateHost(hostname, players.size(), maxPlayers);
						} catch (IOException e) {
							System.out.println("Couldn't update server with host data.");
						}
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
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
		catch (IOException e) {
			System.out.println("IOException in accept()");
			e.printStackTrace();
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
				mbox.addMessage(wholemsg);
				
				for (SelectionKey k : selector.keys()) {
					addAttach(key, message);
				}
			}

			if(parts[0].equals("name")) {
				// change list of names and send name update to everyone
				String ipaddr = ((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress();
				for(String s : players) {
					if(s.split("\\@")[1].equals(ipaddr)) {
						players.remove(s);
					}
				}
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
	
	public MessageBox getBox() {
		return mbox;
	}
	
	public void addToBox(String str) {
		mbox.addMessage(str);
	}

	public CopyOnWriteArrayList<String> getPlayers() {
		return players;
	}
}
