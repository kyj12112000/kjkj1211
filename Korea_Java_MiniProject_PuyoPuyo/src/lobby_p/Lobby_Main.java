package lobby_p;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

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
import javax.swing.WindowConstants;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import game_p.WaitRoomFrame;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import jdbc_p.LobbyDTO;

public class Lobby_Main extends JFrame implements DDongInter, WindowListener {

	public ClientNetWork cn;
	DDongData dData;

	JTextArea textArea; // 대화 내용들이 입력되는 창
	JTextField wrArea; // 메세지 입력창

	JScrollPane js;
	JPanel roomList; // 방리스트
	String ttt;

	UserList_Main userList;

	private JPanel contentPane;

	public Lobby_Main(ClientNetWork cn) {

		this.cn = cn;
		cn.ddInter = this;

		setSize(806, 700);
		setLocationRelativeTo(null);
		setTitle("젤리젤리"); // 타이틀
		setIconImage(new ImageIcon("./img/logo.png").getImage());
		contentPane = new JPanel();
		contentPane.setBackground(Color.white);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// == 화면 윗부분 =====================

		// -- 접속유저 리스트 -------
		userList = new UserList_Main();
		userList.setBounds(605, 20, 180, 420);
		// -- 접속유저 리스트 끝 -------

		// -- 방 리스트 -------
		roomBtn();
		// -- 방 리스트 끝 -------

		// -- 접속유저 리스트 -------
		contentPane.add(userList);
		// -- 접속유저 리스트 끝 -------

		// == 화면 윗부분 끝 =====================

		// == 화면 아랫부분 =====================

		// -- 채팅창 -------
		JPanel chatArea = new ChatUser();
		chatArea.setBounds(20, 455, 570, 200);
		chatArea.setBackground(Color.MAGENTA);
		contentPane.add(chatArea);
		// -- 채팅창 끝 -------

		// -- 랭킹 버튼 -------
		JButton rankBtn = new JButton("랭킹");
		rankBtn.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 16));
		rankBtn.setBounds(605, 455, 180, 115);
		rankBtn.setBackground(Color.PINK);
		contentPane.add(rankBtn);

		rankBtn.addActionListener(new RankBtnAction());
		// -- 랭킹 버튼 끝 -------

		// -- 나가기 버튼 -------
		JButton exitBtn = new JButton("게임종료");
		exitBtn.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 16));
		exitBtn.setBounds(605, 579, 180, 75);
		exitBtn.setBackground(Color.lightGray);
		contentPane.add(exitBtn);

		exitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new LobbyDAO().delete(cn.id);

				dData = new DDongData();
				dData.type = "로비";
				cn.send(dData);

				System.exit(0);
			}
		});
		// -- 나가기 버튼 -------

		// == 화면 아랫부분 끝 =====================

		LobbyDAO dao = new LobbyDAO();

		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

//      addWindowListener(this);
	}

	void roomBtn() { // 방버튼

		if (js != null) {

			roomList.removeAll();
			js.removeAll();
			contentPane.remove(js);
		}

		userList.init();

		roomList = new JPanel();
		roomList.setPreferredSize(new Dimension(550, 840));
		// roomList.setBounds(30,30,770,1200);
		js = new JScrollPane(roomList);
		js.setBounds(20, 20, 570, 420);
		roomList.setLayout(null);
		contentPane.add(js);

		for (int i = 1; i < 19; i++) {

			Integer roomN = i;
			int btnX = 0;
			int btnY = 0;

			if (i % 3 == 1) {

				btnY += 140 * (i / 3);
			} else if (i % 3 == 2) {
				btnX = 184;
				btnY += 140 * (i / 3);
			} else if (i % 3 == 0) {
				btnX = 368;
				btnY += 140 * (i / 3 - 1);
			}

			JButton roombtn = new JButton();
			roombtn.setBounds(btnX, btnY, 184, 140);
			roombtn.setBackground(Color.yellow);
			JLabel btnName = new JLabel();
			btnName.setFont(new Font("돋움", Font.BOLD, 14));
			ttt = "<html>NO. " + roomN + " (0 / 2)<br><br> >> 방만들기 << </html>";
			btnName.setText(ttt);
			roombtn.add(btnName);
			roomList.add(roombtn);

			// 방이 업데이트 됐을때 작동 될 아이
			if (new GameRoomDAO().detailroom(roomN).getUser1() != null
					&& new GameRoomDAO().detailroom(roomN).getUser2() == null) {

				roombtn.setBackground(Color.cyan);
				ttt = "<html>NO. " + roomN + "  (1 / 2)<br><br> >> 입장하기</html>";
				btnName.setText(ttt);

			} else if (new GameRoomDAO().detailroom(roomN).getUser1() != null
					&& new GameRoomDAO().detailroom(roomN).getUser2() != null) {

				roombtn.setBackground(Color.MAGENTA);
				ttt = "<html>NO. " + roomN + " (2 / 2)<br><br> - 입장불가 - </html>";
				btnName.setText(ttt);
			} else if (new GameRoomDAO().detailroom(roomN).getUser1() == null
					&& new GameRoomDAO().detailroom(roomN).getUser2() != null) {

				roombtn.setBackground(Color.cyan);
				ttt = "<html>NO. " + roomN + "  (1 / 2)<br><br> >> 입장하기</html>";
				btnName.setText(ttt);
			}

			// 방버튼이 눌렸을때
			roombtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					GameRoomDTO dto = new GameRoomDTO();

					dData = new DDongData();

					if (new GameRoomDAO().detailroom(roomN).getUser1() == null
							&& new GameRoomDAO().detailroom(roomN).getUser2() == null) {

						roombtn.setBackground(Color.cyan);
						ttt = "<html>NO. " + roomN + " (1 / 2)<br><br> >> 입장하기 </html>";
						btnName.setText(ttt);

						dto.setNo(roomN);
						dto.setUser1(cn.id);

						new GameRoomDAO().modifyUser1(dto);

						new LobbyDAO().delete(cn.id);

						// System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄
						// 타입,데이터1");

						WaitRoomFrame goGame = new WaitRoomFrame(cn.id, roomN); // 여기서 게임생성
						goGame.cn = cn;
						cn.ddInter = goGame;

						dData = new DDongData();
						dData.type = "게임"; //
						cn.send(dData);

						dispose();

					} else if (new GameRoomDAO().detailroom(roomN).getUser1() != null
							&& new GameRoomDAO().detailroom(roomN).getUser2() == null) {

						roombtn.setBackground(Color.MAGENTA);
						ttt = "<html>NO. " + roomN + "(2 / 2)<br><br> - 입장불가 - </html>";
						btnName.setText(ttt);

						dto.setNo(roomN);
						dto.setUser2(cn.id);

						new GameRoomDAO().modifyUser2(dto);

						new LobbyDAO().delete(cn.id);

						WaitRoomFrame goGame = new WaitRoomFrame(cn.id, roomN); // 여기서 게임생성
						goGame.cn = cn;
						cn.ddInter = goGame;

						dData = new DDongData();
						dData.type = "게임"; // 게임방에서 DB업데이트 시점을 알려주기 위해서 사용
						cn.send(dData);

						dispose();

					} else if (new GameRoomDAO().detailroom(roomN).getUser1() == null
							&& new GameRoomDAO().detailroom(roomN).getUser2() != null) {

						roombtn.setBackground(Color.MAGENTA);
						ttt = "<html>NO. " + roomN + "(2 / 2)<br><br> - 입장불가 - </html>";
						btnName.setText(ttt);

						dto.setNo(roomN);
						dto.setUser1(cn.id);

						new GameRoomDAO().modifyUser1(dto);

						new LobbyDAO().delete(cn.id);

						// System.out.println(dData.type.toString()+","+dData.toString()+"여긴 두번 눌렀을떄
						// 타입,데이터1");

						WaitRoomFrame goGame = new WaitRoomFrame(cn.id, roomN); // 여기서 게임생성
						goGame.cn = cn;
						cn.ddInter = goGame;

						dData = new DDongData();
						dData.type = "게임"; //
						cn.send(dData);

						dispose();

					} else if (new GameRoomDAO().detailroom(roomN).getUser1() != null
							&& new GameRoomDAO().detailroom(roomN).getUser2() != null) {

						JOptionPane.showMessageDialog(null, "방이 꽉 찼습니다.\n다른방을 이용해 주세요!!");
					}

				}
			});
		}

		js.setVisible(false);
		js.setVisible(true);
	}

	class UserList_Main extends JPanel {

		String id;
		JTable lobbyT;
		JScrollPane sp2;

		public UserList_Main() {

			setBounds(0, 0, 180, 420);
			setBackground(Color.white);
			setLayout(null);

			JLabel lobbyList = new JLabel("로비 접속 유저");
			lobbyList.setBounds(0, 0, 180, 25);
			lobbyList.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 20));
			lobbyList.setHorizontalAlignment(JLabel.CENTER);
			add(lobbyList);
		}

		void init() {

			if (sp2 != null) {

				lobbyT.removeAll();
				sp2.removeAll();
			}

			ArrayList<LobbyDTO> arr = new LobbyDAO().list();

			Object[] index = { "ID" };
			Object[][] lobbyL = new Object[arr.size()][1];

			for (int i = 0; i < arr.size(); i++) {

				lobbyL[i][0] = arr.get(i).getId();
			}

			lobbyT = new JTable(lobbyL, index);
			lobbyT.setEnabled(false);
			lobbyT.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 13));
			sp2 = new JScrollPane(lobbyT);
			sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp2.setBounds(0, 30, 180, 390);
			add(sp2);

		}
	}

	// == 랭킹버튼 리스너 ===
	class RankBtnAction implements ActionListener { // 랭킹 버튼 리스너

		public RankBtnAction() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {

			JFrame rankPop = new JFrame();
			rankPop.setSize(520, 570);
			rankPop.setLocationRelativeTo(null);
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

				setBounds(0, 0, 570, 100);
				setLayout(null);

				// chatArea - 대화창
				textArea = new JTextArea(); // 대화창
				textArea.setEnabled(false); // 대화가 입력되는 공간이므로 텍스트를 입력하지 못하게 막는다
				JScrollPane js = new JScrollPane(textArea);
				js.setBounds(0, 0, 570, 165);
				textArea.setBackground(new Color(250, 250, 250));
				textArea.setFont(new Font("돋움", Font.BOLD, 15));
				textArea.setForeground(Color.black);
				add(js);
				// == 대화창 swing ==============

				// wrArea - 메세지 입력창
				wrArea = new JTextField();
				wrArea.setBounds(0, 165, 570, 35);
				// wrArea.setBackground(Color.magenta);
				wrArea.setFont(new Font("돋움", Font.BOLD, 16));
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

		if (dd.type.equals("채팅")) {

			textArea.append(" [" + dd.src + "]  " + dd.data + "\n"); // 여기서 에러나는디
			// 읽어 온 내용을 대화창에 입력해주고 줄바꿈을 해준다
			textArea.setCaretPosition(textArea.getDocument().getLength());
			// 스크롤을 맨 마지막 대화쪽에 위치해준다

		} else if (dd.type.equals("로비") || dd.type.equals("게임")) {

			if (dd.dst == null)
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