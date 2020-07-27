package game_p;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class WaitRoomPanel extends JPanel {

	ImageIcon background;

	public WaitRoomPanel() {
		// TODO Auto-generated constructor stub
		this.background = new ImageIcon("./img/background.png");
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(background.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}

}
