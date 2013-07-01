/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lixl.nio.server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author lixl
 */
public class Applicative {
    final static Charset UTF8 = Charset.forName("UTF-8");
    void onRequest(ByteBuffer p_requestData) {
        System.out.println(UTF8.decode(p_requestData));
    }
    
    ByteBuffer getResponse() {
        return UTF8.encode("I am the server");
    }
}
