/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.commons;

import java.nio.channels.SelectionKey;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public interface NIOOpsListener<T extends NIOSocketWrapper> {

	void onConnectable(T p_clientWrapper);
	void onReadable(T p_clientWrapper, SelectionKey p_selectedKey);
	void onWritable(T p_clientWrapper, SelectionKey p_selectedKey);
	void onAcceptable(T p_socketWrapper);
}
