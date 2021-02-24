import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerEcho {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    private List<Users> usersList = new ArrayList<>();

    public ServerEcho(int port) {
        //fake tmp user
        usersList.add(new Users("Bobby1", "123", 1000));
        usersList.add(new Users("Bobby2", "1234", 1001));
        usersList.add(new Users("Bobby3", "12345", 1002));

        try {
            server = new ServerSocket(port);
            System.out.println("ServerEcho started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String loginData = in.readUTF();
            String username = loginData.split(":")[0];
            String pass = loginData.split(":")[1];

            for (Users users : usersList) {
                if (users.getUsername().equals(username) && users.getPassword().equals(pass)) {
                    out.writeUTF("w:Velkommen til systemet");
                    out.writeUTF("t:" + (users.getUserId()));
                    break;
                } else {
                    //forkert password
                    out.writeUTF("e:Forkert bruger eller password!");
                    System.out.println("Closing connection");
                    socket.close();
                    in.close();
                    out.close();
                    System.exit(0);
                }
            }

            String line = "";
            while (!line.equals("Over")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                    out.writeUTF(line);

                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
            out.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        ServerEcho serverEcho = new ServerEcho(5000);
    }
}