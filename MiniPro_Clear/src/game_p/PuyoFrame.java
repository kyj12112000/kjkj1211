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
			if (((MeGameInfo) dd.data).itemChk) {
				System.out.println("아이템 receive : " + ((MeGameInfo) dd.data).itemChk);
				addItem();
			} else {
				you.paint((MeGameInfo) dd.data);
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

						if (me.meInfo.itemChk) {
							cn.send(data);
							me.meInfo.itemChk = false;
						}

						cn.send(data);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		this.threadPool.submit(thread);
	}

//	void item() { // 상대방이 아이템을 터트려 true 가 되었을때 내 정보를 업데이트 한다
//
//		// all stop 부분을 찾아 아이템을 추가
//
//		// me.puyoLbs => 라벨만 담겨 있는 뿌요 => 여기만 업데이트 해주면 meinfo 에 들어가나 확인
//
//		addItem();
//
//	}

	void addItem() {

		ArrayList<MyLabel> puyoLbs = positionUpdate(me.puyoLbs);

		int x = 0;
		int y = Puyo.PUYOSIZE * 12;
		for (int i = 0; i < 6; i++) {

			MyLabel lb = new MyLabel(new ImageIcon("./img/nuisance-48.png"));
			lb.setBounds(x, y, Puyo.PUYOSIZE, Puyo.PUYOSIZE);
			puyoLbs.add(lb);
			x += Puyo.PUYOSIZE;

		}

		reInit(puyoLbs);

	}

	ArrayList<MyLabel> positionUpdate(ArrayList<MyLabel> puyoLbs) { // 한칸씩 위로 올려줍니다.

		ArrayList<MyLabel> result = new ArrayList<MyLabel>(puyoLbs);

		for (MyLabel myLabel : result) {

			int x = myLabel.getX();
			int y = myLabel.getY() + Puyo.PUYOSIZE;

			myLabel.setLocation(x, y);

		}

		return result;

	}

	void reInit(ArrayList<MyLabel> puyoLbs) {

		boolean meStopChk = me.me.stopChk;
		boolean youStopChk = me.you.stopChk;

		me.me.stopChk = true;
		me.you.stopChk = true;

		removeComponent(me.puyoLbs);

		// 아이템이 추가된 배열로 바꿔치기
		me.puyoLbs = puyoLbs;

		paint(puyoLbs);

		System.out.println("바꿔치기 작업중 [원본] : " + me.puyoLbs);
		System.out.println("바꿔치기 작업중 [바뀐 어레이] : " + puyoLbs);

		// 업데이트 후 원상 복구
		me.me.stopChk = meStopChk;
		me.you.stopChk = youStopChk;

	}

	void removeComponent(ArrayList<MyLabel> lbs) {

		for (JLabel puyoLb : lbs) {
			me.remove(puyoLb);
		}

		setVisible(false);
		setVisible(true);

	}

	void paint(ArrayList<MyLabel> lbs) {

		// 그려주기 작업 -- allStop 되었을 시점에 그려 주어야함....

		for (MyLabel myLabel : lbs) {
			me.add(myLabel);
		}

		// 정보도 업데이트
		me.updateInfo();

		setVisible(false);
		setVisible(true);

	}

	class ExitBtn implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			PuyoFrame.this.dispose();

		}

	}

	public static void main(String[] args) {
		new PuyoFrame("asd", "asdasdasd");
	}

}