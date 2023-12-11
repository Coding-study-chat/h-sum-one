package main.java;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DateDayGUI extends JFrame{
	private String serverAddress;
	private int serverPort;
	private JTextField input;
	
	private int year;
	private int month;
	private int day;
	private Socket socket;
	private Writer out;
	
	public DateDayGUI(String serverAddress, int serverPort) {
		super("DateDayGUI");
		
		buildGUI();
		
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
            }
        });
		
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
		setLayout(null);

        JLabel text = new JLabel("First Dated");
        add(text);
        
        JLabel text2 = new JLabel("(yyyy/mm/dd)");
        add(text2);

        input = new JTextField(30);
        add(input);

        JButton submitButton = new JButton("submit");
        submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputString = input.getText();
				
				sendScore(5);
				//sendDate(inputString);
                
				disconnect();
				setVisible(false);
                }
			
		});
        add(submitButton);
        
        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
			
		});
        add(cancelButton);
        
        text.setBounds(150, 10, 100, 50);
        text2.setBounds(85, 40, 300, 50);
        input.setBounds(40, 100, 300, 30);
        submitButton.setBounds(160, 150, 60, 30);
        cancelButton.setBounds(160, 200, 60, 30);
        
        
	}
	
	
	
	private void connectToServer() throws UnknownHostException, IOException{
		socket = new Socket();
		SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
		socket.connect(sa, 3000);
		 
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
					
		

	 }

	    private void disconnect() {

	            try {
					socket.close();
				} catch (IOException e) {
					System.err.println("err> "+ e.getMessage());
					System.exit(-1);
				}
	            

	    }

	    private void sendScore(int num) {
	    	System.out.println("sendScore");
	        String score = Integer.toString(Integer.parseInt(MainGUI.scoreLabel.getText()) + num) ;
	        if(score.isEmpty()) return;
	        
	        try {
	        	
				((BufferedWriter)out).write(score + "\n");
				out.flush();
			} catch (IOException e) {
				System.err.println("err> " + e.getMessage());
				System.exit(-1);
			}
	        
	    }
	    
	    private void sendDate(String date) {
	    	System.out.println("sendScore");
	        if(date.isEmpty()) return;
	        
	        try {
	        	
				((BufferedWriter)out).write(date + "\n");
				out.flush();
			} catch (IOException e) {
				System.err.println("err> " + e.getMessage());
				System.exit(-1);
			}
	        
	    }
	    
		
	    
	    
	
	
	
	    
	    public static void main(String[] args) {
	        String serverAddress = "localhost";
	        int serverPort = 54321;

	        new DateDayGUI(serverAddress, serverPort);
	    }

}

