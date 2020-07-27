package game_p;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;
import jdbc_p.LobbyDAO;
import lobby_p.Lobby_Main;

public class ModalFrame extends JFrame implements DDongInter {

	ClientNetWork cn;

	PuyoFrame frame;

	int width, height, second;

	JLabel textLb2;

	ExecutorService threadPool;

	public ModalFrame(PuyoFrame frame, String result) {
		// TODO Auto-generated constructor stub

		init(frame);

		setSize(width, height); // 프레임 사이즈
		setLocationRelativeTo(null); // 프레임 시작시 모니터 중앙에 출력
		setResizable(false); // 프레임 사이즈 조절 할 수 없음
		getContentPane().setLayout(new GridLayout(2, 1)); // 레이2아웃
		setTitle("게임 종료");
		setIconImage(new ImageIcon("./img/logo.png").getImage()); // 타이틀바 로고 설정
		getContentPane().setBackground(Color.white);

		JLabel textLb = new JLabel(frame.enemyId + " 와의 승부에서 " + result);
		textLb.setHorizontalAlignment(JLabel.CENTER);
		add(textLb);

		this.textLb2 = new JLabel();
		textLb2.setHorizontalAlignment(JLabel.CENTER);
		add(textLb2);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 옵션
		setVisible(true); // 프레임을 보여줌

		endTimer();

	}

	void init(PuyoFrame frame) {

		this.frame = frame;
		this.width = 400;
		this.height = 200;
		this.second = 5;
		this.threadPool = Executors.newCachedThreadPool();

	}

	void endTimer() {

		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (second >= 0) {

					try {
						ModalFrame.this.textLb2.setText(second + "초 후 로비로 이동됩니다.");
						Thread.sleep(1000);
						second--;
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				goLobby();
			}
		};
		this.threadPool.submit(thread);
	}

	void goLobby() {

		ModalFrame.this.dispose();
		ModalFrame.this.frame.dispose();

		new LobbyDAO().insert(frame.meId);

		if (frame.me.lobbyChk) { // 둘다 게임이 종료 되어 나갔다면 방 폭파
			GameRoomDTO users = new GameRoomDAO().detailroom(frame.roomNum);

			String user1 = users.getUser1();
			String user2 = users.getUser2();

			String[] userArr = { user1, user2 };

			for (int i = 0; i < userArr.length; i++) {

				new GameRoomDAO().modifyUser5(new String[] { "user1", "user2" }[i], userArr[i]);

			}

		}

		new Lobby_Main(cn);

		DDongData data = new DDongData();
		data.type = "로비";
		data.data = null;
		cn.send(data);

	}

	@Override
	public void reciver(DDongData dd) {
		// TODO Auto-generated method stub

	}

}
