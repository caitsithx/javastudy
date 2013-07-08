package lixl.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABCPrintMain {

	public static void main(String[] args) {
		final char[] l_shared = {'A'};
		PrintTask aTask = new PrintTask('A', 'B', l_shared);
		PrintTask bTask = new PrintTask('B', 'C', l_shared);
		PrintTask cTask = new PrintTask('C', 'A', l_shared);
		
		ExecutorService l_exec = Executors.newFixedThreadPool(3);
		l_exec.submit(bTask);
		l_exec.submit(cTask);
		l_exec.submit(aTask);
	}
}
