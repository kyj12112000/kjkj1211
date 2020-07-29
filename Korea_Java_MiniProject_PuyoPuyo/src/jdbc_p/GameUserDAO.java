package jdbc_p;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ddong.InitData;

//DAO 데이터베이스에 접근해서 작업을 하는 부분을 소유한 클래스 
public class GameUserDAO {

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null;

//   String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String url = "jdbc:oracle:thin:@" + InitData.ip + ":1521:xe";
	String id = InitData.id;
	String pw = InitData.pw;

	PreparedStatement ps = null;

	public GameUserDAO() {

		try {
			con = DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<GameUserDTO> list() {

		ArrayList<GameUserDTO> res = new ArrayList<GameUserDTO>();
		sql = "select * from gameuser";

		try {

			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				GameUserDTO dto = new GameUserDTO();
				dto.id = rs.getString("id");
				dto.pw = rs.getString("pw");
				dto.name = rs.getString("name");
				dto.birth = rs.getString("birth");
//            dto.birth= rs.getDate("birth");
				dto.email = rs.getString("email");

				res.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}

	// 로그인 부분~~~~~~
	public boolean Login(String id, String pw) {
		boolean result = false;

		try {
			stmt = con.createStatement();

			sql = "select * from gameuser where id='" + id + "'";
			rs = stmt.executeQuery(sql); // 읽어오는거라 다르다 비교해 //리턴타입이 ResultSet

			if (rs.next() == false || (id.isEmpty()) == true) { // id가 존재x
				result = false;
			} else {
				sql = "select * from (select * from gameuser where id='" + id + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next() == true) { // 다음값의
					if (rs.getString(1).equals(id) && rs.getString(2).equals(pw)) { // pw와 같은지 비교
						result = true; // 같으면 로그인 성공
						break;
					} else { // 아이디는같고 pw가 다른경우
						result = false;

						break;
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return result;
	}

	// 아이디 비번 찾기
	public GameUserDTO find(String name, String birth, String email) {

		GameUserDTO dto = new GameUserDTO();

		try {
			stmt = con.createStatement();

			sql = "select * from gameuser where email='" + email + "'";
			rs = stmt.executeQuery(sql); // 읽어오는거라 다르다 비교해 //리턴타입이 ResultSet

			if (rs.next()) { // id가 존재x
				if (rs.getString(3).equals(name) && rs.getString(4).equals(birth) && rs.getString(5).equals(email)) {
					dto.id = rs.getString("id");
					dto.pw = rs.getString("pw");
					dto.name = rs.getString("name");
					dto.birth = rs.getString("birth");
					dto.email = rs.getString("email");
					// System.out.println(dto+"dao에서 dto");
				} else {
					dto = null;
				}

			}

		} catch (

		Exception ee) {
			ee.printStackTrace();
		}
		return dto;
	}

	public GameUserDTO detail(String id) {

		GameUserDTO dto = new GameUserDTO();
		sql = "select * from gameuser where id = '" + id + "'";

		try {

			rs = stmt.executeQuery(sql);
			if (rs.next()) {

				dto.id = rs.getString("id");
				dto.pw = rs.getString("pw");
				dto.name = rs.getString("name");
				dto.birth = rs.getString("birth");
				dto.email = rs.getString("email");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return dto;
	}

	// 회원 가입란~~~~~~~~~~
	public int insert(GameUserDTO dto) {

		int res = 0;
		sql = "insert into gameuser " + "(id, pw, name, birth, email) " + "values " + "('" + dto.id + "','" + dto.pw
				+ "','" + dto.name + "','" + dto.birth + "','" + dto.email + "')";

		try {

			res = stmt.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}

	public int delete(String id) {

		int res = 0;
		sql = "delete from gameuser where id = '" + id + "'";

		try {

			res = stmt.executeUpdate(sql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}

	// 아이디 중복검사~~~~~~~
	public int idChk(String id) {

		int result = 1;

		try {
			stmt = con.createStatement();

			sql = "select * from gameuser where id='" + id + "'";
			rs = stmt.executeQuery(sql); // 읽어오는거라 다르다 비교해 //리턴타입이 ResultSet

			if (rs.next() == false || (id.isEmpty()) == true) { // id가 존재x
				result = 1;
			} else {
				sql = "select * from (select * from gameuser where id='" + id + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next() == true) {
					if (rs.getString(1).equals(id)) {
						result = 0;
						break;
					}
					break;

				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return result;
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

}