package lobby_p;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import game_p.PuyoFrame;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import lobby_p.RankMain_GUI;
import lobby_p.UserList_Main;


public class Lobby_Main_Clear2 extends JFrame implements DDongInter{


	public ClientNetWork cn;
	DDongData dData;

	JTextArea chatArea; // 대화 내용들이 입력되는 창
	JTextField wrArea; // 메세지 입력창	
	JScrollPane js;

	String ttt;

	private JPanel contentPane;


	public Lobby_Main_Clear2() {

		setBounds(300, 50, 1130, 950);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// == 화면 윗부분 =====================

		// -- 방 리스트 -------
		roomBtn();
		//		 -- 방 리스트 끝 -------


		// -- 접속유저 리스트 -------
		JPanel userList = new UserList_Main();
		userList.setBounds(830, 30, 261, 600);
		userList.setBackground(Color.CYAN);
		contentPane.add(userList);
		// -- 접속유저 리스트 끝 -------

		// == 화면 윗부분 끝 =====================



		// == 화면 아랫부분 =====================

		// -- 채팅창 -------
		JPanel chatArea = new ChatUser();
		chatArea.setBounds(30, 640, 782, 228);
		chatArea.setBackground(Color.MAGENTA);
		contentPane.add(chatArea);
		// -- 채팅창 끝 -------


		// -- 랭킹 버튼 -------
		JButton rankBtn = new JButton("랭킹");
		rankBtn.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 16));
		rankBtn.setBounds(830, 640, 261, 228);
		rankBtn.setBackground(Color.PINK);
		contentPane.add(rankBtn);

		rankBtn.addActionListener(new RankBtnAction());
		// -- 랭킹 버튼 끝 -------

		// == 화면 아랫부분 끝 =====================



		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		new LobbyDAO().delete(cn.id); // 클로크로 껐을때 로비리스트 디비에서 삭제되게 수정하기

	}


	void roomBtn() { // 방버튼

		JPanel roomList = new JPanel();
		roomList.setBounds(30,30,780,1200);
		roomList.setLayout(null);
		js = new JScrollPane(roomList);
		js.setBounds(30, 30, 780, 600);
		js.setLayout(null);
		contentPane.add(js);

		ArrayList<JButton> btns = new ArrayList<JButton>();

		for (int i = 1; i < 11; i++) {

			Integer roomN = i;
			int btnX = 0;
			int btnY = 0;

			if(i%3==1) {

				btnY += 200*(i/3);
			}else if(i%3==2) {
				btnX = 260;
				btnY += 200*(i/3);
			}else if(i%3==0) {
				btnX = 520;
				btnY += 200*(i/3-1);
			}

			JButton roombtn = new JButton();
			roombtn.setBounds(btnX, btnY, 260, 200);
			roombtn.setBackground(Color.yellow);
			JLabel btnName = new JLabel();
			btnName.setFont(new Font("맑은고딕",Font.BOLD, 15));
			ttt = "<html>NO. " + roomN + " (0 / 2)<br> >>방만들기 << </html>";
			btnName.setText(ttt);
			roombtn.add(btnName);
			js.add(roombtn);
			btns.add(roombtn);


			// 방이 업데이트 됐을때 작동 될 아이
			if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
					new GameRoomDAO().detailroom(roomN).getUser2()==null) {

				roombtn.setBackground(Color.cyan);
				ttt = "<html>NO. " + roomN + "  (1 / 2)<br> >> 입장하기 << </html>";
				btnName.setText(ttt);

			} else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
					new GameRoomDAO().detailroom(roomN).getUser2()!=null) {

				roombtn.setBackground(Color.MAGENTA);
				ttt = "<html>NO. " + roomN + " (2 / 2)<br>-- 인원이 가득 차 있습니다 --</html>";
				btnName.setText(ttt);
			}


			roombtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					GameRoomDTO dto = new GameRoomDTO();

					dData = new DDongData();
					dData.dst = new ArrayList<String>();

					if(new GameRoomDAO().detailroom(roomN).getUser1()==null &&
							new GameRoomDAO().detailroom(roomN).getUser2()==null) {

						roombtn.setBackground(Color.cyan);
						ttt = "<html>NO. " + roomN + " (1 / 2)<br> >> 입장하기 << </html>";
						btnName.setText(ttt);


						System.out.println(roomN+"여긴 유저가 없는 곳을 눌렀을때");
						dto.setNo(roomN);
						dto.setUser1(cn.id);

						new GameRoomDAO().modifyUser1(dto);

						new LobbyDAO().delete(cn.id);



						dData.type = "게임";
						dData.src = cn.id;
						dData.dst.add(cn.id);
						System.out.println(dData.type+","+dData.dst.size()+"방에 한명일때 dst사이즈");
						cn.send(dData);

//						System.out.println(dData.type.toString()+","+dData.toString()+"여긴한번 눌렀을떄 타입,데이터1");
//						cn.send(dData);

						js.setVisible(false);
						js.setVisible(true);

						PuyoFrame goGame = new PuyoFrame();
						goGame.cn = cn;
						cn.ddInter = goGame;
						dispose();

					} else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
							new GameRoomDAO().detailroom(roomN).getUser2()==null) {

						roombtn.setBackground(Color.MAGENTA);
						ttt = "<html>NO. " + roomN + "(2 / 2)<br>-- 인원이 가득 차 있습니다 --</html>";
						btnName.setText(ttt);

						System.out.println(roomN+"여긴 유저가 1명 있는곳을 눌렀을때");

						dto.setNo(roomN);
						dto.setUser2(cn.id);

						new GameRoomDAO().modifyUser2(dto);

						new LobbyDAO().delete(cn.id);

						dData.type = "게임";
						dData.src = cn.id;
						dData.dst.add(cn.id);
						System.out.println(dData.dst.size()+"방에 두명일때 dst사이즈");
						cn.send(dData);

						js.setVisible(false);
						js.setVisible(true);
						System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄 타입,데이터2");

						PuyoFrame goGame = new PuyoFrame();
						goGame.cn = cn;
						cn.ddInter = goGame;
						dispose();

					} else if(new GameRoomDAO().detailroom(roomN).getUser1()!=null &&
							new GameRoomDAO().detailroom(roomN).getUser2()!=null) {

						System.out.println(roomN+"꽉차 있는곳을 눌렀을때");
						JOptionPane.showMessageDialog(null, "방이 꽉 찼습니다.\n다른방을 이용해 주세요!!");
					}

					dData = new DDongData();
					dData.type = "로비";
					System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄 타입,데이터1");
					cn.send(dData);

				}
			});
		}
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
			rankPop.add(new RankMain_GUI());
			rankPop.setVisible(true);
		}
	}
	// == 랭킹버튼 리스너 끝 ===

	class ChatUser extends JPanel {

		public ChatUser() { // 생성자

			try {

				setBounds(0, 0, 782, 228);
				setLayout(null);

				// chatArea - 대화창
				chatArea = new JTextArea(); // 대화창
				chatArea.setEnabled(false); // 대화가 입력되는 공간이므로 텍스트를 입력하지 못하게 막는다
				JScrollPane js = new JScrollPane(chatArea);
				js.setBounds(0, 0, 782, 188);
				chatArea.setBackground(new Color(250, 250, 250));
				chatArea.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
				chatArea.setForeground(Color.black);
				add(js);
				// == 대화창 swing ==============

				// wrArea - 메세지 입력창
				wrArea = new JTextField();
				wrArea.setBounds(0, 188, 782, 40);
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
							System.out.println("채팅 리셋");

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


	public static void main(String[] args) {
		new Lobby_Main_Clear2();
	}

	@Override
	public void reciver(DDongData dd) {

		if(dd.type.equals("채팅")) {

			System.out.println(dd.data.toString()+" - 롸이트해주는곳인데 데이터타입뭐니");
			chatArea.append("["+dd.src+"]  "+dd.data+"\n"); // 여기서 에러나는디
			// 읽어 온 내용을 대화창에 입력해주고 줄바꿈을 해준다
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			// 스크롤을 맨 마지막 대화쪽에 위치해준다

		} else if(dd.type.equals("로비")) {

			roomBtn();

			//			dData.type = "게임";
			//			dData.src = cn.id;
			//			dData.dst.add(cn.id);
			//			System.out.println(dData.type+","+dData.dst.size()+"방에 한명일때 dst사이즈");
			//			cn.send(dData);
			//
			//			PuyoFrame goGame = new PuyoFrame();
			//			goGame.cn = cn;
			//			cn.ddInter = goGame;
			//			dispose();
		}

	}
}
