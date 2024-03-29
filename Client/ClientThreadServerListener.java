package yg.Client;

import yg.Job;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This thread will listen for the messages from the server
 * saying which jobs are finished. When it receives a finished job, it will
 * print it out
 */

public class ClientThreadServerListener implements Runnable{
    private Socket clientSocket;
    private int clientID;

    public ClientThreadServerListener(Socket clientSocket, int clientID)
    {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
    }

    public void run()
    {
        try (
                ObjectInputStream objectIn = new ObjectInputStream(
                        clientSocket.getInputStream());
        ) {
            Object input;
            while((input = objectIn.readObject()) != null)
            {
                Job finishedJob = (Job) input;

                System.out.println("Received DONE job:  Type: " + finishedJob.getType() + ", ID: " + finishedJob.getID() + " Client: " + finishedJob.getClient());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
