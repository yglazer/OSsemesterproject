
package yg.Master;

import yg.Job;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Master's WRITER threads to slaveA
 * takes the jobs from sendToSlaveA array and
 * actually writes it over on the slave socket
 */
public class ServerThreadSlaveAWriter implements Runnable{
    // a reference to the server socket is passed in, all threads share it
    private final ObjectOutputStream objectOutSA;
    private final ServerSharedMemory sharedMemory;
    private final Object jobsForSlaveA_Lock;

    public ServerThreadSlaveAWriter(ObjectOutputStream objectOutSA, ServerSharedMemory sharedMemory)  {
        this.objectOutSA = objectOutSA;
        this.sharedMemory = sharedMemory;
        this.jobsForSlaveA_Lock = sharedMemory.getJobsForSlaveA_LOCK();
    }

    @Override
    public void run() {
        try (
            objectOutSA;
        ) {
            while(true)
            {
                // to use as current status:
                ArrayList<Job> currJobsForSlaveA;

                synchronized (jobsForSlaveA_Lock)
                {
                    currJobsForSlaveA = new ArrayList<>(sharedMemory.getJobsForSlaveA());
                }

                for (Job currJob : currJobsForSlaveA)
                {
                    // remove each one from original array:
                    synchronized (jobsForSlaveA_Lock)
                    {
                        sharedMemory.getJobsForSlaveA().remove(currJob);
                    }
                    // write it to the slave A socket:
                    System.out.println("ServerTSlaveAWriter: Sending to slave A socket: Client: "
                            + currJob.getClient() + ", Type: " + currJob.getType() + ", ID: " + currJob.getID());
                    objectOutSA.writeObject(currJob);
                    objectOutSA.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
