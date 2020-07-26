package game_p;

import java.awt.Frame;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ddong.ClientNetWork;
import ddong.DDongData;
import ddong.DDongInter;
import jdbc_p.RankDAO;
import jdbc_p.RankDTO;

public class MePuyoPanel extends JPanel {

	String id;

	ImageIcon background;

	MeGameInfo meInfo;
	PuyoGameInfo info;

	Puyo me;
	Puyo you;

	MyLabel meLb;
	MyLabel youLb;

	ArrayList<MyLabel> puyoLbs;
	HashSet<MyLabel> bombArr;
	HashMap<String, HashSet<MyLabel>> map;

	int step, cutLine, score, jum, combo, comboCnt, comboChk; // �ѿ䰡 ���������� ĭ��

	boolean bombChk;

	PuyoTimer timer;

	PuyoFrame frame;

	public ExecutorService threadPool;

	public MePuyoPanel(PuyoFrame frame) {
		// TODO Auto-generated constructor stub

		init(frame);

		// TODO Auto-generated constructor stub
		this.background = new ImageIcon("./img/background.png");
		setLayout(null);

		info = new PuyoGameInfo();
		info.setBounds(0, 0, Puyo.PUYOSIZE * 6, Puyo.PUYOSIZE);
		add(info);

		createPuyo(); // �ѿ���� ��ŸƮ
		timer.start();

	}

	void init(PuyoFrame frame) {

		// �г��� �����Ǿ� add �Ǿ��ٴ°��� ������ ������ �ǹ�

		this.id = frame.meId;
		this.frame = frame;
		this.threadPool = Executors.newCachedThreadPool(); // ������ Ǯ �ʱ�ȭ
		this.meInfo = new MeGameInfo(); // ���� ���� Ŭ���� ����
		this.step = 3; // �ѿ䰡 ���������� ĭ��
		this.puyoLbs = new ArrayList<MyLabel>();
		this.map = new HashMap<String, HashSet<MyLabel>>();
		this.bombArr = new HashSet<MyLabel>();
		this.bombChk = false;
		this.score = 0;
		this.jum = 0;
		this.combo = 0;
		this.comboCnt = 0;
		timer = new PuyoTimer(this);

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(background.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}

	void createPuyo() { // �ѿ��� ������� ������ ���Ͽ� ������ ���

		int LocationX = (Puyo.PUYOSIZE * 6) / 2; // �ѿ���� ���� ��ġ�� ��Ʊ����� x
		int LocationY = -Puyo.PUYOSIZE; // �ѿ���� ���� ��ġ�� ��Ʊ����� y

		Runnable thread = new Runnable() { // �ѿ� ���� ������ => �����Ӱ� ���� ���ƾ� �ϹǷ� �����带 ��

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (true) { // ������ ���������� ���� �ݺ��Ѵ�.

					if (meInfo.endGame) { // end ���ӳ��� chk
						System.out.println("��������");
						JOptionPane.showMessageDialog(MePuyoPanel.this, "���� ����!");
						updateRank(); // ���� ������Ʈ

						if (threadPool != null && !threadPool.isShutdown()) // ������ ������ ������ Ǯ�� ���� �ִٸ�
							threadPool.shutdown(); // ������ �������Ƿ� ��� �����带 ����.

						return; // ���� ����
					}

					if (!bombChk) {

						// me ����
						me = new Puyo();
						meLb = new MyLabel(new ImageIcon("./img/" + me.color + "-48.png"));
						meLb.setBounds(LocationX, LocationY, Puyo.PUYOSIZE, Puyo.PUYOSIZE);
						meLb.setName(me.color);
						add(meLb);
						meInfo.puyos.add(me); // ����Ŭ������ ���� ����
						puyoLbs.add(meLb); // �󺧸� ���� ����
						addMap(meLb); // �� �гο��� ����� ���� ����

						// you ����
						you = new Puyo();
						youLb = new MyLabel(new ImageIcon("./img/" + you.color + "-48.png"));
						youLb.setBounds(LocationX, LocationY - Puyo.PUYOSIZE, Puyo.PUYOSIZE, Puyo.PUYOSIZE);
						youLb.setName(you.color);
						add(youLb);
						meInfo.puyos.add(you);
						puyoLbs.add(youLb);
						addMap(youLb);

						move(); // ����

					} else {

						if (bombChk()) { // �����忩�� ��� �����Ű�� ���� �߻�
							bomb(); // �׻� ���Ѻ��ٰ� ���� ���� 4�� �̻� ������ ���� ������ ����
							bombChk = false; // 356 Line ���� ���� ���� �Ұ��� ����, ������ �Ǿ� ��� ������
							// bomb �޼ҵ忡�� ���� ���Ƿ� �� �޼ҵ��� ��������
							// �ٸ� ��Ұ� ���ü� �ֵ��� boolean ��������
						} else {
							bombChk = false; // 356 Line ���� ���� ���� �Ұ��� ����, ������ �Ǿ� ��� ������
							// bomb �޼ҵ忡�� ���� ���Ƿ� �� �޼ҵ��� ��������
							// �ٸ� ��Ұ� ���ü� �ֵ��� boolean ��������
						}

					}

				}

			}

		};

		threadPool.submit(thread); // �����带 ����ϱ� ���� ������ Ǯ�� ���

	}

	void addMap(MyLabel puyo) { // �����ɶ� �򺰷� ������ �ѹ��� ����

		if (this.map.containsKey(puyo.getName())) {

			map.get(puyo.getName()).add(puyo);

		} else {
			HashSet<MyLabel> set = new HashSet<MyLabel>();
			set.add(puyo);
			this.map.put(puyo.getName(), set);
		}

	}

	void move() { // �ڵ����� �������� y�ุ ������Ʈ �ϸ� �˴ϴ�.
		// �ѿ䰡 ���� �Ǹ� x ���� key����� �����ϰ�, y������ �ڵ����� �帥��.
		while (true) {

			try {

				if (me.stopChk && you.stopChk) { // �ѿ� �ѽ��� ���� �ڸ��� ��Ҵ�.
					fixBug(me, meLb); // ������ ����� ����� ����
					fixBug(you, youLb); // ������ ����� ����� ����
					modifyNode();
					comboChk = -1; // �޺� chk �ʱ�ȭ
					this.bombChk = true; // ������ ��� �ߴ��ϰ� ���� ��Ұ� �ִ��� �˻�
					return; // ���� �ѿ� ������ ���� �ݺ��� ����
				}

				Thread.sleep(33); // 33���� ������
				endMove(me, meLb); // �ѿ䰡 ������ �귯���� ���ϴ�.
				endMove(you, youLb); // �ѿ䰡 ������ �귯���� ���ϴ�
				updateInfo();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	} // move �Լ� ��

	void endMove(Puyo puyo, MyLabel lb) { // �ѿ䰡 ���������� �귯 ���� �� �� �ְ� �ϴ� �Լ�

		fixBug(puyo, lb);

		if (puyo.stopChk)
			return;

		int y = nodeY(lb); // y���� �ѿ䰡 ���õ� y���� ��

		if (y <= getSize().height - Puyo.PUYOSIZE) { // �ѿ��� y���� ���� �ٴڰ� ������ �Ҷ� ������ ���ǿ� ����
			// ���⼭ �ٴڱ��� ������....
			// �� ���� �ٸ� ��Ұ� �ִٸ� stop
			if (search(lb)) { // ���� ����� y ���� ���ڸ��� ������ �ɶ��� �ѿ䰡 �ڸ��� ��Ҵٶ�� �ǹ�. �� �ٴڿ� ���� ����.
				y = cutLine;
				puyo.stopChk = true; // �ѿ䰡 ���� ����� �� �ѿ��� ��ġ�� ���� �Ǿ����� ....

				if (lb.getY() <= -5) // �ѿ䰡 �ڸ��� ��Ҵµ� Y�� ���� 0���� �۰ų� ������ �� �������� �׿����� ������ ����
					meInfo.endGame = true;

			}

			if (y >= getSize().height - Puyo.PUYOSIZE)
				y = getSize().height - Puyo.PUYOSIZE;

			lb.setLocation(lb.getX(), y);
		} else {
			puyo.stopChk = true; // �Ʒ� �ѿ� �Ǵ� ���� �϶� ��ġ�� ���� �Ǿ�����....
		}

	} // endMove ��

	int nodeY(MyLabel puyo) { // �ѿ��� y��ǥ�� ��ȯ �޴� �Լ�

		int y = puyo.getY();
		int result = y + step;

		return result;

	} // nodeY ��

	boolean search(MyLabel puyo) { // �ѿ䰡 ������ �带�� �� �ؿ� �����ΰ� ���� �Ѵٸ� y���� �ٽ� �����Ͽ� ��ȯ
									// ������ : �� ���� �����϶� ��ģ���� �Ʒ� ģ�� ������ �������� ���� �߻�
		boolean result = false;

		for (MyLabel pu : puyoLbs) {
			if (puyo.getY() < pu.getY()) { // �����Ǵ� �ѿ��� y �� ���� ���� ���� ���� y���� ū �ֵ鸸 ����. => ������ �ؿ� �ִ� �ֵ�
				if (puyo.getX() == pu.getX()) { // x���� ���ƾ� ���� �����Դϴ�.
					if (puyo.getY() + Puyo.PUYOSIZE > pu.getY()) { // ���� y�� �ؿ��ִ� �ѿ��� y���� ���ų� Ŀ������ �Ҷ�
						System.out.println("puyo.Lb.getY() + Puyo.PUYOSIZE : " + (puyo.getY() + Puyo.PUYOSIZE));
						System.out.println(" pu.Lb.getY() : " + pu.getY());
						this.cutLine = pu.getY() - Puyo.PUYOSIZE;
						System.out.println("this.cutLine : " + this.cutLine);
						result = true;
					}
				}
			}

		}

		return result;

	} // inspectY �Լ� ��

	void modifyNode() {

		for (MyLabel puyo : puyoLbs) {

			// y ��ǥ�� �ڲ� �Դ� ���� �߻�!!!!
			// y ��ǥ�� 50���� ������ 0�� �ȵǴ� �� ã��

			if (puyo.getY() % Puyo.PUYOSIZE != 0) {
				// modifyArr.add(puyo);
				double y = puyo.getY();
				double remainder = Puyo.PUYOSIZE - (puyo.getY() % Puyo.PUYOSIZE);
				int result = (int) (y + remainder);
				// ���� Ư���� ��ǥ�� �������� �ʰԵ� �� ���� ������ ũ�⸸ŭ ���� ������ ��ŭ�� �����־� �ٽ� ����

				puyo.setLocation(puyo.getX(), result);

			}

		}

	}

	void fixBug(Puyo puyo, MyLabel lb) { // ������ ����� ����� ���� (�߰��� �ɸ��� ����)

		if (!puyo.stopChk)
			return;

		int chk = 0;
		if (puyo.stopChk && lb.getY() != getSize().height - Puyo.PUYOSIZE) {

			for (MyLabel pu : puyoLbs) {

				if (lb.getY() + Puyo.PUYOSIZE == pu.getY())
					chk++;
			}

		}

		System.out.println(chk);

		if (chk == 0) {
			puyo.stopChk = false;
			endMove(puyo, lb);
		}

	}

	////////////// TODO ���� ����

	boolean bombChk() {

		boolean result = false;

		for (HashSet<MyLabel> puyo : map.values()) {

			// 4�� �϶� �� ã�� Error �� ������� �ɾ� ���� ��
			System.out.println("4�� �϶� �� ã�� Error �� ������� �ɾ� ���� ��");
			sleepThread();

			if (puyo.size() >= MeGameInfo.PANG)
				result = true;

		}

		return result;

	}

	void bomb() { // �迭�� ���ƾ� �ϴµ� ������� �θ��� ������ �񵿱� ���

		// for���� ���ư��� map�� remove�� �ǹǷ� Ŭ���� �ϳ� ��� �մϴ�.
		HashMap<String, HashSet<MyLabel>> cloneMap = new HashMap<String, HashSet<MyLabel>>(this.map);

		for (HashSet<MyLabel> puyo : cloneMap.values()) {

			// �ʿ��� �ش� ������ �ؽ����� ���̰� 4�̸��̸� �� �ʿ䵵 ����.
			System.out.println("puyo.size() : " + puyo.size());

			if (puyo.size() >= MeGameInfo.PANG) {

				deepBomb(puyo);
				System.out.println(bombArr);
				System.out.println(bombArr.size());

			}
		}

	}

	void deepBomb(HashSet<MyLabel> colors) { // clolors �� ������Ʈ�� �ȵ�

		HashSet<MyLabel> equals = new HashSet<MyLabel>(); // �پ� �ִ� ���� ���� ū ������� ���� �迭
		int size = 0;

		for (MyLabel puyo : colors) {

			HashSet<MyLabel> equalsTemp = new HashSet<MyLabel>();

			equalsTemp.add(puyo);

			// ���ڰ��� �������� ������ �Ǵ� puyo�� ��ǥ�� ����
			int x = puyo.getX();
			int y = puyo.getY();

			for (MyLabel pu : colors) {

				if (x == pu.getX() && y == pu.getY() + Puyo.PUYOSIZE
						|| x == pu.getX() && y == pu.getY() - Puyo.PUYOSIZE)
					equalsTemp.add(pu);
				if (y == pu.getY() && x == pu.getX() + Puyo.PUYOSIZE
						|| y == pu.getY() && x == pu.getX() - Puyo.PUYOSIZE)
					equalsTemp.add(pu);
			}

			System.out.println("** equalsTemp: " + equalsTemp);

			if (size < equalsTemp.size()) { // �ְ� ���� �پ� �ִ� ����� ����
				size = equalsTemp.size();
				equals = equalsTemp;
			}

			if (equalsTemp.size() == 3)
				break;

			// System.out.println("colors : " + colors);
			// System.out.println("equals : " + equals);
		}

		equals = deepDeepBomb(colors, equals);
		// System.out.println(equals);

		if (equals.size() >= 4) {
			bombArr = equals;
			jum = 10;
			this.score += bombArr.size() * jum;
			remove();
			empty();
		}

		System.out.println("deepBomb ��");
	}

	HashSet<MyLabel> deepDeepBomb(HashSet<MyLabel> colors, HashSet<MyLabel> equals) {
		System.out.println("deepDeepBomb ����");

		if (equals.size() <= 1)
			return equals;

		HashSet<MyLabel> result = new HashSet<MyLabel>();

		HashSet<MyLabel> removeColor = new HashSet<MyLabel>(colors);
		removeColor.removeAll(equals);

		for (MyLabel puyo : equals) {

			// ���ڰ��� �������� ������ �Ǵ� puyo�� ��ǥ�� ����
			int x = puyo.getX();
			int y = puyo.getY();

			result.add(puyo);
			modifyNode();

			for (MyLabel pu : removeColor) {

				if (x == pu.getX() && y == pu.getY() + Puyo.PUYOSIZE
						|| x == pu.getX() && y == pu.getY() - Puyo.PUYOSIZE)
					result.add(pu);
				if (y == pu.getY() && x == pu.getX() + Puyo.PUYOSIZE
						|| y == pu.getY() && x == pu.getX() - Puyo.PUYOSIZE)
					result.add(pu);

			}

		}

//		System.out.println("** ���� colors : " + colors);
//		System.out.println("** ���� �ֵ鸸 ��ƿ� �迭 : " + equals);
//		System.out.println("**  ���� colors ���� ���� �ֵ��� ������ �迭 : " + removeColor);
//		System.out.println("** result �迭 : " + result);

		if (result.size() > equals.size())
			result = deepDeepBomb(colors, result);

		return result;

	}

	void remove() {

		for (MyLabel puyo : bombArr) {
			// ������ �ѿ� �гο��� ���� �۾�
			remove(puyo); // �гο��� ����
			setVisible(false); // update
			setVisible(true); // update
		}

		itemChk(bombArr);

		comboChk++; // ó�� �������� 0 �̵ǰ�
		// ��������� �̱����� �� ��ĥ�� �������Ƿ� 1 �̵Ǽ� ���� �޺� ����
		if (comboChk > 0)
			this.comboCnt++;
		combo = comboCnt * (jum * 2);

	}

	void itemChk(HashSet<MyLabel> bombArr) { // ������ ���� ������ ���������� ����
		// �������� ������ ���κ��� �ٽ� ������Ʈ �ϴ� �κ��� �߿�
		// �׷� ���������� ���λ����� ? meInfo�� ... ?

		ArrayList<MyLabel> item = new ArrayList<MyLabel>(bombArr);

		// ���� ����߿� ������ ���� �ִ��� Ȯ��
		for (MyLabel myLabel : item) {
			if (myLabel.getName().equals("ninja")) {
				meInfo.itemChk = true; // �ִٸ� true
				// �����ӿ��� ������ �ٽ� false�� �ٲ�
				break;
			}
		}

	}

	void empty() { // ���� �ѿ䰡 ������ ������� �޲ٴ� �޼ҵ� ����

		for (MyLabel puyo : bombArr) {
			// ���� �ѿ並 �� �� ��̿��� ����
			map.get(puyo.getName()).remove(puyo);
			meInfo.puyos.remove(equalsPuyo(puyo));
			puyoLbs.remove(puyo); // Ǫ����� ��� �迭���� ����
		}

		emptyMove();

	}

	void emptyMove() { // �ڵ����� �������� y�ุ ������Ʈ �ϸ� �˴ϴ�.

		// �ٴڿ� �ִ� ���̵��� �������� �ʿ� �����Ƿ� ����

		TreeSet<MyLabel> updatePuyo = new TreeSet<MyLabel>();

		// �ƿ� ó������ ������Ʈ �� �༮�� ��������

		System.out.println(bombArr);

		for (MyLabel puyo : bombArr) {

			for (MyLabel pu : puyoLbs) {

				if (puyo.getX() == pu.getX()) { // ������ �ѿ�� x�� ����
					if (puyo.getY() > pu.getY()) { // ������ �ѿ亸�� ���� �ִٸ�...

						equalsPuyo(pu).stopChk = false;
						updatePuyo.add(pu);
//						System.out.println("������Ʈ�� �ʿ��� �ѿ� ���");
//						System.out.println(updatePuyo);
					}
				}

			}

		}

		if (updatePuyo.size() == 0) { // ������Ʈ �� ��Ұ� ���ٸ� ����
			return;
		}

		// �ִٸ� �Ʒ��� ���� ����

		bombArr = new HashSet<MyLabel>(); // ���� ����� ���� �ʿ� �����Ƿ� �ʱ�ȭ

		emptyEndMove(updatePuyo); // ��ҵ��� ������ �̵��� ������
		modifyNode();

		if (bombChk()) { // ��������� ���� ���� �ֳ� �˻��մϴ�.
			bomb();
			modifyNode();

		}

	} // move �Լ� ��

	void emptyEndMove(TreeSet<MyLabel> updatePuyo) {

		while (true) {

			for (MyLabel puyo : updatePuyo) {

				int index = 0;
				for (MyLabel pu : updatePuyo) {

					if (equalsPuyo(pu).stopChk)
						index++;

				}

				if (updatePuyo.size() == index) // ������Ʈ ��� ���� retuen
					return;

				if (!equalsPuyo(puyo).stopChk) {

					try {

						endMove(equalsPuyo(puyo), puyo); // �ѿ䰡 ������ �귯���� ���ϴ�.
						updateInfo();
						Thread.sleep(33); // 33���� ������

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

	Puyo equalsPuyo(MyLabel lb) { // ������ �󺧿� �´� puyo Ŭ������ ��ȯ�ϴ� �޼ҵ�

		int index = puyoLbs.indexOf(lb);

		Puyo result = meInfo.puyos.get(index);

		return result;

	}

	void updateInfo() { // �ð����� �г��� ������
		// �����ٷ�� Ŭ������ ������ ������Ʈ �Ѵ�.

		for (int i = 0; i < puyoLbs.size(); i++) {
			meInfo.puyos.get(i).x = puyoLbs.get(i).getX();
			meInfo.puyos.get(i).y = puyoLbs.get(i).getY();

		}
		meInfo.score = score;
		meInfo.combo = comboCnt;
		meInfo.second = timer.second;
		meInfo.total = score + combo;

		if (frame.data != null)
			frame.data.data = meInfo;

	}

	void updateRank() { // ��ŷ ������Ʈ
		
		System.out.println("����000-----");

		RankDTO dto = new RankDAO().detail(id);
		
		System.out.println("������������"+dto);

		Integer currentScore = dto.getScore();

		if (currentScore < meInfo.total)
			currentScore = meInfo.total;
		
		System.out.println("Ŀ��Ʈ ���ھ� "+currentScore);

		dto.setScore(currentScore);
		System.out.println("Ŀ��Ʈ �Ʒ��� "+dto);
		new RankDAO().modifyScore(dto);

	}

	void sleepThread() {

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}