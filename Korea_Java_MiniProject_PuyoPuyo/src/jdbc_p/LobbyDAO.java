package jdbc_p;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ddong.InitData;

public class LobbyDAO {
   
   Connection con; // DB와 연결해주는 역할
   Statement stmt; // 데이터 바인딩, SQL을 해석해주는 객체
   ResultSet rs; //DB에서 select한 결과를 담는다
   
   String sql; // 쿼리문으로 쓸 String
   
//    DB연결시 필요한 url과 ID, PW를 쓰기 편하게 따로 변수로 빼주었다
//   String url = "jdbc:oracle:thin:@localhost:1521:xe";
   String url = "jdbc:oracle:thin:@"+InitData.ip+":1521:xe";
   String id = InitData.id;
   String pw = InitData.pw;
   
   
   //----------------------------------------------
   // == public LobbyDAO() 시작 =======() 시작 =======
   public LobbyDAO() {
      
      try {
         
         con = DriverManager.getConnection(url, id, pw); // DB연결
         
         stmt = con.createStatement(); // 쿼리 실행 준비
      
      } catch (SQLException e) {
         e.printStackTrace();
      } // = try-catch DB접속준비 끝 =
   } // == public RankDAO() 끝 =======
   //--------------------------------------------------------
   
   
   //--------------------------------------------------------
      // == public void close() 시작 =======
   public void close() { // 열어준 DB를 닫으면서 닫기 전 확인사항 체크 메소드
      
      // 열렸는지 체크하면서 닫아줘야한다. 값이변경됬는지 보고 열린지 판단 
      // resultset부터 반대로 확인 > statement > connection 순으로
      
      if(rs!=null) try { rs.close(); } catch (SQLException e) { }
      if(stmt!=null) try { stmt.close(); } catch (SQLException e) { }
      if(con!=null) try { con.close(); } catch (SQLException e) { }
      // 제일 마지막에 연 것 부터 확인하면서 닫아주면서 나간다
   } // == public void close() 끝 =======
   //--------------------------------------------------------
   
   
   //--------------------------------------------------------
   // == public ArrayList<RankDTO> list() : 데이터를 담아줌 =======
   public ArrayList<LobbyDTO> list(){
      
      ArrayList<LobbyDTO> res = new ArrayList<LobbyDTO>();
      // *데이터를 담아주는 리스트
      sql = "select * from lobby";
      // 쿼리 실행문.DB테이블 중 lobby을 사용
      
      try {
         
         rs = stmt.executeQuery(sql);
         // 쿼리 실행결과를 가져온다. select 쿼리 일때만 사용
         
         while(rs.next()) {// 데이터가 있으면 데이터가 없을때까지 가져온다
            
            LobbyDTO dto = new LobbyDTO();
            // 데이터를 Object로 변환해서 가져와줄 DTO생성
            
            dto.id = rs.getString("id"); // DB에서 id를 가져와 dto에서 변환시켜준다
            
            res.add(dto);
            // 위에서 만들어 준 *데이터를 담아주는 arraylist에 데이터를 넣어준다.
         } // while문 - 데이터가져오기 끝
         
      } catch (SQLException e) {
         e.printStackTrace();
      } finally { // DB를 사용 후 DB를 닫을 때는 finally를 사용해서 닫는다
         close(); // 데이터를 사용 후 연결해제, 해제하지 않으면 다른사용자가 사용 할 수 있다
      }
      
      
      return res;
      // 데이터를 담아준 arraylist를 반환 해준다
   } // == public ArrayList<MemDTO> list() 끝 =======
   //--------------------------------------------------------
   
   
   //--------------------------------------------------------
   
   public int insert(String id) {
      
      int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다
      
      sql = "insert into lobby "+ "(id) " + "values " +
      "('" + id + "')";
      // ▲ v에서 추가해준 값들을 받아와서 DB넣어줄 쿼리문을 만들어줌
      
      try {
         
         res = stmt.executeUpdate(sql); // 내용 업데이트
      
      } catch (SQLException e) {
         e.printStackTrace();
      }finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
         close(); // DB를 닫기 전 확인을 위한 메소드
      }

      
      return res;
      // 추가된 데이터의 업데이트가 성공적으로 되면 결과값을 1로 반환해준다
   } // == public int insert 끝 =======
   //--------------------------------------------------------
   
   
   //--------------------------------------------------------
   // == public int delete : java에서 삭제한 데이터 DB에서 삭제 =======
   public int delete(String id) { // 삭제할 아이디값만 받아와서 확인 후 삭제한다
      
      int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다
      
      sql = "delete from lobby where id = '" + id + "'";
      // ▲ v에서 삭제하기 위해 받아온 아이디 값을 DB에서 찾아서 삭제한다
      
      try {
         
         res = stmt.executeUpdate(sql); // 삭제한 내용을 업데이트
      
      } catch (SQLException e) {
         e.printStackTrace();
      }finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
         close(); // DB를 닫기 전 확인을 위한 메소드
      }
      
      
      return res;
      // 삭제된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
   } // == public int delete 끝 =======
   
   
   public int deleteAll() { // 삭제할 아이디값만 받아와서 확인 후 삭제한다
      
      int res = 0; // 쿼리문 결과 값이 숫자로 나오므로 int로 결과를 받아준다
      
      sql = "delete from lobby ";
      // ▲ v에서 삭제하기 위해 받아온 아이디 값을 DB에서 찾아서 삭제한다
      
      try {
         
         res = stmt.executeUpdate(sql); // 삭제한 내용을 업데이트
      
      } catch (SQLException e) {
         e.printStackTrace();
      }finally { // 위에서와 마찬가지로 DB사용 후 DB를 닫아준다
         close(); // DB를 닫기 전 확인을 위한 메소드
      }
      
      
      return res;
      // 삭제된 데이터의 업데이트가 성공적으로 되면 결과값을 0로 반환해준다
   }
   
   
    public LobbyDTO detail(String id){
        LobbyDTO dto = new LobbyDTO();
         sql = "select * from lobby where id = '"+id+"'";
         try {
            
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
               dto.id = rs.getString("id");
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }finally {
            close();
         }
          
         return dto;
      }
   
   

}