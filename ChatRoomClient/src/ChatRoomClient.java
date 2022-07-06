import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ChatRoomClient extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	//Constructor
	public ChatRoomClient(String host) {
		super("Instant Messenger - Client");
		serverIP = host;
		userText =  new JTextField();
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow =  new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	//connect to server
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
			
		}catch(EOFException eofException) {
			
			sendMessage("\n Client terminated the connection");
			
		}catch(IOException ioException) {
			
			ioException.printStackTrace();
			
		}finally {
			closeCrap();
		}
		
	}
	
	//connect to Server
	private void connectToServer() throws IOException {
		showMessage("Attempting to connect to server... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to: "+connection.getInetAddress().getHostName()); 
	}
	
	//setup streams to send and receive messages
	private void setupStreams() throws IOException{
		outputStream =  new ObjectOutputStream(connection.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Your streams are now good to go! \n");
	}
	
	//while chatting with Server
	private void whileChatting() throws IOException{
		ableToType(true);
		do {
			try {
				message = (String) inputStream.readObject();
				showMessage("\n"+message);
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n I don't know that object type");
			}
		}while(!message.equals("SERVER - END"));
	}
	
	//close the streams and sockets
	private void closeCrap (){
		showMessage("\n Closing crap down...");
		ableToType(false);
		try {
			outputStream.close();
			inputStream.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//send messages to server
	private void sendMessage(String message) {
		try {
			outputStream.writeObject("CLIENT - "+message);
			outputStream.flush();
			showMessage("\nCLIENT - "+message);
		}catch(IOException ioException) {
			sendMessage("\n Something went wrong while sending the message! ");
		}
	}
	
	//change/update chatWindow
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(message);
					}
				}
		);
	}
	
	//gives user permisstion to type into the textbox
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
		);
		
	}
}

