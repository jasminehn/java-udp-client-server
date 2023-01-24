import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class clientDisplay extends JPanel{
	String encoding; //tracks which encoding should be used
	
	private DatagramSocket socket;
	private InetAddress dest;
	
	private int port; //inbound port on the server
	
	JLabel ipLabel = new JLabel("Destination IP");
	JTextField ipField = new JTextField("127.0.0.1", 15); //default is 127.0.0.1
	JLabel portLabel = new JLabel("Port");
	JTextField portField = new JTextField("1234", 10); //default is 1234
	
	JLabel enterStringLabel = new JLabel("Enter a String To Send");
	JTextField enterStringField = new JTextField("Hello World!");
	JLabel saveStringAsLabel = new JLabel("Click to send string as...");
	JButton asUTF8Button = new JButton("UTF-8");
	JButton asUTF16Button = new JButton("UTF-16");
	JButton asUTF32Button = new JButton("UTF-32");
	JLabel checkDestination = new JLabel("");
	
	/**
	 * Creates display for the client
	 * @param width
	 * @param height
	 * @param bgColor
	 */
	public clientDisplay(int width, int height, Color bgColor) {
		
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(bgColor);
		
		this.add(ipLabel);
		this.add(ipField);
		this.add(portLabel);
		this.add(portField);
		
		this.add(enterStringLabel);
		enterStringLabel.setPreferredSize(new Dimension(width - 10, 15));
		enterStringLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(enterStringField);
		enterStringField.setPreferredSize(new Dimension(width - 10, height / 2));
		this.add(saveStringAsLabel);
		this.add(asUTF8Button);
		this.add(asUTF16Button);
		this.add(asUTF32Button);
		this.add(checkDestination);
		checkDestination.setForeground(Color.red);
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		checkDestination.setFont(font1);
		checkDestination.setPreferredSize(new Dimension(width, height / 12));
		checkDestination.setHorizontalAlignment(SwingConstants.CENTER);

		asUTF8Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				encoding = "UTF-8"; //Sets encoding to UTF8
				runcode();
			}			
		});
		
		asUTF16Button.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				encoding = "UTF-16"; //Sets encoding to UTF16
				runcode();
			}			
		});
				
		asUTF32Button.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				encoding = "UTF-32"; //Sets encoding to UTF32
				runcode();
			}			
		});
	}
	
	/**
	 * Checks if a valid destination IP address was entered using regular expression. Returns a boolean 
	 * indicating if the given IP is valid or invalid. Also shows pop-up window if entry is null.
	 * @param ip 
	 * @return true if the IP address matches the regular expression 
	 */
	public boolean validateIP(String ip){ 
		String segment = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])"; //Regular expression for number between 0 to 255
		String regex = segment + "\\." + segment + "\\." + segment + "\\." + segment; //Regular expression for a number between 0 to 255 and followed by a dot, repeated 4 times to be in xxx.xxx.xxx.xxx format
	    Pattern p = Pattern.compile(regex); //compile the regular expression 
	    if (ip == null) {
	    	JOptionPane.showMessageDialog(null, "pls enter an ip address");
	        return false; 
	    } 
    	Matcher m = p.matcher(ip); 
    	return m.matches(); 
    	//source: https://www.geeksforgeeks.org/how-to-validate-an-ip-address-using-regular-expressions-in-java/?ref=lbp
	}
	
	/**
	 * Checks if a valid destination port was entered using regular expression. Returns a boolean 
	 * indicating if the given port is valid or invalid. Also shows pop-up window if entry is null.
	 * @param port
	 * @return true if the port matches the regular expression
	 */
	public boolean validatePort(String port){
		String regex = "^()([1-9]|[1-5]?[0-9]{2,4}|6[1-4][0-9]{3}|65[1-4][0-9]{2}|655[1-2][0-9]|6553[1-5])$"; //Regular expression for a number between 1 and 65535. source: https://www.regextester.com/104146
	    Pattern p = Pattern.compile(regex); //compile the regular expression 
	    if (port == null) { 
	    	JOptionPane.showMessageDialog(null, "pls enter a port");
	        return false; 
	    } 
    	Matcher m = p.matcher(port); 
    	return m.matches();
	}
	
	/**
	 * Runs the UDPclient method using the user-input values. Also uses validateIP and 
	 * validatePort to check if the inputs are valid before executing the code.
	 */
	public void runcode() {
		String dest = ipField.getText();
		String port = portField.getText();
		
		if(validateIP(dest) && validatePort(portField.getText())) {
			UDPclient(dest, Integer.parseInt(port)); //executes UDPclient with stored values
			checkDestination.setText("");
		}else {
			if(!validateIP(dest) && !validatePort(port)) {
				checkDestination.setText("Invalid IP and port!");
				ipField.setText("127.0.0.1");
				portField.setText("1234");
			}else if(!validateIP(dest)) {
				checkDestination.setText("Invalid IP!");
				ipField.setText("127.0.0.1");
			}else if(!validatePort(port)) {
				checkDestination.setText("Invalid port!");
				portField.setText("1234");
			}
		}
		
	}
	
	/**
	 * Creates a UDP client that sends data to the given destination IP at the given port
	 * @param destination - IP in xxx.xxx.xxx.xxx form
	 * @param port
	 */
	public void UDPclient(String destination, int port) {
		this.port = port;
		
		try {
			dest = InetAddress.getByName(destination);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		String line = "";
		byte[] payload = null;
		try {
			line = enterStringField.getText();
			System.out.println("Transmitting " + line);
			payload = line.getBytes(encoding); //sets payload as encoding based on button pressed
			DatagramPacket dp  = new DatagramPacket(payload, payload.length, dest, port);
			
			for(int i=0; i<payload.length; i++) { //prints bytes sent into console
				System.out.format("%02x ", payload[i]);				
			}
			System.out.println();
			
			socket.send(dp);
		}catch (Exception e) {
			System.out.println("Failed to send packet " + e);
		}	
	}
	
}
