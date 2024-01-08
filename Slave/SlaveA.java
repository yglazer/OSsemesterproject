package yg.Slave;

import yg.Job;

import java.io.*;
import java.net.*;
import java.util.*;

public class SlaveA {

    // a count for the current load:
           /*  when it gets a job,
             it will increase the load by 2 if it's optimal type
             or by 10 if it's the non-optimal job.
             (might need to add a lock on this for when master accesses this
             to determine which slave to send a job to)
             */
    static int currentLoad = 0;
    static boolean isOpen = true;

    public SlaveA() {
        this.currentLoad = currentLoad;
        this.isOpen = isOpen;
    }

    public static int getCurrentLoad() {
        return currentLoad;
    }

    public static boolean getIsOpen() {return isOpen;}

    public static void main(String[] args) {
        args = new String[]{"127.0.0.1", "30122"};

        if (args.length != 2) {
            System.err.println("Usage: java client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                //sockets for connections between client and master (server)
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter requestWriter = //stream to write text requests to server
                        new PrintWriter(clientSocket.getOutputStream(), true);
                ObjectInputStream jobInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                ObjectOutputStream jobOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            // this is what it does better
            char optimalJob = 'a';


            Job currentJob;
            while(jobInputStream.readObject() != null) {
                currentJob = (Job) jobInputStream.readObject();
                if(currentJob.getType() == optimalJob) {
                    System.out.println("Job is optimal, takes 2 seconds to complete.");
                    // increase load by 2 and sleep for 2 seconds
                    currentLoad += 2;
                    Thread.sleep(2000);
                    // when finish job, decrease load by 2
                    currentLoad -= 2;
                } else {
                    System.out.println("Job is not optimal, takes 10 seconds to complete.");
                    // increase load by 10 and sleep for 10 seconds
                    currentLoad += 10;
                    Thread.sleep(10000);
                    // when finish job, decrease load by 10
                    currentLoad -= 10;
                }
                System.out.println("Completed job and sending to master, type: " + currentJob.getType() + " ID: " + currentJob.getID());
                jobOutputStream.writeObject(currentJob); // sending the done job to the master
            }



        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}