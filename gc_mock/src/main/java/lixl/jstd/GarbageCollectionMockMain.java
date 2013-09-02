// -------------------------------------------------------------------------
//
// Project name: gc_mock
//
// Platform : Java virtual machine
// Language : JAVA 6.0
//
// Original author: caitsithx@live.com
// -------------------------------------------------------------------------
package lixl.jstd;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:caitsithx@live.com">caitsithx </a>
 * 
 * 
 */
public class GarbageCollectionMockMain {
   private final static int BOX_NUMBER = 5;

   private final static int OLD_FACTOR = 5;

   private final static String data = "hello word, I am from gc world. nice to meet you! GOGOGO! Let's GO!";

   private static final Box[] BOXES = new Box[BOX_NUMBER];

   static {
	for (int i = 0; i < BOXES.length; i++) {
	   BOXES[i] = new Box();
	}
   }
   
   public static void main(String[] args) {
	final ExecutorService l_exec = Executors.newCachedThreadPool();

	ScheduledExecutorService l_exec1 = Executors.newScheduledThreadPool(1);
	l_exec1.scheduleWithFixedDelay(new Runnable() {

	   public void run() {
		try {

		   StringBuilder l_sb = new StringBuilder();
		   for (Box l_box : BOXES) {
			l_sb.append(l_box.dataList.size());
			l_sb.append('-');
		   }
		   System.out.println(l_sb.toString());

		   l_exec.execute(new ObjGenerationThread());
		   l_exec.execute(new ObjGenerationThread());
		   l_exec.execute(new ObjConsumeThread());
		   l_exec.execute(new ObjGenerationThread());
		   l_exec.execute(new ObjGenerationThread());
		   l_exec.execute(new ObjConsumeThread());
		   l_exec.execute(new ObjGenerationThread());

		} catch (Exception l_ex) {
		   l_ex.printStackTrace();
		}
	   }
	}, 1, 5, TimeUnit.SECONDS);

   }

   static class Box {
	final ArrayList<String> dataList = new ArrayList<String>();

	final ReentrantLock lock = new ReentrantLock();

	void add(String p_string) {
	   try {
		lock.lock();
		dataList.add(p_string);
	   } finally {
		if (lock.isHeldByCurrentThread()) {
		   lock.unlock();
		}
	   }
	}

	String remove() {
	   try {
		if (lock.tryLock() || lock.tryLock(1, TimeUnit.SECONDS)) {
		   if (!dataList.isEmpty()) {
			return dataList.remove(0);
		   }
		}
	   } catch (InterruptedException ex) {
		// TODO Auto-generated catch block
		ex.printStackTrace();
	   } finally {
		if (lock.isHeldByCurrentThread()) {
		   lock.unlock();
		}
	   }

	   return null;
	}
   }

   static class ObjGenerationThread implements Runnable {
	public void run() {
	   for (int i = 0; i < 10000; i++) {
		StringBuffer l_sb = new StringBuffer(data);
		for(int j = 0; j < 10; j ++) {
		   l_sb.append(data);
		}
		l_sb.append(data);
		Random l_rd = new Random();
		long l_key = l_rd.nextLong();
		if (l_key % OLD_FACTOR == 1) {
		   add(i, l_sb.toString());
		}
	   }

	}

	/**
	 * @param p_string
	 * 
	 */
	private void add(int p_index, String p_string) {
	   int l_boxIndex = p_index % BOX_NUMBER;

	   BOXES[l_boxIndex].add(p_string);
	}
   }

   static class ObjConsumeThread implements Runnable {
	public void run() {
	   for (int i = 0; i < 4000; i++) {
		   remove();
	   }
	}

	private void remove() {
	   for (int i = 0; i < BOX_NUMBER; i++) {
		if (BOXES[i].remove() != null) {
		   break;
		}
	   }
	}

   }

}
