import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class WebServer
{
    public void run(int port)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(80);
            while(true)
            {
                Socket client = serverSocket.accept();
                System.out.println("Nouvelle connexion");
                new Thread(new RequestProcessor(client)).run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private void readRequest(Socket socket)
    {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = "";
            while((response = input.readLine()) != null && !response.isEmpty())
            {
                System.out.println(response);
            }
            System.out.println("Fin de la requete client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Socket socket)
    {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = "HTTP/1.1 200 OK\n\nHello world\n\r\n\r";
            output.write(response);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}