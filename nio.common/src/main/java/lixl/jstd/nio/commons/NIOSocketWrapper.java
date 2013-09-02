/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.commons;

import java.nio.channels.Selector;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public interface NIOSocketWrapper {

	Selector getSelector();
	
	void selectFor();
}
