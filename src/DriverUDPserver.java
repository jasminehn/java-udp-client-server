import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class DriverUDPserver {
	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int panelWidth = screen.width;
		int panelHeight = screen.height;
		
		serverDisplay serverDisplay = new serverDisplay(panelWidth/2, panelHeight/2, Color.white);
		JFrame serverFrame = new JFrame("String Receiver Server");
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.add(serverDisplay);
		serverFrame.pack();
		serverFrame.setLocation(550, 100);
		serverFrame.setVisible(true);
		
		serverDisplay.UDPserver(1234); //default is 1234
	}
}
