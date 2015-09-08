package sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by xiaoliangl on 9/1/15.
 */
public class Main {
   public static void main(String[] args) throws IOException {
      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

      String[] str = context.getBeanNamesForType(Executor.class);

      for (String str1 : str) {
         System.out.println(str1);
      }

      System.in.read();

   }
}
