package jdbc_p;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import ddong.InitData;

public class GameRoomDAO {

	Connection con;
	Statement stmt;
	ResultSet rs;

	String url = "jdbc:oracle:thin:@" + InitData.ip + ":1521:xe";
//   String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String id = InitData.id;
	String pw = InitData.pw;

	String sql;

	public GameRoomDAO() {

		try {
			con = DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {

		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
			}
	}

	public ArrayList<GameRoomDTO> list() {

		ArrayList<GameRoomDTO> res = new ArrayList<GameRoomDTO>();

		sql = "select * from gameroom";

		try {

			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				GameRoomDTO dto = new GameRoomDTO();

				dto.no = rs.getInt("no");
				dto.user1 = rs.getString("user1");
				dto.user2 = rs.getString("user2");

				res.add(dto);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}

	// --------------------------------------------------------
	// public GameRoomDTO detail : 원하는 정보를 찾을때 사용한다
	public GameRoomDTO detailroom(Integer no) { // 방만들때 user1자리가 null인 곳 부터 방생성

		GameRoomDTO dto = null;

		sql = "select * from gameroom where no = '" + no + "'";

		try {

			rs = stmt.executeQuery(sql); // 쿼리에서 찾아본다

			if (rs.next()) { // 쿼리에서 찾은 데이터를 DTO에서 get해온다

				dto = new GameRoomDTO();

				dto.user1 = rs.getString("user1");
				dto.user2 = rs.getString("user2");
				dto.no = rs.getInt("no");
			} // if문 끝

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return dto;
		// 찾은 데이터 값을 반환해준다
	}

	// --------------------------------------------------------
	// == public int modifyUser1 =======
	public int modifyUser1(GameRoomDTO dto) { // user1가 방을 만들면 추가

		int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다

		// java에서 수정한 데이터 삽입해주는 쿼리문
		sql = "update gameroom set user1 = '" + dto.user1 + "' where  no = '" + dto.no + "'";

		try {

			res = stmt.executeUpdate(sql); // 수정한 내용 업데이트

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;
		// 수정된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
	} // == public int modifyUser1 끝 =======

	// --------------------------------------------------------
	// == public int modifyUser2 =======
	public int modifyUser2(GameRoomDTO dto) { // user2가 방에 입장하면 추가

		int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다

		// java에서 수정한 데이터 삽입해주는 쿼리문
		sql = "update gameroom set user2 = '" + dto.user2 + "' where  no = '" + dto.no + "'";

		try {

			res = stmt.executeUpdate(sql); // 수정한 내용 업데이트

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;
		// 수정된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
	} // == public int modifyUser2 끝 =======
		// --------------------------------------------------------

	// --------------------------------------------------------
	// == public int modifyUser3 =======

	public int modifyUser4(String col, String id, Integer roomNum) { // user2가 방에 입장하면 추가

		int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다

		// java에서 수정한 데이터 삽입해주는 쿼리문
		sql = "update gameroom set " + col + " = " + null + " where " + col + " = '" + id + "' and no = " + roomNum;

		try {

			res = stmt.executeUpdate(sql); // 수정한 내용 업데이트

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;
		// 수정된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
	} // == public int modifyUser2 끝 =======
		// --------------------------------------------------------

	public int modifyUser5(String col, String id) {

		int res = 0;

		sql = "update gameroom set " + col + " = " + null + " where " + col + " = '" + id + "'";
		// System.out.println(sql);

		try {

			res = stmt.executeUpdate(sql); // 수정한 내용 업데이트

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;

	}

	public int modifyUser6(String col, Integer roomNum) {

		int res = 0;

		sql = "update gameroom set " + col + " = 'temp' where no = " + roomNum;
		// System.out.println(sql);

		try {

			res = stmt.executeUpdate(sql); // 수정한 내용 업데이트

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;
	}

	// --------------------------------------------------------
	// == public int delete : java에서 삭제한 데이터 DB에서 삭제 =======
	public int reset(GameRoomDTO dto) { // user1이 방을 나가면 방을 없앤다

		int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다

		sql = "update gameroom set user1 = " + dto.user1 + ", user2 = " + dto.user2 + " where  no = '" + dto.no + "'";

		try {

			res = stmt.executeUpdate(sql); // 삭제한 내용을 업데이트

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
			close(); // DB를 닫기 전 확인을 위한 메소드
		}

		return res;
		// 삭제된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
	} // == public int delete 끝 =======

	public GameRoomDTO roomdPepleetail(String id) {
		GameRoomDTO dto = new GameRoomDTO();
		sql = "select * from gameroom where user1 = '" + id + "'" + " or user2='" + id + "'";
		try {

			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				dto.user1 = rs.getString("user1");
				dto.user2 = rs.getString("user2");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return dto;
	}

}