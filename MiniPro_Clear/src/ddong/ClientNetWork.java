package ddong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientNetWork {

   ObjectOutputStream oos;
   ObjectInputStream ois;

   public String id;

   public DDongInter ddInter;
   public Resiver resiver;

   public ClientNetWork(String id) {

      try {

         this.id = id;

         System.out.println(InitData.ip);
         System.out.println(InetAddress.getLocalHost().getHostAddress());

         Socket soc = new Socket(InitData.ip, 7777);
         oos = new ObjectOutputStream(soc.getOutputStream());
         DDongData ddos = new DDongData();
         ddos.src = id;
         ddos.data = "안뇽";
         ddos.type = "login";
         ddos.chk = false;
         oos.writeObject(ddos);
         oos.flush();
         oos.reset();
         System.out.println("로그인하고 cn");
         System.out.println(ddos + "로그인하고 ddos");
         resiver = new Resiver(soc);
         resiver.start();

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void send(DDongData ddos) {

      try {

         ddos.src = id;

         oos.writeObject(ddos);
         oos.flush();
         oos.reset();

         System.out.println("전송잘돼요");

      } catch (Exception e) {
      }
   }

   public class Resiver extends Thread {

      public Resiver(Socket soc) {

         try {
            ois = new ObjectInputStream(soc.getInputStream());
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

      @Override
      public void run() {

         while (ois != null) {
            try {
               DDongData data = (DDongData) ois.readObject(); // 여기서 에러나서 리시브가 안된다
               
               ddInter.reciver(data);

            } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }
}