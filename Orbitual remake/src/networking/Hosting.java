package networking;

import game.Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import components.SXTimer;

public abstract class Hosting extends Thread {
	protected final int port = 7661;
	protected boolean closing;
	protected NetHandler hndlr;
	protected final int updateInterval = 1000;
	protected int maxPlayers;
	protected String hostname;
	protected boolean inLobby;
	protected boolean changeHostingState;
	protected Selector selector;
	protected ServerSocketChannel server;
	protected SXTimer timer;
	protected SelectionKey serverKey;
	protected boolean isAcceptable;
	protected boolean isConnectable;
	protected boolean isReadable;
	protected boolean isWritable;
	CopyOnWriteArrayList<String> players;

	public Hosting(String hostname, int maxPlayers) throws IOException {
		hndlr = new NetHandler();
		this.hostname = hostname;
		this.maxPlayers = maxPlayers;
		closing = false;
		inLobby = true;
		selector = null;
		server = null;
		changeHostingState = false;
		isAcceptable = false;
		isConnectable = false;
		isReadable = false;
		isWritable = false;
		players = new CopyOnWriteArrayList<String>();
		players.add(Game.username + "@127.0.0.1");
	}

	public Hosting(Hosting host) {
		hndlr = host.hndlr;
		hostname = host.hostname;
		maxPlayers = host.maxPlayers;
		closing = host.closing;
		inLobby = host.inLobby;
		selector = host.selector;
		server = host.server;
		isAcceptable = false;
		isConnectable = false;
		isReadable = false;
		isWritable = false;
		players = host.players;
	}

	public void run() {
		try {
			// update server with host
			beforeSelect();

			while(true)
			{
				if(changeHostingState) {
					int l = 0;
					for(SelectionKey k : selector.keys()) {
						//System.out.println(((CopyOnWriteArrayList<String>)k.attachment()));
						if(k.equals(serverKey)) continue;

						if(((CopyOnWriteArrayList<String>)k.attachment()).contains("start")) {
							l++;
						}
					}
					if(l == 0) {
						break;
					}
				}

				if(closing) {
					server.socket().close();
					server.close();
					selector.close();
					break;
				}

				// act as server for incoming connections

				selector.select();
				for(Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {

					SelectionKey key = i.next();
					i.remove();

					isConnectable = key.isConnectable();
					isAcceptable = key.isAcceptable();
					isReadable = key.isReadable();
					isWritable = key.isWritable();

					if(isConnectable) {
						((SocketChannel) key.channel()).finishConnect();
					}

					if(isAcceptable) {
						accept();
					}

					// read
					if(isReadable) {
						if(!((SocketChannel) key.channel()).isOpen()) {
							key.cancel();
							continue;
						}
						read(key);
					}

					// write
					if(isWritable) {
						if(!((SocketChannel) key.channel()).isOpen()) {
							key.cancel();
							continue;
						}
						write(key);
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (CancelledKeyException | ClosedSelectorException e) {
			System.out.println("Closed hosting");
		}
		finally {
			if(changeHostingState) {
				changeHostingState = false;
			}
			else {
				try {
					selector.close();
					server.socket().close();
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		closing = true;
		hndlr.close();
		try {
			server.socket().close();
			server.close();
			selector.close();
		} catch (IOException e) {
			System.out.println("Can't close");
			e.printStackTrace();
		}

	}

	public boolean isInLobby() {
		return inLobby;
	}

	public void setInLobby(boolean n) {
		inLobby = n;
	}

	public void changeHost() {
		changeHostingState = true;
	}

	public void wakeup() {
		selector.wakeup();
	}

	public void setAllKeys(String str) {
		try {
			Set<SelectionKey> tmp = selector.keys();
			for(SelectionKey key : tmp) {
				if(!key.isValid() || key.equals(serverKey)) continue;
				addAttach(key, str);
			}
		}
		catch (ClosedSelectorException e) {
			System.out.println("Selector closed");
			e.printStackTrace();
			close();
		}
	}

	protected String readIncomingMessage(SelectionKey key) throws IOException {
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		SocketChannel channel = (SocketChannel) key.channel();
		int count = 0;
		String msg = "";
		while((count = channel.read(readBuffer)) > 0) {
			readBuffer.flip();
			msg += Charset.defaultCharset().decode(readBuffer);
		}
		if(count == -1) {
			channel.close();
			key.cancel();
		}
		//if(channel.isOpen()) 
			//System.out.println(channel.getRemoteAddress().toString() + ":  " + msg);
		readBuffer.clear();
		return msg;
	}

	protected void writeMessage(SelectionKey key, String msg) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		CharBuffer buffer = CharBuffer.wrap("!" + msg);
		while(buffer.hasRemaining()) {
			channel.write(Charset.defaultCharset().encode(buffer));
		}
		buffer.clear();
		//System.out.println("Wrote: " + "!" + msg + " to " + channel.getRemoteAddress().toString());
	}

	protected String popAttach(SelectionKey key) {
		if(key.attachment() == null) return "";
		CopyOnWriteArrayList<String> atchs = (CopyOnWriteArrayList<String>) key.attachment();
		if(atchs.size() < 1) return "";

		String tmp = atchs.get(0);
		atchs.remove(0);
		return tmp;
	}

	protected void addAttach(SelectionKey key, String msg) {
		if(key.attachment() == null) {
			CopyOnWriteArrayList<String> atchs = new CopyOnWriteArrayList<String>();
			atchs.add(msg);
			key.attach(atchs);
		}
		else {
			((CopyOnWriteArrayList<String>)key.attachment()).add(msg);
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

	protected abstract void accept();
	protected abstract void read(SelectionKey key);
	protected abstract void write(SelectionKey key);
	protected abstract void beforeSelect() throws IOException;
}
