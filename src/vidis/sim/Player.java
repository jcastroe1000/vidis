package vidis.sim;

import javax.swing.JOptionPane;

public class Player {
	private boolean paused = true;
	private boolean stopped = false;
	private boolean killed = false;
	private Thread worker;

	public Player() {
 //           Simulator.configDisable3D();
		worker = new Thread() {
			@Override
			public void run() {
				long steps = 0;
				long sleepTime = 250;
				while (!killed) {
					steps = alive(steps, sleepTime);
				}
			}

			private long alive(long steps, long sleepTime) {
				try {
					if (!stopped) {
						if (!paused) {
							try {
								long simPre = System.currentTimeMillis();
								Simulator.getInstance().simulateOneStep();
								long simPost = System.currentTimeMillis();
								long simulatedMillis = simPost - simPre;
								long sleepMillis = sleepTime - simulatedMillis;
								if (sleepMillis > 0)
									Thread.sleep(sleepMillis);
								// Logger.output(LogLevel.DEBUG, "Player", "simulatedMillis="
								// + simulatedMillis);
								steps++;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OutOfMemoryError e) {
								JOptionPane.showMessageDialog(null , "OUT OF MEMORY ERROR: \n"+e.getMessage());
								System.exit(99);
							}
						} else {
							Thread.sleep(150);
						}
					} else {
						Simulator.getInstance().reset();
						Thread.sleep(150);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return steps;
			}
		};
	}

	public void startWorker() {
		worker.setName("Vidis Player");
		worker.start();
	}

	public void play() {
		paused = false;
		stopped = false;
	}
	
	public void playPause() {
		if ( stopped ) {
			play();
		} else {
			pause();
		}
	}

	public void pause() {
		if (paused)
			paused = false;
		else
			paused = true;
	}

	public void stop() {
		stopped = true;
		paused = true;
	}

	public void kill() {
		stop();
		killed = true;
	}

	public boolean isPaused() {
		return this.paused;
	}
}
