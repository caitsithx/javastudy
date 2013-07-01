package lixl.nio.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class NIOClientMain 
{
    public static void main( String[] args )
    {
        String l_hostName = "localhost";
        int l_port = 8003;
        ExecutorService l_clientRunner = new ThreadPoolExecutor(
                1, 
                5,
                30, 
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        
        for (int i = 0; i < 1; i++) {
            
            NIOClient l_client = new NIOClient();
                l_client.setHostName(l_hostName);
                l_client.setPort(l_port);
                l_clientRunner.submit(l_client);
        }

            
    }
}
