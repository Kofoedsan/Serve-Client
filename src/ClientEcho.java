import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientEcho
{
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataOutputStream out     = null;
    private boolean loggedIn = false;

    // constructor to put ip address and port
    public ClientEcho(String address, int port) throws IOException {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            input  = new DataInputStream(socket.getInputStream());
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        Scanner scanner = new Scanner(System.in);

        // forsøge at logge ind her....

        System.out.print("Username: ");
        String user = scanner.nextLine(); // tag imod brugernavn

        System.out.print("Password: ");
        String pass = scanner.nextLine(); // tag imod password

        // send til server
        out.writeUTF(user+":"+pass);

        // modtag svar fra server
       /*
           w:Velkommen...
           t:userId
           e:Forkert...
       */

        String loginResponse = input.readUTF();
        String loginToken;

        // er dette en velkomst besked???
        if(loginResponse.contains("w:")) {
            loginToken = input.readUTF();
            loggedIn = true;
        }

        // er dette forkert login???
        if(loginResponse.contains("e:")) {
            System.out.println(loginResponse.split(":")[1]);
        }

       /*
       Tråd eksempel
       Thread sendMessage = new Thread(new Runnable()
       {
           @Override
           public void run() {
               while (true) {

               }
           }
       });
       */

        String line = "";
        while (!line.equals("Over") || loggedIn)
        {
            try
            {
                String cmd = scanner.nextLine();
                out.writeUTF(cmd);

                line = input.readUTF();
                System.out.println(line);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }

        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[]) throws IOException {
        ClientEcho client = new ClientEcho("127.0.0.1", 5000);
    }

}

