package lobby_p;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import game_p.PuyoFrame;
import game_p.WaitRoom;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import jdbc_p.LobbyDTO;
import lobby_p.RankMain_GUI;



public class Lobby_Main extends JFrame implements DDongInter, WindowListener{


   public ClientNetWork cn;
   DDongData dData;

   JTextArea textArea; // 대화 내용들이 입력되는 창
   JTextField wrArea; // 메세지 입력창   
   
   JScrollPane js;
   JPanel roomList; // 방리스트
   String ttt;
   
   JPanel userListA;
   String id;
   JTable lobbyT;
   JScrollPane sp2;
   //   UserList_Main userList;

   private JPanel contentPane;


   void UserList_Main (){

      if(sp2!=null) {
         
         userListA.removeAll();
         lobbyT.removeAll();
         sp2.removeAll();
      }
      
      userListA = new JPanel();
      userListA.setBounds(662, 20, 210, 400);
      userListA.setBackground(Color.magenta);
      userListA.setLayout(null);
      contentPane.add(userListA);
      
      JLabel lobbyLabel = new JLabel("로비");
      lobbyLabel.setBounds(0, 0, 210, 50);
      lobbyLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 20));
      lobbyLabel.setHorizontalAlignment(JLabel.CENTER);
      userListA.add(lobbyLabel);


      ArrayList<LobbyDTO> arr = new LobbyDAO().list();


      Object [] index ={"ID"};
      Object [][] lobbyL = new Object[arr.size()][1]; 


      for (int i =0; i<arr.size();i++) {

         lobbyL[i][0] = arr.get(i).getId();
      }

      lobbyT = new JTable(lobbyL,index);
      lobbyT.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 13));
      sp2 = new JScrollPane(lobbyT);
      sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      sp2.setBounds(0, 50, 210, 350);
      userListA.add(sp2);

   }


   public Lobby_Main(ClientNetWork cn) {

      this.cn = cn;
      cn.ddInter = this;

      setSize(900, 650);
      setLocationRelativeTo(null);
      setTitle("젤리젤리"); // 타이틀
      setIconImage(new ImageIcon("./img/logo.png").getImage());
      contentPane = new JPanel();
      contentPane.setLayout(null);
      setContentPane(contentPane);


      // == 화면 윗부분 =====================
      // -- 방 리스트 -------
      roomBtn();
      //       -- 방 리스트 끝 -------

      // -- 접속유저 리스트 -------
      UserList_Main();
      // -- 접속유저 리스트 끝 -------


      // == 화면 윗부분 끝 =====================



      // == 화면 아랫부분 =====================

      // -- 채팅창 -------
      JPanel chatArea = new ChatUser();
      chatArea.setBounds(25, 430, 618, 175);
      chatArea.setBackground(Color.MAGENTA);
      contentPane.add(chatArea);
      // -- 채팅창 끝 -------


      // -- 랭킹 버튼 -------
      JButton rankBtn = new JButton("랭킹");
      rankBtn.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 16));
      rankBtn.setBounds(662, 430, 210, 100);
      rankBtn.setBackground(Color.PINK);
      contentPane.add(rankBtn);

      rankBtn.addActionListener(new RankBtnAction());
      // -- 랭킹 버튼 끝 -------

      // -- 나가기 버튼 -------
      JButton exitBtn = new JButton("게임종료");
      exitBtn.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 16));
      exitBtn.setBounds(662, 537, 210, 67);
      exitBtn.setBackground(Color.lightGray);
      contentPane.add(exitBtn);

      exitBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {

            new LobbyDAO().delete(cn.id);

            System.exit(0);
            
            dData = new DDongData(); 
            dData.type = "로비";
            cn.send(dData);
         }
      });
      // -- 나가기 버튼 -------



      // == 화면 아랫부분 끝 =====================

      LobbyDAO dao = new LobbyDAO();

      setVisible(true);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //dao.delete(cn.id); // 클로크로 껐을때 로비리스트 디비에서 삭제되게 수정하기

      addWindowListener(this); // 윈도우창을 x로 닫으면 닫히게 한다

      DDongData data = new DDongData();
      data.type = "로비";
      cn.send(data);

   }


   void roomBtn() { // 방버튼


      if(js!=null) {

         roomList.removeAll();
         js.removeAll();
         contentPane.remove(js);
      }

//      userList.init();

      roomList = new JPanel();
      roomList.setPreferredSize(new Dimension(582, 600));
      //      roomList.setBounds(30,30,770,1200);
      js = new JScrollPane(roomList);
      js.setBounds(25, 20, 618, 400);
      roomList.setLayout(null);
      //      js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      //      js.setLayout(null);
      contentPane.add(js);


      for (int i = 1; i <19; i++) {

         Integer roomN = i;
         int btnX = 0;
         int btnY = 0;

         if(i%3==1) {

            btnY += 100*(i/3);
         }else if(i%3==2) {
            btnX = 206;
            btnY += 100*(i/3);
         }else if(i%3==0) {
            btnX = 412;
            btnY += 100*(i/3-1);
         }

         JButton roombtn = new JButton();
         roombtn.setBounds(btnX, btnY, 206, 100);
         roombtn.setBackground(Color.yellow);
         JLabel btnName = new JLabel();
         btnName.setFont(new Font("맑은고딕",Font.BOLD, 14));
         ttt = "<html>NO. " + roomN + " (0 / 2)<br> >>방만들기 << </html>";
         btnName.setText(ttt);
         roombtn.add(btnName);
         roomList.add(roombtn);


         // 방이 업데이트 됐을때 작동 될 아이
         if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
               new GameRoomDAO().detailroom(roomN).getUser2()==null) {

            roombtn.setBackground(Color.cyan);
            ttt = "<html>NO. " + roomN + "  (1 / 2)<br> >> 입장하기</html>";
            btnName.setText(ttt);

         } else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
               new GameRoomDAO().detailroom(roomN).getUser2()!=null) {

            roombtn.setBackground(Color.MAGENTA);
            ttt = "<html>NO. " + roomN + " (2 / 2)<br>- 인원이<br>  가득 차 있습니다 -</html>";
            btnName.setText(ttt);
         }

         //방버튼이 눌렸을때
         roombtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

               GameRoomDTO dto = new GameRoomDTO();

               dData = new DDongData();

               if(new GameRoomDAO().detailroom(roomN).getUser1()==null &&
                     new GameRoomDAO().detailroom(roomN).getUser2()==null) {

                  roombtn.setBackground(Color.cyan);
                  ttt = "<html>NO. " + roomN + " (1 / 2)<br> >> 입장하기 </html>";
                  btnName.setText(ttt);


                  System.out.println(roomN+"여긴 유저가 없는 곳을 눌렀을때");
                  dto.setNo(roomN);
                  dto.setUser1(cn.id);

                  new GameRoomDAO().modifyUser1(dto);

                  new LobbyDAO().delete(cn.id);

                  //                  System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄 타입,데이터1");

                  WaitRoom goGame = new WaitRoom(cn.id,roomN); // 여기서 게임생성
                  goGame.cn = cn;
                  cn.ddInter = goGame;
                  
                  dData = new DDongData(); 
                  dData.type = "게임"; // 
                  cn.send(dData);
                  System.out.println("이야 슈발"+dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄 타입,데이터1");

                  dispose();

               } else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
                     new GameRoomDAO().detailroom(roomN).getUser2()==null) {

                  roombtn.setBackground(Color.MAGENTA);
                  ttt = "<html>NO. " + roomN + "(2 / 2)<br>- 인원이<br>  가득 차 있습니다 -</html>";
                  btnName.setText(ttt);

                  System.out.println(roomN+"여긴 유저가 1명 있는곳을 눌렀을때");

                  dto.setNo(roomN);
                  dto.setUser2(cn.id);

                  new GameRoomDAO().modifyUser2(dto);

                  new LobbyDAO().delete(cn.id);

                  WaitRoom goGame = new WaitRoom(cn.id,roomN); // 여기서 게임생성
                  goGame.cn = cn;
                  cn.ddInter = goGame;

                  dData = new DDongData(); 
                  dData.type = "게임"; // 게임방에서 DB업데이트 시점을 알려주기 위해서 사용
                  cn.send(dData);
                  System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄 타입,데이터1");
                  dispose();

               }else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
                     new GameRoomDAO().detailroom(roomN).getUser2()!=null) {

                  System.out.println(roomN+"꽉차 있는곳을 눌렀을때");
                  JOptionPane.showMessageDialog(null, "방이 꽉 찼습니다.\n다른방을 이용해 주세요!!");
               }

            }
         });
      }

      js.setVisible(false);
      js.setVisible(true);
   }


   // == 랭킹버튼 리스너 ===
   class RankBtnAction implements ActionListener{ // 랭킹 버튼 리스너

      public RankBtnAction() {

      }

      @Override
      public void actionPerformed(ActionEvent e) {

         JFrame rankPop = new JFrame();
         rankPop.setBounds(500, 75, 536, 700);
         rankPop.setTitle("랭킹");
         rankPop.setIconImage(new ImageIcon("./img/logo.png").getImage());
         rankPop.getContentPane().add(new RankMain_GUI());
         rankPop.setVisible(true);
      }
   }
   // == 랭킹버튼 리스너 끝 ===

   class ChatUser extends JPanel {

      public ChatUser() { // 생성자

         try {

            setBounds(0, 0, 600, 100);
            setLayout(null);

            // chatArea - 대화창
            textArea = new JTextArea(); // 대화창
            textArea.setEnabled(false); // 대화가 입력되는 공간이므로 텍스트를 입력하지 못하게 막는다
            JScrollPane js = new JScrollPane(textArea);
            js.setBounds(0, 0, 618, 135);
            textArea.setBackground(new Color(250, 250, 250));
            textArea.setFont(new Font("나눔바른고딕", Font.BOLD, 15));
            textArea.setForeground(Color.black);
            add(js);
            // == 대화창 swing ==============

            // wrArea - 메세지 입력창
            wrArea = new JTextField();
            wrArea.setBounds(0, 135, 618, 40);
            //            wrArea.setBackground(Color.magenta);
            wrArea.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
            add(wrArea);
            // == 메세지 입력창 swing ==============

            wrArea.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent e) {

                  try {

                     dData = new DDongData();
                     dData.type = "채팅";
                     dData.data = wrArea.getText();
                     cn.send(dData);

                     wrArea.setText(""); // 메세지를 입력후 메세지 창을 비워준다
                     wrArea.setFocusable(true); // 커서도 맨앞으로 보내준다

                  } catch (Exception e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                  }
               }
            });

         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      } // == ChatUser_test() 끝 =====================
   }


   @Override
   public void reciver(DDongData dd) {

      System.out.println("Tq 리씨버 받는다" + dd);

      if(dd.type.equals("채팅")) {

         System.out.println(dd.data.toString()+" - 롸이트해주는곳인데 데이터타입뭐니");
         textArea.append(" ["+dd.src+"]  "+dd.data+"\n"); // 여기서 에러나는디
         // 읽어 온 내용을 대화창에 입력해주고 줄바꿈을 해준다
         textArea.setCaretPosition(textArea.getDocument().getLength());
         // 스크롤을 맨 마지막 대화쪽에 위치해준다

      } else if(dd.type.equals("로비")||dd.type.equals("게임")) {
         
         System.out.println("여기는 로비");
         UserList_Main();
         roomBtn();
      }

   }


   @Override
   public void windowActivated(WindowEvent arg0) {
      // TODO Auto-generated method stub

   }


   @Override
   public void windowClosed(WindowEvent e) {
      // TODO Auto-generated method stub

   }


   @Override
   public void windowClosing(WindowEvent e) {
      // TODO Auto-generated method stub

      dispose();
      new LobbyDAO().delete(cn.id);
      dData = new DDongData(); 
      dData.type = "로비";
      cn.send(dData);
   }


   @Override
   public void windowDeactivated(WindowEvent e) {
      // TODO Auto-generated method stub

   }


   @Override
   public void windowDeiconified(WindowEvent e) {
      // TODO Auto-generated method stub

   }


   @Override
   public void windowIconified(WindowEvent e) {
      // TODO Auto-generated method stub

   }


   @Override
   public void windowOpened(WindowEvent e) {
      // TODO Auto-generated method stub

   }
}