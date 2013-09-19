package networking;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.CopyOnWriteArrayList;

public class InGameHosting extends Hosting{

	public InGameHosting(String hostname, int maxPlayers) throws IOException {
		super(hostname, maxPlayers);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void accept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void read(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void write(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beforeSelect() {
		// TODO Auto-generated method stub
		
	}

}
