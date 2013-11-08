package networking;

import game.Player;
import gamestates.ServerMultiplayerState;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
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
					Player tmp = ipplayermap.get(((SocketChannel) key.channel()).socket().getInetAddress().getHostAddress());
					if(tmp.acceptHook()) {
						tmp.hook();
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
		String ipaddr = ((SocketChannel) key.channel()).socket().getInetAddress().getHostAddress();
		for(String s : players) {
			if(s.split("\\@")[1].equals(ipaddr)) {
				players.remove(s);
				ipplayermap.remove(ipaddr);
				for(String s2 : ServerMultiplayerState.names) {
					if(s2.split("\\@")[1].equals(ipaddr)) {
						ServerMultiplayerState.players.remove(ServerMultiplayerState.names.indexOf(s2));
						ServerMultiplayerState.names.remove(s2);
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
	
	// prefix index anchor x y imgrotation stuntime
	public void sendPlayerUpdate(String prefix, Player p) {
		sendTickUpdate(prefix, Integer.toString(ServerMultiplayerState.players.indexOf(p)), Integer.toString(p.getHookedTo()), Float.toString(p.getPosition().x), Float.toString(p.getPosition().y), Float.toString(p.getdDegrees()), Double.toString(p.getStunTime()));
	}
	
	public void sendTickUpdate(String prefix, String ... data) {
		String toSend = prefix;
		for(String s : data) {
			toSend += "\n" + s;
		}
		
		setAllKeys(toSend);
	}
}
