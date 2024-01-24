package yg;

import java.io.Serializable;

public class Job implements Serializable {
	private int ID;
	private char type;
	private int client;
	
	
	//Constructor
	public Job(char type, int ID, int client) {
		this.type = type;
		this.ID = ID;
		this.client = client;
	}

	//getters and setters
	public char getType() {
		return type;
	}


	public void setType(char type) {
		this.type = type;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client)
	{
		this.client = client;
	}
	
}
