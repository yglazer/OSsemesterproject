package yg.Client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * The client instantiates its writer and listener threads,
 * which listen from the user and master and write to the master.
 */

public class Client {

    public static void main(String[] args) {
        //args = new String[]{"127.0.0.1", "30121", "1"}; // 3rd arg is client id#

        if (args.length != 3)
        {
            System.err.println("Usage: java client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumberC = Integer.parseInt(args[1]);
        int clientID = Integer.parseInt(args[2]);
        System.out.println("My ID: " + clientID);



        try (
                //sockets for connections between client and master (server)
                Socket clientSocket = new Socket(hostName, portNumberC);
                )

        {
            System.out.println("Client is connected. ");

            // array for the client threads:
            ArrayList<Thread> clientThreads = new ArrayList<>();

            // creating the threads
            clientThreads.add(new Thread(new ClientThreadServerListener(clientSocket, clientID)));
            clientThreads.add(new Thread(new ClientThreadServerWriter(clientSocket, clientID)));

            // starting the client threads
            for (Thread t : clientThreads)
            {
                t.start();
            }

            // waiting for all the threads to finish
            for(Thread t : clientThreads)
            {
                try
                {
                    t.join();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();;
                }
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
