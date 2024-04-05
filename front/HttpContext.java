import java.io.IOException;
import java.net.Socket;

public class HttpContext
{
	private Socket socket;
	
	public HttpContext(Socket socket)
	{
		this.socket = socket;
	}
	
	public String getIP()
	{
		return socket.getInetAddress().getHostAddress();
	}
	
	public Socket getClient()
	{
		return this.socket;
	}
	
	public HttpRequest getRequest() throws IllegalArgumentException, IOException
	{
		return new HttpRequest(this.socket);
	}
	
	public HttpResponse.Builder getResponse(int code, String message) throws IOException
	{
		return new HttpResponse.Builder(200, "OK", getClient());
	}
	
	public void close() throws IOException
	{
		this.socket.close();
	}
}