// 200729 수정완료
package lobby_p;

import java.awt.Font;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jdbc_p.RankDAO;
import jdbc_p.RankDTO;

import javax.swing.ScrollPaneConstants;

class User implements Comparable<User>{

   String id;
   Integer score, rank;


   public User(String id, Integer score) {

      super();
      this.id = id;
      this.score = score;
      this.rank = rank;
   }


   void rankCalc(TreeSet<User> you){ // 순위 메기는 곳

      rank = 1;

      for (User uR : you) {
         if(score<uR.score)
            rank++;
      }

      RankDTO dto = new RankDTO();
      dto.setId(id);
      dto.setRank(rank);

      new RankDAO().modifyRank(dto); // 순위메기고 순위값 DB에 저장
   }


   @Override
   public String toString() {

      return rank + "," + id + "," + score;
   }


   @Override
   public int compareTo(User you) { // 점수 제일 높은 순으로 출력

      int res = you.score - score;

      if(res==0)
         res = id.compareTo(you.id);

      return res;
   }
}



public class RankMain_GUI extends JPanel {

   Lobby_Main lobby;


   public RankMain_GUI() {

      setBounds(0,0, 636,902);
      setLayout(null);

      // == DB에서 점수를 가져와 순위를 메긴다 ========
      String id;
      Integer score;

      TreeSet<User> userInfo = new TreeSet<User>(); // 유저들의 정보를 담을 treeset, 유저들 점수비교해서 랭킹 

      RankDTO dto = new RankDTO();
      RankDAO dao = new RankDAO();

      for (RankDTO info : new RankDAO().list()) { // DB에 있는 정보를 순위비교를 위해 TreeSet에 저장

         id = info.getId();
         score = info.getScore();

         userInfo.add(new User(id, score));
      }


      int cnt = 0; // 테이블에 들어갈 내용 배열 줄수 체크를 위해 생성

      Object [] index = {"RANK", "ID", "SCORE"}; // 랭킹테이블 컬럼
      Object [][] userL = new Object[userInfo.size()][3]; // 유저의 랭킹, 점수를 랭킹순으로 담을 배열
      Object [][] myRankL = new Object[1][1];

      for (User chk : userInfo) { // 순위를 메기고 순위 순서대로 출력 후 배열에 담아준다

         chk.rankCalc(userInfo); // 랭킹 계산 후 순서대로 담아 준다

         userL[cnt][0] = chk.rank;
         userL[cnt][1] = chk.id;
         userL[cnt][2] = chk.score;

         cnt++;
      }


      JLabel totRank = new JLabel("전체순위");
      totRank.setBounds(29, 20, 110, 30);
      totRank.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 25));
      add(totRank);

      JTable rankT = new JTable(userL, index);
      rankT.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 13));
      rankT.setEnabled(false);
      JScrollPane sp2 = new JScrollPane(rankT);
      sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      sp2.setBounds(29, 60, 450, 450);
      add(sp2);

   }

   public static void main(String[] args) {

      new RankMain_GUI();
   }
}