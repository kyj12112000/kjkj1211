package game_p;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.GameRoomDAO;
import jdbc_p.GameRoomDTO;

public class PuyoFrame extends JFrame implements DDongInter, WindowListener {

	public ClientNetWork cn;

	final int width = 806;
	final int height = 679 + 50;

	MePuyoPanel me;
	YouPuyoPanel you;

	JPanel state;

	JLabel meLb, youLb;

	String meId, enemyId;
	Integer roomNum;

	DDongData data;
	MeGameInfo enemyData;

	boolean enemyChk;

	ExecutorService threadPool;

	public PuyoFrame(Integer roomNum, String meId, String enemyId) {
		// TODO Auto-generated constructor stub

		init(roomNum, meId, enemyId);
		setSize(width, height); // 프레임 사이즈
		setLocationRelativeTo(null); // 프레임 시작시 모니터 중앙에 출력
		setResizable(false); // 프레임 사이즈 조절 할 수 없음
		getContentPane().setLayout(null); // 레이2아웃
		setTitle("젤리젤리"); // 타이틀
		setIconImage(new ImageIcon("./img/logo.png").getImage()); // 타이틀바 로고 설정
		getContentPane().setBackground(Color.white);

		me = new MePuyoPanel(this);
		me.setBounds(0, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
		add(me);
		addKeyListener(new ActionKey(me));

		int labelH = 50;
		this.meLb = new JLabel(meId);
		meLb.setBounds(0, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, labelH);
		meLb.setHorizontalAlignment(JLabel.CENTER);
		add(meLb);

		you = new YouPuyoPanel();
		you.setBounds(Puyo.PUYOSIZE * 10, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE * 13);
		add(you);

		this.youLb = new JLabel(enemyId);
		youLb.setBounds(Puyo.PUYOSIZE * 10, Puyo.PUYOSIZE * 13, Puyo.PUYOSIZE * 6, labelH);
		youLb.setHorizontalAlignment(JLabel.CENTER);
		add(youLb);

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 옵션
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true); // 프레임을 보여줌

		addWindowListener(this); // 프레임을 강제 종료시 행동할 이벤트리스너

		update();

	}

	void init(Integer roomNum, String meId, String enemyId) {
		this.roomNum = roomNum;
		this.meId = meId;
		this.enemyId = enemyId;
		this.threadPool = Executors.newCachedThreadPool();
		this.enemyChk = false;

		ddongDataInit();

	}

	void ddongDataInit() {
		this.data = new DDongData();
		data.type = "게임중";
		data.dst = enemyId;

	}

	@Override
	public void reciver(DDongData dd) {
		// TODO Auto-generated method stub;

		if (enemyChk)
			return;

		if (dd.type.equals("게임중")) {

			if ((MeGameInfo) dd.data != null) {
				// enemyChk = dd.chk;
				you.paint((MeGameInfo) dd.data);
				this.enemyData = (MeGameInfo) dd.data;
				if (((MeGameInfo) dd.data).itemChk)
					me.itemChk = true;
			}

		}

	}

	void update() {

		int frame = (33 * 33) * 2;

		Runnable thread = new Runnable() {

			@Override
			public void run() {

				while (true) {

					try {

						if (PuyoFrame.this.enemyData != null) {
							if (me.meInfo.endGame || PuyoFrame.this.enemyData.endGame)
								return;

						}

						// 상대방에게 데이터 를 보내지 않음
						cn.send(data);
						me.meInfo.itemChk = false;
						Thread.sleep(frame);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		this.threadPool.submit(thread);
	}

	void signal() {
		DDongData data = new DDongData();
		data.type = "로비";
		data.data = null;
		cn.send(data);
	}

	void updateRoomDb() {

		GameRoomDTO users = new GameRoomDAO().detailroom(roomNum);

		String user1 = users.getUser1();
		String user2 = users.getUser2();

		String[] userArr = { user1, user2 };

//		System.out.println(me.lobbyChk + "로비첵");
//		System.out.println(me.meInfo.endGame + "앤드게임");
//		System.out.println(this.enemyData.endGame + "에너미 데이터 앤드게임");

		if (me.lobbyChk) { // 둘다 게임이 종료 되어 나갔다면 방 폭파

			for (int i = 0; i < userArr.length; i++) {

				new GameRoomDAO().modifyUser4(new String[] { "user1", "user2" }[i], userArr[i], roomNum);

			}

		} else { // 한명만 나갔다면.... 그 한명은 게임이 진행 되어야 하기 때문에....

			for (int i = 0; i < userArr.length; i++) {

				if (meId.equals(userArr[i])) {
					new GameRoomDAO().modifyUser5(new String[] { "user1", "user2" }[i], userArr[i]); // 방 디비에서 날 삭제
					new GameRoomDAO().modifyUser6(new String[] { "user1", "user2" }[i], roomNum); // 방 디비에 임시유저 진입
				}

			}
			// 방 디비 업데이트 끝
		}

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		// 지금은 게임 진행중인 패널...
		// 강제 종료 했다면 방 디비에서 삭제.... 로비 디비로 보낼 필요는 없음...
		// 두명중 게임이 끝난 사람 기준으로 방을 폭파 해야 하기 때문에 ....

		me.meInfo.endGame = true; // 강제 종료 이기 때문에 나의 게임은 강제적으로 종료

		cn.send(data);

		updateRoomDb(); // 강제 종료시 로비로 가지 않고 바로 게임이 끝남
		signal();

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}