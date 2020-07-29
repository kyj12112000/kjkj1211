package game_p;

import java.io.Serializable;
import java.util.ArrayList;

public class MeGameInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	final static int PANG = 4;

	ArrayList<Puyo> puyos;

	boolean endGame, itemChk;
	int score, combo, second, total;

	public MeGameInfo() {
		// TODO Auto-generated constructor stub
		this.puyos = new ArrayList<Puyo>();
		this.endGame = false;
		this.itemChk = false;
		this.score = 0;
		this.combo = 0;
		this.second = 0;
	}

	@Override
	public String toString() {
		return "MeGameInfo [puyos=" + puyos + ", endGame=" + endGame + ", score=" + score + ", combo=" + combo
				+ ", second=" + second + ", total=" + total + "]";
	}

}
