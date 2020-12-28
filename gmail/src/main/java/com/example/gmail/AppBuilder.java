package com.example.gmail;

public interface AppBuilder {
	
	public void loadPassword(String username);
	
	public void loadUsername(String username);
	
	public void loadMails(String username);
	
	public void loadContacts(String username);
	
	public App getApp();
	
	
}
