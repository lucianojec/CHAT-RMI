package edu.uniasselvi.bean;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String name;
	private String ip;
	private boolean connected = true;

	public User() {
	}

	// Construtor
	public User(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConnected() {
		return connected;
	}

	public void disconnect() {
		this.connected = false;
	}

	public void connect() {
		this.connected = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}