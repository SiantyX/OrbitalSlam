package components;

public class SXTimer {
	private long oldTime;
	private long updateInterval;
	private boolean eqreset;
	
	public SXTimer (long ui) {
		eqreset = true;
		updateInterval = ui;
		reset();
	}
	
	public void reset() {
		oldTime = System.currentTimeMillis();
	}
	
	/**
	 * Use to check if enough time has gone by for update. Also resets if trigger. 
	 * Default:	reset on return >= 0. Can be changed by resetOnEqual(boolean) 
	 * @return 1 if enough time has gone by. 0 if time is exactly equal. -1 if not enough time has gone by
	 */
	public int isTriggered() {
		long thisTime = System.currentTimeMillis();
		long comp = thisTime - oldTime;
		if (comp > updateInterval) {
			reset();
			return 1;
		}
		else if (comp == updateInterval) {
			if(eqreset) reset();
			return 0;
		}
		else {
			return -1;
		}
	}
	
	public void resetOnEqual(boolean b) {
		eqreset = b;
	}
}
