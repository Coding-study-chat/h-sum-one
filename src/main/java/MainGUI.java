package main.java;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.time.Month;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainGUI extends JFrame{
	protected static String setDDay;
	private Image backgroundImage;
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private Writer out;
	private Reader in;
	private Thread receiveThread = null;
	private String inScore ="0";
	private String inDate;
	private int year;
	private int month;
	private int day;
	private long ddaynum;
	static String firstDay;
	
	private static JLabel dDayLabel;
	static JLabel scoreLabel;
	private static JButton ddaybt;
	private static JButton hansungmong;
	
	public MainGUI(String serverAddress, int serverPort){
		super("MainGUI");
		
		setSize(1280, 720);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		
		backgroundImage = new ImageIcon(getClass().getResource("exam.jpg")).getImage();
		
		buildGUI();
		
		setVisible(true);
		
		
		
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		
		try {
			connectToServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void buildGUI() {
		MyPanel panel = new MyPanel();
		panel.setLayout(null);
		
		dDayLabel = new JLabel("Enter your first date date");
		scoreLabel = new JLabel(inScore);
		
		Font customFont = new Font("", Font.BOLD, 14);
		
        dDayLabel.setFont(customFont);
        scoreLabel.setFont(customFont);
		
		JButton chat = new JButton(new ImageIcon(getClass().getResource("chat.png")));
		chat.setBorderPainted(false);
		chat.setContentAreaFilled(false);
		chat.setFocusPainted(false);
		/*chat.addActionListener(new ActionListener() {
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
	    });*/
		JButton diary = new JButton(new ImageIcon(getClass().getResource("calender.png")));
		diary.setBorderPainted(false);
		diary.setContentAreaFilled(false);
		diary.setFocusPainted(false);
		diary.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dDayLabel.getText() == "Enter your first date date") {
					JOptionPane.showMessageDialog(null, "Enter your first date date");
				}else
				new CalenderGUI();
			}
		});
		
		ddaybt = new JButton(new ImageIcon(getClass().getResource("ddaybtn.png")));
		ddaybt.setBorderPainted(false);
		ddaybt.setContentAreaFilled(false);
		ddaybt.setFocusPainted(false);
		ddaybt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DateDayGUI(serverAddress,serverPort);
				
			}
		});
		
		hansungmong = new JButton(new ImageIcon(getClass().getResource("hsmong1.png")));
		hansungmong.setBorderPainted(false);
		hansungmong.setContentAreaFilled(false);
		hansungmong.setFocusPainted(false);
		hansungmong.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	sendScore(1);
		        
		        
		        
		        
		    }
		});
		
		
		panel.add(dDayLabel);
		panel.add(scoreLabel);
		panel.add(chat);
		panel.add(diary);
		panel.add(ddaybt);
		panel.add(hansungmong);
	    
		dDayLabel.setBounds(50, 10, 200, 50);
		scoreLabel.setBounds(50, 70, 50, 50);

	    chat.setBounds(1060, 15, 70, 70);
	    diary.setBounds(1160, 10, 70, 70);
	    
	    ddaybt.setBounds(960, 10, 70, 70);
	    hansungmong.setBounds(400, 120, 400, 500);
	    
	    add(panel);
	    
	}
	
	class MyPanel extends JPanel{
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
        }
    }
	
	
	
	
	public static void setDDay(String dDay) {
        dDayLabel.setText(dDay);
    }
	
	public String getDday(int _year, int _month, int _day){
        try {
            
            Calendar Today = Calendar.getInstance();
            Calendar Dday = Calendar.getInstance();
            
            Dday.set(_year, _month -1 , _day);
            
            long dday = Dday.getTimeInMillis() / (24*60*60*1000);
            long today = Today.getTimeInMillis() / (24*60*60*1000);
            
            ddaynum = today - dday + 1;
            if(ddaynum < 0) {
            	return "-";
            }else
			return "D+ " + ddaynum;
 
        } catch (Exception e) {
            return "";
        }
	}
	
	
	
	
	
	private void connectToServer() throws UnknownHostException, IOException{
		socket = new Socket();
		SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
		socket.connect(sa, 3000);
		 
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					
		receiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(receiveThread == Thread.currentThread()) {
					receiveScore();
					//receieveDate();
				}
			}
						
		});
		receiveThread.start();

	 }

	    private void disconnect() {

	            try {
					receiveThread = null;
					socket.close();
				} catch (IOException e) {
					System.err.println("err> "+ e.getMessage());
					System.exit(-1);
				}
	            

	    }

	    private void sendScore(int num) {
	    	System.out.println("send");
	    	String score;
	    	score = Integer.toString(Integer.parseInt(inScore) + num);
	    	
	        if(score.isEmpty()) return;
	        
	        try {
	        	
				((BufferedWriter)out).write(score + "\n");
				out.flush();
			} catch (IOException e) {
				System.err.println("err> " + e.getMessage());
				System.exit(-1);
			}
	        
	    	
	    }
	    
		
	    
	    private void receiveScore() {
	    	String inData = null;
	    	try {
				inData = ((BufferedReader)in).readLine();
			} catch (IOException e) {
				System.err.println("err> " + e.getMessage());
			}
			/*
			 * if (inData != null && inData.contains("/")){ receieveDate(); }else
			 */{
				System.out.println("recieveScore");
	    	
	    		inScore = inData;
				scoreLabel.setText(inScore);
				System.out.println(inScore);
			
	    	if(Integer.parseInt(scoreLabel.getText()) >= 0 && Integer.parseInt(scoreLabel.getText()) < 50) {
	    		ImageIcon newIcon = new ImageIcon(MainGUI.class.getResource("hsmong1.png"));
	            hansungmong.setIcon(newIcon);
	    	}else if(Integer.parseInt(scoreLabel.getText()) >= 50 && Integer.parseInt(scoreLabel.getText()) < 100) {
	        	ImageIcon newIcon = new ImageIcon(MainGUI.class.getResource("hsmong2.png"));
	            hansungmong.setIcon(newIcon);
	        }else if(Integer.parseInt(scoreLabel.getText()) >= 100) {
	        	ImageIcon newIcon = new ImageIcon(MainGUI.class.getResource("hsmong3.png"));
	            hansungmong.setIcon(newIcon);
	        }
	    	
			}	    
	    }
	    
		/*
		 * private void receieveDate() { System.out.println("recieveDate");
		 * 
		 * try { String inDate = ((BufferedReader)in).readLine(); firstDay = inDate;
		 * 
		 * String[] YMD = inDate.split("/"); year = Integer.parseInt(YMD[0]); month =
		 * Integer.parseInt(YMD[1]); day = Integer.parseInt(YMD[2]);
		 * 
		 * dDayLabel.setText(getDday(year,month,day)); System.out.println(inDate); }
		 * catch (IOException e) { System.err.println("클라이언트 일반 수신 오류> " +
		 * e.getMessage()); }
		 * 
		 * }
		 */
	

	public static void main(String[] args) {
		String serverAddress = "localhost";
        int serverPort = 54321;

        new MainGUI(serverAddress, serverPort);
    }
}
