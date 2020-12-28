package com.example.gmail;

public class AppDirector {
private AppBuilder contactBuilder;

public void construct(String username,AppBuilder cnt) {
	this.contactBuilder=cnt;
	contactBuilder.loadUsername(username);
	contactBuilder.loadPassword(username);
	contactBuilder.loadMails(username);
	contactBuilder.loadContacts(username);
}
}
