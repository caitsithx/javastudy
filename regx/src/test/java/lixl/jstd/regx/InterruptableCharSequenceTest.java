package lixl.jstd.regx;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

// -------------------------------------------------------------------------
//
// Project name: regx
//
// Platform : Java virtual machine
// Language : JAVA 6.0
//
// Original author: lixl
// -------------------------------------------------------------------------

/**
 *
 * 
 */
public class InterruptableCharSequenceTest {

   @Test
   public void testInterrupt() {
	Pattern l_pattern = Pattern.compile("a*b");
	Matcher l_matcher = l_pattern.matcher(new InterruptableCharSequence("ab"));
	
	Assert.assertTrue(l_matcher.matches());
   }
   
   
   @Test
   public void testInterrupt1() throws MatchException {
	Pattern l_pattern = Pattern.compile("(a*)*b");
	Matcher l_matcher = l_pattern.matcher(new InterruptableCharSequence("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
	
	Assert.assertFalse(l_matcher.matches());
	
	Callable<Boolean> l_matchCallable = new Callable<Boolean>() {
	   
	   Matcher matcher;
	   public Boolean call() throws Exception {
		return matcher.matches();
	   }
	};
	
	Future<Boolean> l_future = Executors.newSingleThreadExecutor().submit(l_matchCallable);
	Boolean l_result = null;
	try {
	   l_result = l_future.get(1000, TimeUnit.MICROSECONDS);
	} catch (InterruptedException ex) {
	   throw new MatchException("unexpected interruption", ex);
	} catch (ExecutionException ex) {
	   throw new MatchException("err during matching", ex.getCause());
	} catch (TimeoutException ex) {
	   l_future.cancel(true);
	   throw new MatchException("timeout");
	}
	
	return l_matcher;
   }   
}
