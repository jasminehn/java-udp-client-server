import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class DriverUDPclient {
	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int panelWidth = screen.width;
		int panelHeight = screen.height;
		
		clientDisplay clientDisplay = new clientDisplay(panelWidth/3, panelHeight/2, Color.white);
		JFrame clientFrame = new JFrame("String Receiver Client");
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.add(clientDisplay);
		clientFrame.pack();
		clientFrame.setLocation(100, 100);
		clientFrame.setVisible(true);
	}
}