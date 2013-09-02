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

/**
 *
 * 
 */
public final class InterruptableCharSequence implements CharSequence {

   private final String internal;
   
   public InterruptableCharSequence(String p_str) {
	if(p_str == null) {
	   throw new IllegalArgumentException();
	}
	
	internal = p_str.substring(0);
   }
   
   public final int length() {
	return internal.length();
   }

   public final char charAt(int p_index) {
	if(Thread.interrupted()) {
	   throw new RuntimeException();
	}
	
	return internal.charAt(p_index);
   }

   public CharSequence subSequence(int p_start, int p_end) {
	return internal.subSequence(p_start, p_end);
   }

}
