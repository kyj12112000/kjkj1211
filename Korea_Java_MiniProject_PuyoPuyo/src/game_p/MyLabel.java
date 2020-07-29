package game_p;

import javax.swing.Icon;
import javax.swing.JLabel;

public class MyLabel extends JLabel implements Comparable<MyLabel> {

	public MyLabel(Icon image) {
		// TODO Auto-generated constructor stub
		super(image);

	}

	@Override
	public int compareTo(MyLabel o) {
		// TODO Auto-generated method stub
		int res = o.getY() - getY();

		if (res == 0)
			res = getX() - o.getY();

		return res;
	}

}