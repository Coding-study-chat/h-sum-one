import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client extends JFrame {
  Thread myThread;
  public Client(){
    JPanel panel = new JPanel();
    JButton diary = new JButton("DIARY");
    JButton chat = new JButton("CHAT");

    chat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        myThread = new Thread(new Runnable() {
          @Override
          public void run() {
            new ClientChat("localhost", 54321);
          }
        });
        myThread.start();
      }
    });

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
