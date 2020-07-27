package game_p;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import lobby_p.Lobby_Main;

public class WaitRoom extends JFrame implements DDongInter {

	final int width = 806;
	final int height = 679 + 50;

	boolean meChk, youChk;

	public ClientNetWork cn;
	JToggleButton ready;
	WaitRoomPanel meP, youP;
	JPanel state;
	JLabel meLb, youLb;

	public Integer roomNum;
	public String id;
	String enenmy;

	DDongData data;

	ExecutorService threadPool;

	public WaitRoom(String id, Integer roomNum) {
		// TODO Auto-generated constructor stub

		init(id, roomNum);

		setSize(width, height); // 프레임 사이즈
		setLocationRelativeTo(null); // 프레임 시작시 모니터 중앙에 출력
		setResizable(false); // 프레임 사이즈 조절 할 수 없음
		getContentPane().setLayout(null); // 레이2아웃
		setTitle("젤리젤리"); // 타이틀
		setIconImage(new ImageIcon("./img/logo.png").getImage()); // 타이틀바 로고 설정
		getContentPane().setBackground(Color.white);

		meP = new WaitRoomPanel();
		meP.setBounds(0, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
		meP.setBackground(Color.black);
		add(meP);

		int labelH = 50;
		this.meLb = new JLabel("ME");
		meLb.setBounds(0, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, labelH);
		meLb.setHorizontalAlignment(JLabel.CENTER);
		meLb.setText(id);
		add(meLb);

		state = new JPanel();
		state.setLayout(null);
		state.setBounds(Puyo.PUYOSIZE * 6, 0, Puyo.PUYOSIZE * 4, Puyo.PUYOSIZE * 13);
		state.setBackground(Color.white);
		add(state);

		ready = new JToggleButton("준비");
		JButton exit = new JButton("나가기");

		// 버튼 간격 50;
		// 버튼 총 길이 130;
		// 남은 길이 520;
		// 520 - 130 / 2
		int btnSizeW = 100;
		int btnSizeH = 40;
		int x = (state.getSize().width - btnSizeW) / 2;
		// System.out.println(x);
		int gap = 50;
		int y = (state.getSize().height - (btnSizeH * 2)) / 2;
		// System.out.println(y);

		// ready.setBackground(null);
		ready.setBorderPainted(false);
		// ready.setBorder(null);

		exit.setBorderPainted(false);

		ready.setBounds(x, y, btnSizeW, btnSizeH);
		exit.setBounds(x, y + gap, btnSizeW, btnSizeH);

		ready.setEnabled(false);

		ready.addActionListener(new ReadyBtn());
		exit.addActionListener(new ExitBtn());

		state.add(ready);
		state.add(exit);

		youP = new WaitRoomPanel();
		youP.setBounds(Puyo.PUYOSIZE * 10, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
		youP.setBackground(Color.green);
		add(youP);

		this.youLb = new JLabel("다른유저의 접속대기");
		youLb.setBounds(Puyo.PUYOSIZE * 10, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, labelH);
		youLb.setHorizontalAlignment(JLabel.CENTER);
		add(youLb);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	void init(String id, Integer roomNum) {
		this.id = id;
		this.roomNum = roomNum;
		this.meChk = false;
		this.threadPool = Executors.newCachedThreadPool();

		ddongDataInit();
	}

	void ddongDataInit() {
		this.data = new DDongData(); // 똥 양식 셋팅
		data.type = "게임";

	}

	@Override
	public void reciver(DDongData dd) {
		// TODO Auto-generated method stub

		System.out.println(this.enenmy);

		if (dd.type.equals("게임")) {

			searchUser();

			if (this.enenmy != null && dd.src.equals(enenmy)) { // 적이 있으면서 적의 정보를 받을때
				System.out.println("여기 나와요 ?");
				this.youChk = dd.chk;

			}
			match();

		}

	}

	void searchUser() { // 통신이 왔을때 적 찾기

		GameRoomDTO users = new GameRoomDAO().detailroom(WaitRoom.this.roomNum); // 방번호로 디비에 접근

		String user1 = users.getUser1();
		String user2 = users.getUser2();

		System.out.println("나 : " + id);

		String[] userArr = { user1, user2 };

		for (String user : userArr) {

			if (user != null) {
				if (!id.equals(user)) {
					System.out.println(" 적 : " + user);
					this.enenmy = user;
					youLb.setText(enenmy);
					this.ready.setEnabled(true);
				}
			} else { // null 이 한번 이라도 있다면
				System.out.println(" 적 : " + user);
				this.enenmy = null;
				youLb.setText("다른유저의 접속대기");
				this.ready.setEnabled(false);
			}

		}

	}

	void match() { // 준비 버튼을 눌렀을때 통신을 보내고 통신이 왔을때 적의 상태 확인

		if (this.meChk && youChk) {

			this.dispose();

			PuyoFrame game = new PuyoFrame(roomNum, id, enenmy);
			game.cn = cn;
			cn.ddInter = game;

			data.type = "게임중";
			data.dst = enenmy;
			cn.send(data);

		}

	}

	class ReadyBtn implements ActionListener { // 준비 버튼

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if (!WaitRoom.this.meChk) {
				WaitRoom.this.meChk = true;
				data.chk = true;
				cn.send(data);
			} else {
				WaitRoom.this.meChk = false;
				data.chk = false;
				cn.send(data);
			}

		}

	}

	class ExitBtn implements ActionListener { // 나가기 버튼 send 를 해서 버튼 활성화와 비활성화를 나눠 줘야지

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			// 로비 디비에 방에서 나간 유저 추가
			new LobbyDAO().insert(id);
			// 로비 업뎃 끝

			// 디비에서 방에서 나간 유저 삭제
			GameRoomDTO users = new GameRoomDAO().detailroom(WaitRoom.this.roomNum);

			String user1 = users.getUser1();
			String user2 = users.getUser2();

			String[] userArr = { user1, user2 };

			for (int i = 0; i < userArr.length; i++) {

				if (id.equals(userArr[i])) {
					new GameRoomDAO().modifyUser5(new String[] { "user1", "user2" }[i], userArr[i]);
					break;
				}

			}
			// 방 디비 업데이트 끝

			// 방 화면 종료
			WaitRoom.this.dispose();

			// 로비로 화면으로 ....
			new Lobby_Main(cn);

			cn.send(data);

		}

	}

}