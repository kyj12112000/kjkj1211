package jdbc_p;

public class RankDTO { // DB를 교환해주는 역할. 여기는 getter랑 setter로 이루어짐

	String id;
	Integer score, rank;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public String toString() { // 데이터값 어케 가져오는지 보려고 만듬
		return "순위 : " + rank + ", ID  : " + id + ", 점수 : " + score;
	}

}
