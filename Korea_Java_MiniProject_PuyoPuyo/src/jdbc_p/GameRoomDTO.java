package jdbc_p;

public class GameRoomDTO {

	int no; // 방번호
	String user1, user2; // 유저
	
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	
	@Override
	public String toString() {
		
		return no + "," + user1 + "," + user2;
	}
	
}
