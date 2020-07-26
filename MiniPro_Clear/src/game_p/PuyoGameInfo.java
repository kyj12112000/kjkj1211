package game_p;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PuyoGameInfo extends JPanel {

	JLabel score; // 점수 라벨
	JLabel combo; // 콤보 라벨
	JLabel second; // 시간 라벨

	public PuyoGameInfo() {
		// TODO Auto-generated constructor stub

		setLayout(null);
		setBackground(Color.black);

		score = new JLabel("점수");
		combo = new JLabel("콤보");
		second = new JLabel("경과 시간");

		score.setForeground(Color.white);
		combo.setForeground(Color.white);
		second.setForeground(Color.white);

		// 폰트 중앙 정렬
//		score.setHorizontalAlignment(score.CENTER);
//		combo.setHorizontalAlignment(combo.CENTER);
//		time.setHorizontalAlignment(time.CENTER);

		// 바운더리 확인
//		score.setBackground(Color.blue);
//		score.setOpaque(true);
//		combo.setBackground(Color.red);
//		combo.setOpaque(true);
//		time.setBackground(Color.green);
//		time.setOpaque(true);

		score.setBounds(0, 0, 100, Puyo.PUYOSIZE);
		combo.setBounds(100, 0, 200, Puyo.PUYOSIZE);
		second.setBounds(200, 0, 300, Puyo.PUYOSIZE);

		add(score);
		add(combo);
		add(second);

		setVisible(false);
		setVisible(true);

	}
}