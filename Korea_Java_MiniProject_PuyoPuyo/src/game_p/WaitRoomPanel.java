package game_p;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class WaitRoomPanel extends JPanel {

	ImageIcon background;
	String src;

	public WaitRoomPanel(String src) {
		// TODO Auto-generated constructor stub
		this.src = src;
		this.background = new ImageIcon(src);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(background.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}

}
