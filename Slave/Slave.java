package yg.Slave;

import yg.Job;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Slave {
    static ArrayList<Job> doneAJobs = new ArrayList<Job>();
    static ArrayList<Job> doneBJobs = new ArrayList<Job>();
    static Object doneAJobs_Lock = new Object();
    static Object doneBJobs_Lock = new Object();

    // getters and setters
    public static ArrayList<Job> getDoneAJobs() {
        return doneAJobs;
    }
    public static ArrayList<Job> getDoneBJobs() {
        return doneBJobs;
    }

    public static void main(String[] args)  {
      // args = new String[]{"127.0.0.1", "30122", "a"}; // this is for slave A
                  // for slave B: args are "127.0.0.1", "30123", "b"

        if (args.length != 3) {
            System.err.println("Usage: java client <host name> <port number> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumberS = Integer.parseInt(args[1]);
        char slaveType = args[2].charAt(0);


        try (
                Socket slaveSocket = new Socket(hostName, portNumberS);
                                ) {

            ArrayList<Thread> slaveThreads = new ArrayList<>();

            slaveThreads.add(new Thread(new SlaveServerListener(slaveSocket, slaveType, doneAJobs_Lock, doneBJobs_Lock)));
            slaveThreads.add(new Thread(new SlaveServerWriter(slaveSocket, doneAJobs_Lock, doneBJobs_Lock, slaveType)));

            for (Thread t: slaveThreads)
            {
                t.start();
            }

            for (Thread t: slaveThreads)
            {
                try
                {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}