package login_p;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import jdbc_p.GameUserDAO;
import jdbc_p.GameUserDTO;
import jdbc_p.RankDAO;
import jdbc_p.RankDTO;
import login_p.LoginTest;
import java.awt.Font;

public class JoinTest extends JFrame {

   public ArrayList<String> data = new ArrayList<String>(); //id, pw, name, email, birth °ªÀ» ÀúÀå ÇØ¼­ mainÀ¸·Î ³Ñ°ÜÁÜ

   GameUserDTO dto = new GameUserDTO();
   GameUserDAO dao = new GameUserDAO();

   String[] regex = { 
         "^[a-zA-Z0-9_]{5,12}$", // ¾ÆÀÌµð ¿µ¾î¶û ¼ýÀÚ¸¸ 5±ÛÀÚ¿¡¼­ 12±ÛÀÚ »çÀÌ
         "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", // ¿µ¹®¼ýÀÚÆ¯¼ö¹®ÀÚ Æ÷ÇÔ 8ÀÚ ÀÌ»ó
         "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", // ¿µ¹®¼ýÀÚÆ¯¼ö¹®ÀÚ Æ÷ÇÔ 8ÀÚ ÀÌ»ó
         "^[°¡-ÆR]{2,4}$", // ÀÌ¸§ÇÑ±Û 2~4ÀÚ
         "^[0-9_]{1,6}$", // 
         "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$" // ÀÌ¸ÞÀÏ À¯È¿¼º °Ë»ç
   };

   String idc = "[a-zA-Z0-9]*(([a-zA-Z][0-9]*)|([0-9][a-zA-Z]*))";

   public String id;         // id ´ã¾ÆµÑ °ø°£
   public String pw;         // pw ´ã¾ÆµÑ °ø°£
   public String pw2;         // pwÈ®ÀÎ¶õ 
   public String name;      // ÀÌ¸§
   public String email;      // ¸ÞÀÏ
   public String birth;      // »ýÀÏ

   JTextField JoinIdText;
   JPasswordField passwordField;
   JPasswordField passwordField_1;
   JTextField JoinBirthText;
   JTextField JoinNameText;
   JTextField JoinEmailText;
   JFrame jf;

   JButton btnCancelMake;   //Á¾·á ¹öÆ°
   JButton btnMakeMember;   //È¸¿ø°¡ÀÔ½Ã ¿Ï·á ¹öÆ°
   JButton btnIdChk;      //¾ÆÀÌµð Áßº¹ °Ë»ç
   

   public JoinTest() {
   	getContentPane().setForeground(Color.RED);   // È¸¿ø °¡ÀÔ guiÃ¢

      setTitle("È¸¿ø°¡ÀÔ");
      setIconImage(new ImageIcon("./img/logo.png").getImage());
      getContentPane().setLayout(null);
      setSize(455, 615);            
      setLocationRelativeTo(null);   //gui È­¸é Á¤ Áß¾ÓÀ¸·Î ¸ÂÃçÁÜ
      getContentPane().setLayout(null);
      setResizable(false);   //gui »çÀÌÁî ¼öÁ¤ ºÒ°¡
 

      JLabel lbJoinId = new JLabel("¾ÆÀÌµð");
      lbJoinId.setBounds(17, 30, 74, 27);
      getContentPane().add(lbJoinId);

      
      JLabel lbJoinPw = new JLabel("ºñ¹Ð¹øÈ£");
      lbJoinPw.setBounds(17, 80, 74, 27);
      getContentPane().add(lbJoinPw);

      JLabel lbJoinPwC = new JLabel("ºñ¹Ð¹øÈ£ È®ÀÎ");
      lbJoinPwC.setBounds(17, 130, 122, 27);
      getContentPane().add(lbJoinPwC);
      
      JLabel lbName = new JLabel("ÀÌ¸§");
      lbName.setBounds(17, 180, 74, 27);
      getContentPane().add(lbName);


      JLabel lbJoinBirth = new JLabel("»ý³â¿ùÀÏ");
      lbJoinBirth.setBounds(17, 230, 74, 27);
      getContentPane().add(lbJoinBirth);

      JLabel lbJoinEmail = new JLabel("ÀÌ¸ÞÀÏ");
      lbJoinEmail.setBounds(17, 280, 74, 27);
      getContentPane().add(lbJoinEmail);

      JoinIdText = new JTextField();

      JoinIdText.setBounds(142, 30, 187, 27);
      JoinIdText.setColumns(10);
      getContentPane().add(JoinIdText);

      passwordField = new JPasswordField();
      passwordField.setBounds(142, 80, 187, 27);
      getContentPane().add(passwordField);

      passwordField_1 = new JPasswordField();
      passwordField_1.setBounds(142, 130, 187, 27);
      getContentPane().add(passwordField_1);

      JoinNameText = new JTextField();
      JoinNameText.setColumns(10);
      JoinNameText.setBounds(142, 180, 187, 27);
      getContentPane().add(JoinNameText);

      JoinBirthText = new JTextField();
      JoinBirthText.setColumns(10);
      JoinBirthText.setBounds(142, 230, 187, 27);
      getContentPane().add(JoinBirthText);

      JoinEmailText = new JTextField();
      JoinEmailText.setColumns(10);
      JoinEmailText.setBounds(142, 280, 187, 27);
      getContentPane().add(JoinEmailText);

      btnCancelMake = new JButton("Á¾·á");
      btnCancelMake.setBackground(Color.LIGHT_GRAY);
      btnCancelMake.setBounds(236, 330, 129, 29);
      getContentPane().add(btnCancelMake);
      btnCancelMake.addActionListener(butAct);

      btnMakeMember = new JButton("È¸¿ø°¡ÀÔ");
      btnMakeMember.setBackground(Color.LIGHT_GRAY);
      btnMakeMember.setBounds(89, 330, 129, 29);
      getContentPane().add(btnMakeMember);
      btnMakeMember.addActionListener(butAct);

      btnIdChk = new JButton("Áßº¹È®ÀÎ");
      btnIdChk.setBackground(Color.LIGHT_GRAY);
      btnIdChk.setBounds(346, 30, 86, 27);
      getContentPane().add(btnIdChk);
      
      JLabel lblNewLabel = new JLabel("\uC601\uBB38 \uB610\uB294 \uC22B\uC790\uB85C 5\uAE00\uC790 \uC774\uC0C1");
      lblNewLabel.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel.setForeground(Color.BLACK);
      lblNewLabel.setBounds(142, 55, 187, 21);
      getContentPane().add(lblNewLabel);
      
      JLabel lblNewLabel_1 = new JLabel("\uC601\uBB38,\uC22B\uC790,\uD2B9\uC218\uBB38\uC790 \uC870\uD569 8\uAE00\uC790 \uC774\uC0C1");
      lblNewLabel_1.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_1.setForeground(Color.BLACK);
      lblNewLabel_1.setBounds(142, 105, 187, 21);
      getContentPane().add(lblNewLabel_1);
      
      JLabel lblNewLabel_3 = new JLabel("2~4\uAE00\uC790");
      lblNewLabel_3.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_3.setForeground(Color.BLACK);
      lblNewLabel_3.setBounds(142, 205, 187, 21);
      getContentPane().add(lblNewLabel_3);
      
      JLabel lblNewLabel_4 = new JLabel("ex) 901211");
      lblNewLabel_4.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_4.setForeground(Color.BLACK);
      lblNewLabel_4.setBounds(142, 255, 187, 21);
      getContentPane().add(lblNewLabel_4);
      
      JLabel lblNewLabel_5 = new JLabel("ex) \uC544\uC774\uB514@\uB3C4\uBA54\uC778 \uC8FC\uC18C");
      lblNewLabel_5.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_5.setForeground(Color.BLACK);
      lblNewLabel_5.setBounds(142, 305, 187, 21);
      getContentPane().add(lblNewLabel_5);
      btnIdChk.addActionListener(butAct);
      
      
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   }
   
   
   
   

   ActionListener butAct = new ActionListener() { // ¾×¼Ç¸®½ºÆ®

      @Override
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == btnCancelMake) { // Á¾·á ¹öÆ° ´©¸¦ ½Ã ·Î±×ÀÎ Ã¢À¸·Î È­¸é ÀüÈ¯

            LoginTest lt = new LoginTest();
            dispose();
   
         }

         if (e.getSource() == btnIdChk) { // Áßº¹ °Ë»ç
            
               String jt = JoinIdText.getText().trim();
               if("".equals(jt) || !Pattern.matches(regex[0], jt)) {
                  JOptionPane.showMessageDialog(null, " ¾ÆÀÌµð¸¦ È®ÀÎ ÇØÁÖ¼¼¿ä");
               
               }else if(dao.idChk(jt)==0) {
                  JOptionPane.showMessageDialog(null, JoinIdText.getText() + " Áßº¹ÀÔ´Ï´Ù.");
               }else if(dao.idChk(jt)==1 && Pattern.matches(regex[0], jt)) {
                  JOptionPane.showMessageDialog(null, JoinIdText.getText() + " »ç¿ë°¡´ÉÇÕ´Ï´Ù.");
               }
            
         }

         if (e.getSource() == btnMakeMember) {   //È¸¿ø °¡ÀÔ 
            id = JoinIdText.getText();   
            pw = passwordField.getText();
            pw2 = passwordField_1.getText();
            name = JoinNameText.getText();
            birth = JoinBirthText.getText();
            email = JoinEmailText.getText();
            
            String[] joinChk = { id, pw, pw2, name, birth, email }; // À¯È¿¼º °Ë»ç¸¦ À§ÇØ ¹è¿­Çü
            String[] pppChk = { "id¸¦ È®ÀÎ ÇØÁÖ¼¼¿ä", "ºñ¹Ð¹øÈ£¸¦ È®ÀÎÇØ ÁÖ¼¼¿ä", "ºñ¹Ð¹øÈ£¸¦ È®ÀÎÇØ ÁÖ¼¼¿ä", "ÀÌ¸§À» È®ÀÎÇØ ÁÖ¼¼¿ä", "»ýÀÏÀ» È®ÀÎÇØ ÁÖ¼¼¿ä",
                  "emailÀ» È®ÀÎÇØ ÁÖ¼¼¿ä" }; // ¿¡·¯³»¿ë

            while (true) { //while ¹®À» µ¹·Á¼­ À¯È¿¼º °Ë»ç ÁøÇà
               int i = 0;   //id¿Í À¯È¿¼º °Ë»ç ¹è¿­, ¿¡·¯ ¾Ë¸²¸Þ¼¼Áö ¹è¿­ ÀÎµ¦½º°ª Ç¥Çö ¿ë º¯¼ö
               int j = -1;
               if(true) {
               if (Pattern.matches(regex[i], joinChk[i])) { //ÆÐÅÏÀÌ ÀÏÄ¡ÇÏ¸é data¿¡ ÀúÀå
//                  data.add(id);
                  dto.setId(id);
               } else { //ÀÏÄ¡ ÇÏÁö ¾ÊÀ» ½Ã idÈ®ÀÎ ¸Þ¼¼Áö Ã¢ 
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  j--;
                  break;
               }
               i++;

               if (Pattern.matches(regex[i], joinChk[i])) { //ÆÐÅÏÀÌ ÀÏÄ¡ÇÏ¸é data¿¡ ÀúÀå
                  
               } else { //ÀÏÄ¡ ÇÏÁö ¾ÊÀ» ½Ã idÈ®ÀÎ ¸Þ¼¼Áö Ã¢ 
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;   //id Åë°ú½Ã i¸¦ Áõ°¡½ÃÄÑ ´ÙÀ½ ¹è¿­ ¼ø¼­ ÁøÇà
               if (Pattern.matches(regex[i], joinChk[i])) {
                  if (pw2.equals(pw))
//                     data.add(pw);
                  dto.setPw(pw);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;
               if (Pattern.matches(regex[i], joinChk[i])) {
//                  data.add(name);
                  dto.setName(name);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;

               if (Pattern.matches(regex[i], joinChk[i])) {
//                  data.add(birth);
                  dto.setBirth(birth);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;
               
               if (Pattern.matches(regex[i], joinChk[i])) {
//                  data.add(email);
                  dto.setEmail(email);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               
               }

               System.out.println(data); //¿Ï·á½Ã µ¥ÀÌÅÍ°ª È®ÀÎ
               //System.out.println(new GameUserDAO().insert(dto)); //db¿¡  insert 
               new GameUserDAO().insert(dto);
               JOptionPane.showMessageDialog(null, name + "´Ô È¸¿ø°¡ÀÔ ¿Ï·á");
               
               new RankDAO().insertId(id);
               
               LoginTest lt = new LoginTest();
               dispose();
            //   lt.setVisible(true);
               break;

            }

         }

      }
   };


   public static void main(String[] args) {
//      new JoinTest();

   }
}
   