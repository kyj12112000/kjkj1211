package game_p;

public class PuyoTimer extends Thread { // 타이머 쓰레드 // 점수도 같이 관리

	MePuyoPanel panel; // 프레임에 있는 정보를 업데이트 하기 위해 받아옴
	int second; // 초
	boolean first;

	public PuyoTimer(MePuyoPanel panel) {
		// TODO Auto-generated constructor stub
		this.panel = panel;
		this.second = 0;
		this.first = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			try {

				if (panel.meInfo.endGame) // 게임이 끝날때까지 계속 돈다
					return;

				if (!first) {

					if (second % 30 == 0) { // 30초마다 속도가 2씩 증가

						panel.step += 2;
					}
				}
				panel.info.score.setText("점수 : " + (panel.score + panel.combo) + "점");
				panel.info.combo.setText("연쇄 : " + panel.comboCnt + "연쇄");
				panel.info.second.setText("경과 시간 : " + second + "s");

				// updateInfo();
				sleep(1000);
				second++;

				first = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

//	void updateInfo() { // 시간마다 패널의 정보로
//		// 정보다루는 클래스의 정보를 업데이트 한다.
//
//		// System.out.println("updateInfo 진입" + second); // 진입 확인 완
//
////			System.out.println("사이즈가 다른가요 ? : " + panel.puyoLbs.size());
////			System.out.println("사이즈가 다른가요 ? : " + panel.meInfo.puyos.size());
//
//		for (int i = 0; i < panel.puyoLbs.size(); i++) {
//			panel.meInfo.puyos.get(i).x = panel.puyoLbs.get(i).getX();
//			panel.meInfo.puyos.get(i).y = panel.puyoLbs.get(i).getY();
//
//		}
//
//		panel.meInfo.score = panel.score;
//		panel.meInfo.combo = panel.comboCnt;
//		panel.meInfo.second = this.second;
//		panel.meInfo.total = panel.score + panel.combo;
//
//	}

}
