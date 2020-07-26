package game_p;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class PuyoFrame extends JFrame implements DDongInter {

	public ClientNetWork cn;

	final int width = 806;
	final int height = 679 + 50;

	MePuyoPanel me;
	YouPuyoPanel you;

	JPanel state;

	JLabel meLb, youLb;

	String meId, enemyId;

	DDongData data;

	ExecutorService threadPool;

	public PuyoFrame(String meId, String enemyId) {
		// TODO Auto-generated constructor stub

		init(meId, enemyId);

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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 옵션
		setVisible(true); // 프레임을 보여줌

		update();

	}

	void init(String meId, String enemyId) {
		this.meId = meId;
		this.enemyId = enemyId;
		this.threadPool = Executors.newCachedThreadPool();

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

		if (dd.type.equals("게임중")) {
			if ((MeGameInfo) dd.data != null) {
				you.paint((MeGameInfo) dd.data);

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

						Thread.sleep(frame);

						cn.send(data);
						me.meInfo.itemChk = false;

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		this.threadPool.submit(thread);
	}

	class ExitBtn implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			PuyoFrame.this.dispose();

		}

	}

}