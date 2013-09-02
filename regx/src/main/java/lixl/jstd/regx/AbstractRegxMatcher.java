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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 
 */
public abstract class AbstractRegxMatcher implements RegxMatcher {

   private Pattern pattern;
   private Matcher matcher;
   private ExecutorService executorService;
   
   public AbstractRegxMatcher(Pattern p_pattern) {
	pattern = p_pattern;
   }
   
   public AbstractRegxMatcher(Pattern p_pattern, ExecutorService p_exeService) {
	pattern = p_pattern;
	executorService = p_exeService;
   }
   
   protected Pattern getPattern() {
	return pattern;
   }
   
   protected ExecutorService getExecutorService() {
	if (executorService == null) {
	   executorService = Executors.newSingleThreadExecutor();
	}
	
	return executorService;
   }
   
   protected final class MatchCallable implements Callable<Boolean> {

	private Matcher matcher;
	
	public MatchCallable(Matcher p_regxMatcher) {
	   if(p_regxMatcher == null) {
		throw new IllegalArgumentException();
	   }
	   
	   matcher = p_regxMatcher;
	}

	public final Boolean call() throws Exception {
	   return matcher.matches();
	}
   }

   /**
    * @return the matcher
    * 
    */
   public Matcher getMatcher() {
      return this.matcher;
   }

   /**
    * @param p_matcher the matcher to set
    * 
    */
   protected void setMatcher(Matcher p_matcher) {
      this.matcher = p_matcher;
   }
}
