package yg.Client;

import yg.Job;

import java.io.*;
import java.net.Socket;

/**
 * Client's WRITER threads write to the master. It takes the user's
 * input from another thread, and sends it to the master, who then
 * decides which slave to send it to.
 */
public class ClientThreadServerWriter implements Runnable{
    private Socket clientSocket;
    private int clientID;

    boolean runThread = true;
    public ClientThreadServerWriter(Socket clientSocket, int clientId)
    {
        this.clientSocket = clientSocket;
        this.clientID = clientId;
    }

    public void run() {
        try (
                // this output stream will send jobs to master.
                ObjectOutputStream objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
                ) {
            while(runThread)
            {
                String user;
                int jobId = 0;
                System.out.println("Please enter jobs of type a or b: ");

                while((user = userIn.readLine()) != null)
                {
                    if (user.equals("a") || user.equals("b"))
                    {
                        System.out.println("Received: User entered a new job of type: " + user);
                        char type = user.charAt(0);  // Extract the first character

                        // this is the new job OBJECT that it will send to master
                        Job newJob = new Job(type, jobId, clientID);
                        System.out.println("New job created. Type: " + type + ", ID: " + jobId + " Client: " + clientID);

                        jobId++;

                        //write message to console:
                        System.out.println("Sending to master. \n");

                        // this sends the newJob to the master
                        objectOut.writeObject(newJob);
                        objectOut.flush();

                    }
                    else
                    {
                        System.out.println("Invalid entry. Please enter a type of job (a or b): ");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
