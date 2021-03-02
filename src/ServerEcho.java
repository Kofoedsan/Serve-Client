import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerEcho
{
    private Socket socket   = null;
    private ServerSocket server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out     = null;

    private ArrayList<Users> users = new ArrayList<Users>();

    private ArrayList<Book> books = new ArrayList<Book>();


    public ServerEcho(int port)
    {
        // fake user creation...
        users.add(new Users("bob", "123", 1000));
        users.add(new Users("julie", "kage", 2000));
        users.add(new Users("robert", "test", 3000));

        // fake book creation...
        books.add(new Book(1, "Den store haj", "Hajen spiser mange fisk"));
        books.add(new Book(2, "Fortællingen om anden med vanter", "Der var engang en and og den er sød og gik stykker"));
        books.add(new Book(3, "Støpning af glas uden vanter", "Når vanten er klar går glasset i stykker"));




        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out    = new DataOutputStream(socket.getOutputStream());

            // check login
            //String tmpLoginData = "bob:123";
            String tmpLoginData = in.readUTF();

            String user = tmpLoginData.split(":")[0];
            String pass = tmpLoginData.split(":")[1];

            for(Users u : users) {
                if(u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                    // du er logget ind
                    out.writeUTF("w:Velkommen til systemet");
                    out.writeUTF("t:"+String.valueOf(u.getUserId()));
                    break;
                } else {
                    // forkert password og eller brugernavn
                    out.writeUTF("e:Forkert brugernavn og eller password!");
                    socket.close();
                    in.close();
                    out.close();
                    System.exit(0);
                }
            }

            String line = "";
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                   /*
                       id:1
                       title:haj
                       snippet:stykker
                   */

                    // søg efter en bog-id
                    if(line.startsWith("id:")) {

                        System.out.println("Søgning efter id");

                        String tmpId = line.split(":")[1];
                        int id = 0;

                        // tjekke om tmpId indeholder et tal....

                        try {
                            id = Integer.parseInt(tmpId);
                        }catch(Exception err) {
                            out.writeUTF("Wrong number format!");
                        }

                        if(!bookSearch(id)) {
                            out.writeUTF("No such book id!");
                        }

                    }

                    // søg efter bog-titel
                    if(line.startsWith("title:")) {

                        String tmpTitle = line.split(":")[1];

                        if(!bookSearch(tmpTitle)) {
                            out.writeUTF("No book with the title: " + tmpTitle);
                        }

                    }

                    // søg efter bog-snippet
                    if(line.startsWith("snippet:")) {

                    }

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
            out.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public boolean bookSearch(String tmpTitle) throws IOException {
        String response = "";

        for(Book b : books) {
            if(b.getBookTitle().contains(tmpTitle)) {
                response += b.getBookTitle() + " , ";
            }
        }

        if(response != "") {
            out.writeUTF(response);
            return true;
        }
        return false;
    }

    public boolean bookSearch(int id) throws IOException {
        if(id > 0) {
            for(Book b : books) {
                if(b.getBookid() == id) {
                    out.writeUTF(b.getBookTitle());
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String args[])
    {
        ServerEcho server = new ServerEcho(5000);
    }
}