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
         "^[0-9_]{6,6}$", // »ý³â¿ùÀÏ 6±ÛÀÚ
         "^[0-9a-zA-Z_]{2,}@[a-zA-Z]{2,}.(([a-zA-Z]{2,})|([a-zA-Z]{2,}.[a-zA-Z]{2,}))" // ÀÌ¸ÞÀÏ À¯È¿¼º °Ë»ç
   };

   public String id;         // id ´ã¾ÆµÑ °ø°£
   public String pw;         // pw ´ã¾ÆµÑ °ø°£
   public String pw2;        // pwÈ®ÀÎ¶õ 
   public String name;      // ÀÌ¸§
   public String email;      // ¸ÞÀÏ
   public String birth;      // »ýÀÏ

   JTextField JoinIdText;
   JPasswordField passwordField;
   JPasswordField passwordField_1;
   JTextField JoinBirthText;
   JTextField JoinNameText;
   JTextField JoinEmailText;

   JButton btnCancelMake;   //Á¾·á ¹öÆ°
   JButton btnMakeMember;   //È¸¿ø°¡ÀÔ½Ã ¿Ï·á ¹öÆ°
   JButton btnIdChk;      //¾ÆÀÌµð Áßº¹ °Ë»ç
   

   public JoinTest() {
   	getContentPane().setForeground(Color.RED);   // È¸¿ø °¡ÀÔ guiÃ¢

      setTitle("È¸¿ø°¡ÀÔ");
      setIconImage(new ImageIcon("./img/logo.png").getImage());
      getContentPane().setLayout(null);
      setSize(600, 700);            
      setLocationRelativeTo(null);   //gui È­¸é Á¤ Áß¾ÓÀ¸·Î ¸ÂÃçÁÜ
      getContentPane().setLayout(null);
      setResizable(false);   //gui »çÀÌÁî ¼öÁ¤ ºÒ°¡
 

      JLabel lbJoinId = new JLabel("¾ÆÀÌµð");
      lbJoinId.setBounds(98, 95, 74, 27);
      getContentPane().add(lbJoinId);

      
      JLabel lbJoinPw = new JLabel("ºñ¹Ð¹øÈ£");
      lbJoinPw.setBounds(98, 145, 74, 27);
      getContentPane().add(lbJoinPw);

      JLabel lbJoinPwC = new JLabel("ºñ¹Ð¹øÈ£ È®ÀÎ");
      lbJoinPwC.setBounds(98, 195, 122, 27);
      getContentPane().add(lbJoinPwC);
      
      JLabel lbName = new JLabel("ÀÌ¸§");
      lbName.setBounds(98, 245, 74, 27);
      getContentPane().add(lbName);


      JLabel lbJoinBirth = new JLabel("»ý³â¿ùÀÏ");
      lbJoinBirth.setBounds(98, 295, 74, 27);
      getContentPane().add(lbJoinBirth);

      JLabel lbJoinEmail = new JLabel("ÀÌ¸ÞÀÏ");
      lbJoinEmail.setBounds(98, 345, 74, 27);
      getContentPane().add(lbJoinEmail);

      JoinIdText = new JTextField();

      JoinIdText.setBounds(223, 95, 187, 27);
      JoinIdText.setColumns(10);
      getContentPane().add(JoinIdText);

      passwordField = new JPasswordField();
      passwordField.setBounds(223, 145, 187, 27);
      getContentPane().add(passwordField);

      passwordField_1 = new JPasswordField();
      passwordField_1.setBounds(223, 195, 187, 27);
      getContentPane().add(passwordField_1);

      JoinNameText = new JTextField();
      JoinNameText.setColumns(10);
      JoinNameText.setBounds(223, 245, 187, 27);
      getContentPane().add(JoinNameText);

      JoinBirthText = new JTextField();
      JoinBirthText.setColumns(10);
      JoinBirthText.setBounds(223, 295, 187, 27);
      getContentPane().add(JoinBirthText);

      JoinEmailText = new JTextField();
      JoinEmailText.setColumns(10);
      JoinEmailText.setBounds(223, 345, 187, 27);
      getContentPane().add(JoinEmailText);

      btnCancelMake = new JButton("Á¾·á");
      btnCancelMake.setBackground(Color.LIGHT_GRAY);
      btnCancelMake.setBounds(313, 435, 129, 29);
      getContentPane().add(btnCancelMake);
      btnCancelMake.addActionListener(butAct);

      btnMakeMember = new JButton("È¸¿ø°¡ÀÔ");
      btnMakeMember.setBackground(Color.LIGHT_GRAY);
      btnMakeMember.setBounds(142, 435, 129, 29);
      getContentPane().add(btnMakeMember);
      btnMakeMember.addActionListener(butAct);
      btnMakeMember.setEnabled(false);

      btnIdChk = new JButton("Áßº¹È®ÀÎ");
      btnIdChk.setBackground(Color.LIGHT_GRAY);
      btnIdChk.setBounds(424, 95, 108, 27);
      getContentPane().add(btnIdChk);
      
      JLabel lblNewLabel = new JLabel("¿µ¹® ¶Ç´Â ¼ýÀÚ·Î 5±ÛÀÚ ÀÌ»ó");
      lblNewLabel.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel.setForeground(Color.BLACK);
      lblNewLabel.setBounds(223, 120, 187, 21);
      getContentPane().add(lblNewLabel);
      
      JLabel lblNewLabel_1 = new JLabel("¿µ¹®,¼ýÀÚ,Æ¯¼ö¹®ÀÚ Á¶ÇÕ 8±ÛÀÚ ÀÌ»ó");
      lblNewLabel_1.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_1.setForeground(Color.BLACK);
      lblNewLabel_1.setBounds(223, 170, 187, 21);
      getContentPane().add(lblNewLabel_1);
      
      JLabel lblNewLabel_3 = new JLabel("2~4±ÛÀÚ");
      lblNewLabel_3.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_3.setForeground(Color.BLACK);
      lblNewLabel_3.setBounds(223, 270, 187, 21);
      getContentPane().add(lblNewLabel_3);
      
      JLabel lblNewLabel_4 = new JLabel("»ý³â¿ùÀÏ ex) 901211");
      lblNewLabel_4.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_4.setForeground(Color.BLACK);
      lblNewLabel_4.setBounds(223, 320, 187, 21);
      getContentPane().add(lblNewLabel_4);
      
      JLabel lblNewLabel_5 = new JLabel("ex) ¾ÆÀÌµð@µµ¸ÞÀÎ ÁÖ¼Ò");
      lblNewLabel_5.setFont(new Font("±¼¸²", Font.PLAIN, 11));
      lblNewLabel_5.setForeground(Color.BLACK);
      lblNewLabel_5.setBounds(223, 370, 187, 21);
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
                  JoinIdText.setEditable(false);
                  btnMakeMember.setEnabled(true);
               }
            
         }

         if (e.getSource() == btnMakeMember) {   //È¸¿ø °¡ÀÔ 
            id = JoinIdText.getText().trim();   
            pw = passwordField.getText().trim();
            pw2 = passwordField_1.getText().trim();
            name = JoinNameText.getText().trim();
            birth = JoinBirthText.getText().trim();
            email = JoinEmailText.getText().trim();
            
            String[] joinChk = { id, pw, pw2, name, birth, email }; // À¯È¿¼º °Ë»ç¸¦ À§ÇØ ¹è¿­Çü
            String[] pppChk = { "id¸¦ È®ÀÎ ÇØÁÖ¼¼¿ä", "ºñ¹Ð¹øÈ£¸¦ È®ÀÎÇØ ÁÖ¼¼¿ä", "ºñ¹Ð¹øÈ£¸¦ È®ÀÎÇØ ÁÖ¼¼¿ä", "ÀÌ¸§À» È®ÀÎÇØ ÁÖ¼¼¿ä", "»ýÀÏÀ» È®ÀÎÇØ ÁÖ¼¼¿ä",
                  "emailÀ» È®ÀÎÇØ ÁÖ¼¼¿ä" }; // ¿¡·¯³»¿ë

            while (true) { //while ¹®À» µ¹·Á¼­ À¯È¿¼º °Ë»ç ÁøÇà
               int i = 0;   //id¿Í À¯È¿¼º °Ë»ç ¹è¿­, ¿¡·¯ ¾Ë¸²¸Þ¼¼Áö ¹è¿­ ÀÎµ¦½º°ª Ç¥Çö ¿ë º¯¼ö
               int j = -1;
               if(true) {
               if (Pattern.matches(regex[i], joinChk[i])) { //ÆÐÅÏÀÌ ÀÏÄ¡ÇÏ¸é data¿¡ ÀúÀå
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
                  dto.setPw(pw);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;
               if (Pattern.matches(regex[i], joinChk[i])) {
                  dto.setName(name);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;

               if (Pattern.matches(regex[i], joinChk[i])) {
                  dto.setBirth(birth);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               i++;
               
               if (Pattern.matches(regex[i], joinChk[i])) {
                  dto.setEmail(email);
               } else {
                  JOptionPane.showMessageDialog(null, pppChk[i]);
                  break;
               }
               
               }

               new GameUserDAO().insert(dto);
               JOptionPane.showMessageDialog(null, name + "´Ô È¸¿ø°¡ÀÔ ¿Ï·á");
               
               new RankDAO().insertId(id);
               
               LoginTest lt = new LoginTest();
               dispose();
               break;

            }

         }

      }
   };

}
   