package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HSumOneClient extends JFrame{

  public HSumOneClient(){
    setTitle("HSumOne Client");
    setVisible(true);
    setSize(900,600);
    setLocationRelativeTo(null);
    JPanel loginPanel = new JPanel(new BorderLayout());
    loginPanel.add(backgroundPanel(), BorderLayout.CENTER);
    loginPanel.add(loginInofGUI(), BorderLayout.SOUTH);
    add(loginPanel);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  public ImagePanel backgroundPanel(){
    ImagePanel imagePanel = new ImagePanel(new ImageIcon("/Users/dongik/Desktop/final-network/hSumOne/images/login.jpg").getImage());
    imagePanel.setBounds(0,0,900,600);
    return imagePanel;
  }
  public JPanel loginInofGUI(){
    JPanel panel = new JPanel();
    JLabel label_id = new JLabel("ID : ");
    JLabel label_password = new JLabel("Password : ");
    JTextField text_id = new JTextField(10);
    JPasswordField text_password = new JPasswordField(10);
    JButton login_button = new JButton("Login");
    JButton register_button = new JButton("register");

    panel.add(label_id);
    panel.add(text_id);
    panel.add(label_password);
    panel.add(text_password);
    panel.add(login_button);
    panel.add(register_button);

    login_button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });
    return panel;
  }


  public static void main(String[] args) {
    new HSumOneClient();
  }
}
