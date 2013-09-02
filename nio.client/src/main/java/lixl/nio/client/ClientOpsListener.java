/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.nio.client;


/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public interface ClientOpsListener {

	void onConnectable(NIOTCPClientSocketWrapper p_clientWrapper);
	void onReadable(NIOTCPClientSocketWrapper p_clientWrapper);
	void onWritable(NIOTCPClientSocketWrapper p_clientWrapper);
}
