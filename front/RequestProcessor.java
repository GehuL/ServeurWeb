import java.io.File;
import java.io.IOException;
import java.net.Socket;

class RequestProcessor implements Runnable
{
	private HttpContext context;
	
	private static final String rootPath = "../frontend/";

	public RequestProcessor(Socket socket)
	{
		this.context = new HttpContext(socket);
	}

	private void process()
	{
		try
		{
			HttpRequest request = this.context.getRequest();

			System.out.println(context.getIP() + ">" + request);

			HttpResponse.Builder responseBuilder = null;
		
			try
			{				
				responseBuilder = this.context.getResponse(200, "OK");
				
				String ext = getExtension(request.getUrl());
				if(ext.length() != 0) // Il y a un fichier
				{
					File file = new File(rootPath + request.getUrl());
					if(!file.canRead())
					{
						responseBuilder = this.context.getResponse(404, "Not Found");
						responseBuilder.setContent("<strong>RESSOURCE NOT FOUND</strong>").addField("Content-Type", "text/html");
				
					}else
					{
					String mime = getMIME(ext);
					responseBuilder.addField("Content-Type", mime);
					responseBuilder.addField("Content-Length", String.valueOf(file.length()));
					responseBuilder.setContent(file);				
					}
					}

			} catch (IOException e)
			{
				responseBuilder = this.context.getResponse(404, "Not Found");
				responseBuilder.setContent("<strong>RESSOURCE NOT FOUND</strong>").addField("Content-Type", "text/html");
			}				
			
			if(responseBuilder != null)
			{
				HttpResponse response = responseBuilder.build();	
				System.out.println(context.getIP() + "<" + response);
				response.send();
			}
		

		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				this.context.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static String getExtension(String url)
	{
		String extension = url.substring(url.lastIndexOf(".") + 1);
		return extension;
	}
	
	private String getMIME(String extension)
	{
		switch(extension)
		{
		case "png":
			return "image/png";
		case "html":
			return "text/html";
		case "css":
			return "text/css";
		case "mp4":
			return "video/mp4";
		case "svg":
			return "image/svg+xml";
		case "txt":
		default:
			return "application/octet-stream";
		}
	}

	@Override
	public void run()
	{
		process();
	}
}