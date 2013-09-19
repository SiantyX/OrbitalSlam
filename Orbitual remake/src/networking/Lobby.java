package networking;

public class Lobby {
	private int port, curPlayers, maxPlayers, id;
	private String name, ipaddr;
	
	public Lobby(String ipaddr, int port, String name, int curPlayers, int maxPlayers) {
		this.curPlayers = curPlayers;
		this.name = name;
		this.maxPlayers = maxPlayers;
		this.port = port;
		this.ipaddr = ipaddr;

		id = ipaddr.hashCode() + port + name.hashCode();
		
	}
	
	public Lobby(String s) {
		String[] split = s.split("\\s+");
		if(split.length < 4) return;
		ipaddr = split[0];
		port = Integer.parseInt(split[1]);
		name = split[2];
		curPlayers = Integer.parseInt(split[3]);
		maxPlayers = Integer.parseInt(split[4]);
	}
	
	public void copy(Lobby l) {
		this.curPlayers = l.getCurPlayers();
		this.name = l.getName();
		this.maxPlayers = l.getMaxPlayers();
		this.port = l.getPort();
		this.ipaddr = l.getIpAddress();

		id = ipaddr.hashCode() + port + name.hashCode();
	}
	
	public String getName() {
		return name;
	}
	
	public int getCurPlayers() {
		return curPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getIpAddress() {
		return ipaddr;
	}
	
	public String toString() {
		return ipaddr + " " + port + " " + name + " " + curPlayers + " " + maxPlayers + " " + id;
	}
	
	public boolean timedOut() {
		return false;
	}
	
	public int getID() {
		return id;
	}
}
