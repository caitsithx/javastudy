// -------------------------------------------------------------------------
//
// Project name: regx
//
// Platform : Java virtual machine
// Language : JAVA 6.0
//
// Original author: lixl
// -------------------------------------------------------------------------
package lixl.jstd.regx;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

/**
 *
 * 
 */
public class BlockingRegxMatcher extends AbstractRegxMatcher{

   private long timeToWait;
   /**
    * @param p_pattern
    * @param p_exeService
    * 
    */
   public BlockingRegxMatcher(Pattern p_pattern, ExecutorService p_exeService) {
	super(p_pattern, p_exeService);
	// TODO Auto-generated constructor stub
   }

   /**
    * @param p_pattern
    * 
    */
   public BlockingRegxMatcher(Pattern p_pattern) {
	super(p_pattern);
	// TODO Auto-generated constructor stub
   }

   public boolean match(String p_str) throws MatchException {
	Matcher l_matcher = this.getPattern().matcher(new InterruptableCharSequence(p_str));
	
	MatchCallable l_matchCallable = new MatchCallable(l_matcher);
	
	Future<Boolean> l_future = getExecutorService().submit(l_matchCallable);
	Boolean l_result = null;
	try {
	   l_result = l_future.get(timeToWait, TimeUnit.MICROSECONDS);
	} catch (InterruptedException ex) {
	   throw new MatchException("unexpected interruption", ex);
	} catch (ExecutionException ex) {
	   throw new MatchException("err during matching", ex.getCause());
	} catch (TimeoutException ex) {
	   l_future.cancel(true);
	   throw new MatchException("timeout");
	}
	
	setMatcher(l_matcher);
	
	return l_result;
   }
}
