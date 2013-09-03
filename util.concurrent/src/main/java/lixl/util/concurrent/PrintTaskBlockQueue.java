package lixl.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class PrintTaskBlockQueue implements Runnable{
	private char printChar = 'A';

	private char nextChar = 'B';

	private int count = 0;
	private BlockingQueue<Character> shared = null;
	
	public PrintTaskBlockQueue(char printChar, char nextChar, BlockingQueue<Character> l_shared) {
		super();
		this.printChar = printChar;
		this.nextChar = nextChar;
		shared = l_shared;
	}
	public void run() {
		while(count < 3) {
			try {
				char l_sharedChar = shared.take();
				if(l_sharedChar == printChar) {
					System.out.println(printChar);
					count++;
					shared.put(nextChar);
				} else {
					shared.put(l_sharedChar);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		final BlockingQueue<Character> l_shared = new LinkedBlockingQueue<Character>(1);
		try {
			l_shared.put('A');
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintTaskBlockQueue aTask = new PrintTaskBlockQueue('A', 'B', l_shared);
		PrintTaskBlockQueue bTask = new PrintTaskBlockQueue('B', 'C', l_shared);
		PrintTaskBlockQueue cTask = new PrintTaskBlockQueue('C', 'A', l_shared);
		
		ExecutorService l_exec = Executors.newFixedThreadPool(3);
		l_exec.submit(bTask);
		l_exec.submit(cTask);
		l_exec.submit(aTask);
	}

}
