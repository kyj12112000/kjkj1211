package game_p;

import java.io.Serializable;

public class Puyo implements Serializable {

	private static final long serialVersionUID = 2L;

	final static int PUYOSIZE = 50;

	int x;
	int y;
	String color;
	boolean stopChk;

	public Puyo() {
		// TODO Auto-generated constructor stub

		this.stopChk = false;

		String[] colors = { "blue", "green", "red", "yellow", "ninja" };
		int i = (int) (Math.random() * 5);

		if (i == 4) {

			for (int j = 0; j < 3; j++) {
				i = (int) (Math.random() * 5);
			}

		}

		this.color = colors[i];
	}

}
