package servClasses;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SocketProcessor implements Runnable {

	private Socket s;
	private InputStream is;
	private OutputStream os;
	String line = null;
	public SocketProcessor(Socket s) throws IOException {

		this.s = s;
		this.is = s.getInputStream();
		this.os = s.getOutputStream();
	}

	@Override
	public void run() {
		try {
		//	DataInputStream in = new DataInputStream(s.getInputStream());
		//	DataOutputStream out = new DataOutputStream(s.getOutputStream());
			readInputHeaders();	
		writeResponse("<html><div width='100%' height='100%' align='center' >"
					+ "<div style='width:300px;height:120px;background:#f4f4f4;border:1px solid #444444;'>"
					+ "Авторизация<br><br>"
					+ "<form action='http://localhost:8085/' method='get'>"
					+ "Login: <input type='login' name='login' style='border:1px solid #ccc;height:22px;width:100px;'></br>"
					+ "Passw: <input type='password' name='password' style='border:1px solid #ccc;height:22px;width:100px;'></br>"
					+ "<input type='submit' name='submit' value='Inter'></form>"
					+ "</div></div></html>");
		} catch (IOException e) {

			e.printStackTrace();
		}finally {
			try {
				s.close();
			}catch (IOException r){
				
			}
		}
		System.out.println("Last2 thread");
		
	}

	private void writeResponse(String s) throws IOException {
     String result =s;
		os.write(result.getBytes());
		os.flush();
			
	}
	private void readInputHeaders() throws IOException  {
	     BufferedReader br = new BufferedReader (new InputStreamReader(is));
	     
	     while(true){
	    	
	    	String st = br.readLine();
	    	System.out.println(st);   
	    	
	    	String REGEX = ".+\\&(password).+";
			Pattern p = Pattern.compile(REGEX);  
	        Matcher m = p.matcher(st); 
	         if(m.matches()){	
	        	 writeResponse(checkUserData(getUser(m.group())));
	        	 os.close();
	         }    
	    	if (s==null || st.trim().length() == 0){
	    		break;
	    	}	
	     }	
		}
	private String checkUserData(String[] user){
		String line = null;
		String[] acceptUser = {"admin","admin"};
		if(acceptUser[0].equals(user[0]) && acceptUser[1].equals(user[1])){
			line = "<html><head><title>authorized page</title></head><body style='color:Yellow; background-color:#66cc66'><h1>Hello :) You have just authorized successfully!</h1><p><a href='http://localhost:8080'>[go back]</a></p><p>Something else</p><p style='color:#ffffff'>Something other</p></body></html>";
		} else
			line = "<center>Sorry. but your login or password incorrect <meta http-equiv='refresh' content='0;url=http://localhost:8080'></center>";
		return line;
		
	}
	
	private String[] getUser(String line){
        String rightLine = line.split("\\?")[1];
        String[] user = new String[2];
        String[] params = rightLine.split("&");
        Pattern pattern1 = Pattern.compile("login=*");
        Pattern pattern2 = Pattern.compile("password=*");
        for (String s : params) {
        	Matcher matcher1 = pattern1.matcher(s);
            Matcher matcher2 = pattern2.matcher(s);
            if (matcher1.find()) {
                user[0]=s.split("=")[1];
            }
            if (matcher2.find()) {
            	user[1]=s.split("=")[1];
            }
        }
        return user;
	}
}

