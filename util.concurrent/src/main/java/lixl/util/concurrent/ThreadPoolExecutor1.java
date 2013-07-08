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
	     // 创建CompletionService
	     ArrayList<Future<String>> l_futureList = new ArrayList<Future<String>>();
		 for (int index = 0; index < 5; index++) {  
	         final int NO = index;  
	         // Callable 接口类似于 Runnable
	         Callable<String> downImg = new Callable<String>() {  
	             public String call() throws Exception {  
	                 Thread.sleep((long) (Math.random() * 10000));  
	                 return "Downloaded Image " + NO;  
	             }  
	         };
	         // 提交要执行的值返回任务，并返回表示挂起的任务结果的 Future。在完成时，可能会提取或轮询此任务。
	         l_futureList.add(tExec.submit(downImg));  
	     }
	     Thread.sleep(1000 * 2);  
	     System.out.println("Show web content");
	     for (int index = 0; index < 5; index++) {
	         Future<String> task = l_futureList.get(index);
	         // 如有必要，等待计算完成，然后获取其结果。
	         String img = task.get();
	         System.out.println(img);
	     }
	     System.out.println("End");
	     // 关闭线程池 
	     tExec.shutdown();  
	 }  
}
