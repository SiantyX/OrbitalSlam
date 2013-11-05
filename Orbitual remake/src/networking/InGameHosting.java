package networking;

import game.MessageBox;
import game.Player;
import gamestates.MultiplayerState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class InGameHosting extends Hosting {
	public Map<String, Player> ipplayermap;

	public InGameHosting(Hosting host) {
		super(host);
		ipplayermap = new HashMap<String, Player>();
	}

	@Override
	protected void accept() {
		return;
	}

	@Override
	protected void beforeSelect() throws IOException {
		return;
	}

	protected void read(SelectionKey key) {
		try {
			String msg = readIncomingMessage(key);
			//String parts[] = message.split("\\n");

			String[] packages = splitPackages(msg);
			for(String pak : packages) {
				String[] parts = splitInfo(pak);

				if(parts[0].equals("hook")) {
					Player tmp = ipplayermap.get(((SocketChannel)key.channel()).socket().getInetAddress().getHostAddress());
					if(tmp.acceptHook()) {
						if(tmp.hook()) {
							setAllKeys(parts[0] + "\n" + MultiplayerState.players.indexOf(tmp) + "\n" + "true" + "\n" + tmp.getPosition().x + "\n" + tmp.getPosition().y + "\n" + tmp.getDx() + "\n" + tmp.getDy());
						}
						else {
							setAllKeys(parts[0] + "\n" + MultiplayerState.players.indexOf(tmp) + "\n" + "false" + "\n" + tmp.getPosition().x + "\n" + tmp.getPosition().y + "\n" + tmp.getDx() + "\n" + tmp.getDy());
						}
					}
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
				if(!atch.equals(""))
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
				ipplayermap.remove(ipaddr);
				for(String s2 : MultiplayerState.names) {
					if(s2.split("\\@")[1].equals(ipaddr)) {
						MultiplayerState.players.remove(MultiplayerState.names.indexOf(s2));
						MultiplayerState.names.remove(s2);
					}
				}
				key.cancel();
				setAllKeys("name");
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
