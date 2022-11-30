package NetworkHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	
	public String IPConnect;
	public int portNoConnect;
	
	public boolean willDisconnect = false, isConnected = false;
	
	public void connectToServer() {
		
		if(isValidInet4Address()) {
			try {
				socket = new Socket(IPConnect, portNoConnect);
				out = new PrintWriter(socket.getOutputStream(), true);
		        isConnected = true;
			} catch(IOException e) {
				e.printStackTrace();
				isConnected = false;
			}
		}
		////else wrong ip address
	}
	
	public void disconnectFromServer() {
		if(socket != null) {
		try {
			socket.close();
			isConnected = false;
		} catch (IOException e) {
			isConnected = true;
	        e.printStackTrace();
		}
		}
	}
	public void sendMessage(String sent) {
		out.println(sent);
	}
	public String recvMessage() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private boolean isValidInet4Address()
    {
		String IPV4_PATTERN =
	            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

	    Pattern pattern = Pattern.compile(IPV4_PATTERN);

	    
	    Matcher matcher = pattern.matcher(IPConnect);
	        return matcher.matches();
	    
    }
}
