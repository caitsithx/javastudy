/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.commons;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class NIOUtil {

	public static<sk extends NIOSocketWrapper> void selectFor(sk p_wrapper, NIOOpsListener<sk> p_listener) throws IOException {
		p_wrapper.getSelector().select();
		
		for (Iterator<SelectionKey> iterator = p_wrapper.getSelector().selectedKeys().iterator(); iterator.hasNext();) {
			SelectionKey l_selectedKey = iterator.next();
			iterator.remove();
			if(!l_selectedKey.isValid()) {
				continue;
			}
			
			if (l_selectedKey.isWritable()) {
				p_listener.onWritable(p_wrapper, l_selectedKey);
			} else if (l_selectedKey.isReadable()) {
				p_listener.onReadable(p_wrapper, l_selectedKey);
			} else if (l_selectedKey.isConnectable()) {
				p_listener.onConnectable(p_wrapper);
			} else if(l_selectedKey.isAcceptable()) {
				p_listener.onAcceptable(p_wrapper);
			}
		}
	}

}
