package main.java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
  public Login(){
    JPanel loginPanel = new JPanel();
    JLabel id = new JLabel("id");
    JTextField idField = new JTextField(10);

    JLabel pw = new JLabel("pw");
    JPasswordField pwField = new JPasswordField(10);

    JButton loginButton = new JButton("login");
    JButton registerButton = new JButton("register");

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //new Client();
    	new MainGUI("localhost", 54321);
        dispose();
      }
    });

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new Register();
    	
        setVisible(false);
      }
    });

    loginPanel.add(id);
    loginPanel.add(idField);
    loginPanel.add(pw);
    loginPanel.add(pwField);
    loginPanel.add(loginButton);
    loginPanel.add(registerButton);

    add(loginPanel);

    setVisible(true);
    setSize(900,600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    new Login();
  }
}
