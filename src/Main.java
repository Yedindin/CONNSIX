import java.awt.Point;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
public class Main {
   
   public static void main(String[] args) throws Exception {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Input the ip address > ");
      String ip = scanner.nextLine();
      System.out.print("Input the port number > ");
      int port = Integer.parseInt(scanner.nextLine());
      System.out.print("Input the color > ");
      String color = scanner.nextLine();
      
      ConnectSix conSix = new ConnectSix(ip, port, color);
      DummyAI dummyAI = new DummyAI(conSix, color);
   }
}