package main.java;

//1871222 ¿Ã»Ò¡ÿ
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainServerGUI {
	private int port;
	private ServerSocket serverSocket;
	private JTextArea t_display;
	private JFrame frame;
	private JButton b_connect, b_disconnect, b_exit;
	private Thread acceptThread = null;
	private Vector<ClientHandler> users = new Vector<ClientHandler>();
	
	public MainServerGUI(int port) {
		frame = new JFrame("ServerGUI");
		
		buildGUI();
		
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		this.port = port;
	}
	
	
	
	private void buildGUI() {
		frame.add(createDisplayPanel(), BorderLayout.CENTER);

		frame.add(createControlPanel(), BorderLayout.SOUTH);
	}

	


	private JPanel createDisplayPanel() {
		JPanel p = new JPanel(new BorderLayout());
      t_display = new JTextArea();
      t_display.setEditable(false); 
      p.add(new JScrollPane(t_display), BorderLayout.CENTER);
      return p;
	}


	private JPanel createControlPanel() {
		JPanel p = new JPanel(new GridLayout(1,0));
		b_connect = new JButton("connect");
		b_connect.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              acceptThread = new Thread(new Runnable() {

					@Override
					public void run() {
						startServer();
					}
              	
              });
              acceptThread.start();
              
              b_disconnect.setEnabled(true);
              b_connect.setEnabled(false);
              
              b_exit.setEnabled(false);

          }
      });
		
		b_disconnect = new JButton("Server");
		b_disconnect.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              disconnect();
              b_disconnect.setEnabled(false);
              b_connect.setEnabled(true);
              b_exit.setEnabled(true);
          }
      });
		
		b_exit = new JButton("exit");
		b_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
      	
      });
		p.add(b_connect);
		p.add(b_disconnect);
		p.add(b_exit);
		
		b_disconnect.setEnabled(false);
		return p;
		
	}
	
	private String getLocalAddr() {
		InetAddress local = null;
		String addr = "";
		try {
			local = InetAddress.getLocalHost();
			addr = local.getHostAddress();
			System.out.println(addr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
		
	}
	
	private void startServer() {
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
          printDisplay("start server" + getLocalAddr());

          while (acceptThread == Thread.currentThread()) {
          	clientSocket = serverSocket.accept();
          	
          	String cAddr = clientSocket.getInetAddress().getHostAddress();
              printDisplay("connected client: " + cAddr + "\n");

              //recieveMessages(clientSocket);
              ClientHandler cHandler = new ClientHandler(clientSocket);
              users.add(cHandler);
              cHandler.start();
			}
		} catch (SocketException e) {
			printDisplay("server socket close");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void disconnect() {

      try {
      	acceptThread = null;
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("server socket close err> "+ e.getMessage());
			System.exit(-1);
		}
      

}
	private void printDisplay(String msg) {
		t_display.append(msg + "\n");
		t_display.setCaretPosition(t_display.getDocument().getLength());
	}

	
	

	
	private class ClientHandler extends Thread {
		private Socket clientSocket;
		private BufferedWriter out;
		private BufferedReader in;
		
		private String uid;
		
		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		private void recieveDatas(Socket cs) {
			try {
				in = new BufferedReader(new InputStreamReader(cs.getInputStream(), "UTF-8"));
				out = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream(), "UTF-8"));
				
				String data;
				while ((data = in.readLine()) != null) {
					
					
					
					printDisplay("data: "+ data);
					broadcasting(data);
					
				}
				users.removeElement(this);
				printDisplay(uid + "disconnected. now user number:" + users.size());
			} catch (IOException e) {
				users.removeElement(this);
				printDisplay(uid + "disconnected. now user number:" + users.size());
			}
			finally {
				try {
					cs.close();
				} catch (IOException e) {
					System.err.println("err> "+ e.getMessage());
					System.exit(-1);
				}
			}
		}
		
		
		private void sendData(String data) {
			try {
				((BufferedWriter)out).write(data + "\n");
				System.out.println(data);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("err> " + e.getMessage());
			}
			
		}
		
		private void broadcasting(String data) {
			for (ClientHandler c : users) {
				c.sendData(data);
			}
			
		}
		@Override
		public void run() {
			recieveDatas(clientSocket);
		}
		
	}
	


	public static void main(String[] args) {
		int port = 54321;

		MainServerGUI server = new MainServerGUI(port);
	}

}
