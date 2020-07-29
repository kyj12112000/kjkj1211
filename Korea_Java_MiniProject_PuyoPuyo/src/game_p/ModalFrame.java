package game_p;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.LobbyDAO;
import lobby_p.Lobby_Main;

public class ModalFrame extends JFrame implements WindowListener {

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

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 옵션
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 프레임 닫기 비활성화
		setVisible(true); // 프레임을 보여줌

		addWindowListener(this); // 윈도우창을 x로 닫으면 닫히게 한다

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
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 이게 먹히나 ?
		ModalFrame.this.frame.dispose();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		new LobbyDAO().insert(frame.meId);

		frame.updateRoomDb();

		new Lobby_Main(frame.cn);

		DDongData data = new DDongData();
		data.type = "로비";
		data.data = null;
		frame.cn.send(data);

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) { // 강제 종료시.....
		// TODO Auto-generated method stub
		frame.updateRoomDb();

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
