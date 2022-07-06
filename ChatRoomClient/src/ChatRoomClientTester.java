import javax.swing.JFrame;

public class ChatRoomClientTester {

	public static void main(String[] args) {
		ChatRoomClient charlie;
		charlie = new ChatRoomClient("127.0.0.1");
		charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		charlie.startRunning();
		
	}

}
