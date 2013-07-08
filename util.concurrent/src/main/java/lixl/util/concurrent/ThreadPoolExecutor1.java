package lixl.util.concurrent;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutor1 {
	 public static void main(String[] args) throws InterruptedException,  
	 ExecutionException {
		 ThreadPoolExecutor tExec = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		 tExec.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	     // ����CompletionService
	     ArrayList<Future<String>> l_futureList = new ArrayList<Future<String>>();
		 for (int index = 0; index < 5; index++) {  
	         final int NO = index;  
	         // Callable �ӿ������� Runnable
	         Callable<String> downImg = new Callable<String>() {  
	             public String call() throws Exception {  
	                 Thread.sleep((long) (Math.random() * 10000));  
	                 return "Downloaded Image " + NO;  
	             }  
	         };
	         // �ύҪִ�е�ֵ�������񣬲����ر�ʾ������������� Future�������ʱ�����ܻ���ȡ����ѯ������
	         l_futureList.add(tExec.submit(downImg));  
	     }
	     Thread.sleep(1000 * 2);  
	     System.out.println("Show web content");
	     for (int index = 0; index < 5; index++) {
	         Future<String> task = l_futureList.get(index);
	         // ���б�Ҫ���ȴ�������ɣ�Ȼ���ȡ������
	         String img = task.get();
	         System.out.println(img);
	     }
	     System.out.println("End");
	     // �ر��̳߳� 
	     tExec.shutdown();  
	 }  
}
