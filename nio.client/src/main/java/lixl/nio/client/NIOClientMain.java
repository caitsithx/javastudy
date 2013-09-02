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
        
            NIOTCPClientSocketWrapper l_tcpClient = new NIOTCPClientSocketWrapper();
            l_tcpClient.setRemoteHostName(l_hostName);
            l_tcpClient.setPort(l_port);
            
            NIOClientRunner l_runner = new NIOClientRunner();
            l_runner.setTcpClient(l_tcpClient);
            l_clientRunner.submit(l_runner);

            
    }
}
