import javax.swing.*;

public class Client extends JFrame {
  public Client(){
    JPanel panel = new JPanel();
    JButton diary = new JButton("DIARY");
    JButton chat = new JButton("CHAT");

    panel.add(chat);
    panel.add(diary);
    add(panel);

    setVisible(true);
    setSize(900,600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
