package networking;

import game.Game;
import game.MessageBox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import components.SXTimer;

public class LobbyHosting extends Hosting {
	private MessageBox mbox;

	public LobbyHosting(String hostname, int maxPlayers, MessageBox mbox) throws IOException {
		super(hostname, maxPlayers);
		this.mbox = mbox;
	}

	protected void beforeSelect() throws IOException {
		timer = new SXTimer(updateInterval);
		selector = Selector.open();
		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		server.socket().bind(new InetSocketAddress(port));
		server.register(selector, SelectionKey.OP_ACCEPT);	
		serverKey = server.keyFor(selector);

		Runnable r = new Runnable() {
			public void run() {
				while(!closing && inLobby) {
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
			//e.printStackTrace();
		}
	}

	protected void read(SelectionKey key) {
		try {
			String msg = readIncomingMessage(key);
			//String parts[] = message.split("\\n");

			String[] packages = splitPackages(msg);
			for(String pak : packages) {
				String[] parts = splitInfo(pak);

				String wholemsg = "";
				if(parts.length > 1) {
					for(int i = 1; i < parts.length; i++) {
						wholemsg += parts[i];
					}
				}

				if(parts[0].equals("chat")) {
					// display chat message n send it
					mbox.addMessage(wholemsg);

					setAllKeys(msg);
					/*for (SelectionKey k : selector.keys()) {
					if(!k.equals(serverKey)) {
						addAttach(key, message);
					}
				}*/
				}

				else if(parts[0].equals("name")) {
					// change list of names and send name update to everyone
					String ipaddr = ((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress();
					for(String s : players) {
						if(s.split("\\@")[1].equals(ipaddr)) {
							players.remove(s);
						}
					}
					players.add(wholemsg + "@" + ((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress());

					setAllKeys(makePlayerList());
				}
			}
		} 
		catch (IOException e) {
			removePlayer(key);
			System.out.println("A player disconnected.");
			//e.printStackTrace();
		}
	}

	protected void write(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			if(key.attachment() != null) {
				String atch = popAttach(key);
				//String[] parts = atch.split("\\n");

				writeMessage(key, atch);
			}
		}
		catch (IOException e) {
			removePlayer(key);
			System.out.println("A player disconnected.");
			//e.printStackTrace();
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
				setAllKeys(makePlayerList());
				return true;
			}
		}
		key.cancel();
		return false;
	}
	
	public String makePlayerList() {
		String tmp = "!names\n";
		for(String s : players) {
			String[] p = s.split("\\@");
			String str = p[0];
			if(p[1].equals("127.0.0.1")) {
				tmp += "(Host) ";
			}
			tmp += (str.length() < 1 ? "Unknown" : str) + "\n";
		}
		return tmp;
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
