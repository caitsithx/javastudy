package lixl.util.concurrent;


public class PrintTask implements Runnable {

	private char printChar = 'A';
	private char nextChar = 'B';
	private int count = 0;

	private char[] shared = null;

	public PrintTask(char printChar, char nextChar, char[] shared) {
		super();
		this.printChar = printChar;
		this.nextChar = nextChar;
		this.shared = shared;
	}
	public void run() {
		synchronized (shared) {
			while(count < 3) {
				if(shared[0] == printChar) {
					System.out.println(printChar);
					shared[0] = nextChar;
					count ++;
					shared.notifyAll();
				}

				try {
					shared.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
