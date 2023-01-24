import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class serverDisplay extends JPanel{
	private DatagramSocket socket;
	String utf8;
	String utf16;
	String utf32;
	
	JLabel displayStringLabel = new JLabel("Incoming Message:");
	JLabel displayStringBodyLabel = new JLabel("");
	JLabel loadStringAsLabel = new JLabel("Read incoming message as...");
	JButton asUTF8Button = new JButton("UTF-8");
	JButton asUTF16Button = new JButton("UTF-16");
	JButton asUTF32Button = new JButton("UTF-32");

	/**
	 * Creates display for the server
	 * @param width
	 * @param height
	 * @param bgColor
	 */
	public serverDisplay(int width, int height, Color bgColor) {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(bgColor);

		this.add(loadStringAsLabel);
		this.add(asUTF8Button);
		this.add(asUTF16Button);
		this.add(asUTF32Button);
		this.add(displayStringLabel);
		this.add(displayStringBodyLabel);
		displayStringBodyLabel.setPreferredSize(new Dimension(width - 10, height / 2));
		displayStringBodyLabel.setOpaque(true);
		displayStringBodyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		displayStringBodyLabel.setBackground(Color.white);
		
		asUTF8Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayStringBodyLabel.setText("<html>"+utf8+"</html>"); //sets text as UTF8 string from receiveInput()
			}
		});
		
		asUTF16Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayStringBodyLabel.setText("<html>"+utf16+"</html>"); //sets text as UTF16 string from receiveInput()
			}
		});
		
		asUTF32Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayStringBodyLabel.setText("<html>"+utf32+"</html>"); //sets text as UTF32 string from receiveInput()
			}
		});
	}
	
	/**
	 * Receives UDP incoming traffic on the given inbound port
	 * @param port
	 */
	public void UDPserver(int port) {
		try {
			socket = new DatagramSocket(port);
		}catch(Exception e) {
			System.out.println("Failed to create socket " + e);
		}
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				receiveInput();	
			}
		}).start();
	}
	
	/**
	 * Stores received input as strings of different encodings and prints to console
	 */
	public void receiveInput() {
		String line = "";
		byte[] buffer = new byte[65535];
		while(true) {
			try {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				socket.receive(dp);
				
				utf8 = new String(buffer, "UTF-8");
				utf16 = new String(buffer, "UTF-16");
				utf32 = new String(buffer, "UTF-32");
				
				line = new String(buffer, "UTF-8");
				System.out.println("Received: " + line  + " with length " + dp.getLength());
				
				for(int i=0; i<dp.getLength(); i++) { //prints bytes received into console
					System.out.format("%02x ", buffer[i]);
				}
				System.out.println();
				
				buffer = new byte[65535];
			}catch (Exception e) {
				System.out.println("Failed to receive packet " + e);
			}
		}
		
	}
}
