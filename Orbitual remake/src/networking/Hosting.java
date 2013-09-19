package networking;

import game.Game;

import java.io.IOException;
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
import java.util.Iterator;
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
	protected Selector selector;
	protected ServerSocketChannel server;
	protected SXTimer timer;

	public Hosting(String hostname, int maxPlayers) throws IOException {
		hndlr = new NetHandler();
		this.hostname = hostname;
		this.maxPlayers = maxPlayers;
		closing = false;
		inLobby = true;
	}

	public void run() {
		timer = new SXTimer(updateInterval);

		selector = null;
		server = null;
		boolean isAcceptable = false;
		boolean isConnectable = false;
		boolean isReadable = false;
		boolean isWritable = false;

		try {
			selector = Selector.open();
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(7661));
			server.register(selector, SelectionKey.OP_ACCEPT);	
			
			// update server with host
			beforeSelect();
			
			while(true)
			{
				if(closing) {
					server.socket().close();
					server.close();
					selector.close();
					break;
				}

				// act as server for incoming connections

				selector.select();
				for(Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
					System.out.println("stage 0");
					
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
		if(channel.isOpen()) 
			System.out.println(channel.getRemoteAddress().toString() + ":  " + msg);
		readBuffer.clear();
		return msg;
	}

	protected void writeMessage(SelectionKey key, String msg) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		CharBuffer buffer = CharBuffer.wrap(msg);
		while(buffer.hasRemaining()) {
			channel.write(Charset.defaultCharset().encode(buffer));
		}
		buffer.clear();
		System.out.println("Wrote: " + msg + " to " + channel.getRemoteAddress().toString());
	}
	
	protected abstract void accept();
	protected abstract void read(SelectionKey key);
	protected abstract void write(SelectionKey key);
	protected abstract void beforeSelect();
}
