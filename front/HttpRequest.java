import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class HttpRequest
{	
    private EMethods method;
    
    private String url;
    
    private String version;
    
    public HttpRequest(Socket socket) throws IOException, IllegalArgumentException
    {
    	readClientRequest(socket);
    }

    private void readClientRequest(Socket socket) throws IOException, IllegalArgumentException
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String[] response = input.readLine().split(" ");
        
        if(response.length != 3)
        	throw new IllegalArgumentException("Header bad format");
        
        this.method = EMethods.valueOf(response[0]);
        this.url = response[1].substring(1); // Enleve le slash
        this.version = response[2];
    }
    
    public EMethods getMethod()
    {
        return this.method;
    }

    public String getUrl()
    {
        return this.url;
    }
    
    public String getVersion()
    {
    	return this.version;
    }
    
    public String toString()
    {
    	return getMethod().toString() + " " + getUrl() + " " + getVersion();
    }
}