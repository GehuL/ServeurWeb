class WebServerApplication
{
	public static void main(String[] args)
	{
		final int port = 80;
		System.out.println("Démarrage du serveur sur le port " + port);
		WebServer server = new WebServer();
		server.run(port);
	}
}