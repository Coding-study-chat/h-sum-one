import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends JFrame {

  BufferedWriter bw;
  public Register(){
    setTitle("REGISTER");

    JLabel title =
            new JLabel("REGISTER", JLabel.CENTER);

    title.setFont(new Font("휴먼편지체", Font.BOLD, 30));

    JButton join = new JButton("REGISTER");
    JButton cancel = new JButton("CANCLE");

    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new Login();
        dispose();
      }
    });

    JTextField id = new JTextField(10);
    JPasswordField pwd = new JPasswordField(10);
    JTextField port = new JTextField(10);

    join.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = id.getText();
        String password = pwd.getText();
        int pt = Integer.parseInt(port.getText());

        String sql = "insert into users(username, password, port) values (?,?,?)";

        try {
          Connection conn = getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql);

          pstmt.setString(1,username);
          pstmt.setString(2,password);
          pstmt.setInt(3,pt);

          int r = pstmt.executeUpdate();
          System.out.println("변경된 row " + r);
          JOptionPane.showMessageDialog(null, "회원 가입 완료!", "회원가입", 1);
        } catch (SQLException ex) {
          System.out.println("DB연동 오류");
          throw new RuntimeException(ex);
        }
      }
    });

    // form panel
    JPanel idPanel = new JPanel();
    idPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    idPanel.add(new JLabel("아이디 : "));
    idPanel.add(id);

    JPanel pwdPanel = new JPanel();
    pwdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    pwdPanel.add(new JLabel("비밀번호 : "));
    pwdPanel.add(pwd);

//    JPanel localPanel = new JPanel();
//    localPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//    localPanel.add(new JLabel("localhost : "));

    JPanel portPanel = new JPanel();
    portPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    portPanel.add(new JLabel("port : "));
    portPanel.add(port);


    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridLayout(4, 1));
    formPanel.add(idPanel);
    formPanel.add(pwdPanel);
//    formPanel.add(localPanel);
    formPanel.add(portPanel);

    // radio + form panel
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new FlowLayout());
    contentPanel.add(formPanel);

    // button panel
    JPanel panel = new JPanel();
    panel.add(join);
    panel.add(cancel);

    add(title, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
    add(panel, BorderLayout.SOUTH);

    setBounds(200, 200, 250, 300);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public Connection getConnection() throws SQLException {
    Connection conn = null;
    String url = "jdbc:mysql://127.0.0.1:3306/hsumone_db";

    conn = DriverManager.getConnection(url, "root", "toMist0930!");

    return conn;
  }
}