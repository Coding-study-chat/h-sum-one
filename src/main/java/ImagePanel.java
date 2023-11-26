import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
  private Image img;
  public ImagePanel(Image img){
    this.img = img;
    this.setSize(img.getWidth(null), img.getHeight(null));
    this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
  }

  public void paintComponent(Graphics g){
    g.drawImage(img,0,0,null);
  }
}
