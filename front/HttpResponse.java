import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

class HttpResponse
{
	private OutputStream output;

	private String header;

	private byte[] content;
	
	private File file;

	public HttpResponse(Socket socket) throws IOException
	{
		this.output = socket.getOutputStream();
	}

	public void send() throws IOException
	{
		this.output.write(header.getBytes());
		this.output.flush(); // Envoie le header

		if(file != null && file.canRead())
		{
			FileInputStream input = new FileInputStream(this.file);
			this.output.write(input.readAllBytes());
			input.close();
		}else if(this.content != null)
		{
			this.output.write(content);
			
		}
		
		this.output.flush();
	}

	public String toString()
	{
		return this.header;
	}

	public static class Builder
	{
		private Socket socket;

		private ArrayList<String> fields;

		private String header;

		private String content;
		
		private File fileContent;

		public Builder(int code, String message, Socket socket)
		{
			this.fields = new ArrayList<String>();
			this.header = "HTTP/1.1 " + String.valueOf(code) + " " + message + "\n";
			this.socket = socket;
		}

		public Builder addField(String type, String parameter)
		{
			this.fields.add(type + ": " + parameter);
			return this;
		}

		public Builder setContent(String data)
		{
			this.content = data;
			this.fileContent = null;
			return this;
		}

		public Builder setContent(File file) throws IOException
		{
			this.fileContent = file;
			this.content = null;
			return this;
		}

		public HttpResponse build() throws IOException
		{
			HttpResponse response = new HttpResponse(socket);
			response.header = this.header;

			for (String field : fields)
				response.header += field + "\n";

			if (this.content != null)
			{
				response.header += "\n";
				response.content = this.content.getBytes();
			}else if(this.fileContent != null)
			{
				response.header += "\n";
				response.file = this.fileContent;
			}

			return response;
		}
	}
}