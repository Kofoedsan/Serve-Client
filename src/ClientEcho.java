import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientEcho {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port
    public ClientEcho(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        //Login
        Scanner scan = new Scanner(System.in);
        String user = scan.nextLine();
        String pass = scan.nextLine();

        out.writeUTF(user + ":" + pass);

        String loginResponse = input.readUTF();
        String loginToken;
        boolean loggedIn = false;

        if (loginResponse.contains("w:")) {
            loggedIn = true;
            loginToken = input.readUTF();
            System.out.println(loginResponse);
            System.out.println(loginToken);
        }
        if (loginResponse.contains("e:")) {
            loggedIn = false;
            System.out.println(loginResponse.split(":")[1]);
        }



        System.exit(0);
        out.close();
        input.close();
        socket.close();

        String line = "";
        while ((!line.equals("Over") || loggedIn))  {
            try {
                line = input.readLine();
                out.writeUTF(line);
            } catch (IOException i) {
                System.out.println(i);
            }
        }
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) throws IOException {
        ClientEcho clientEcho = new ClientEcho("127.0.0.1", 5000);
    }
}