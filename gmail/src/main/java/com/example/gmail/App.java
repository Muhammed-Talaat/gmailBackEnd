package com.example.gmail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App implements Logger{
private String username;
private String password;
private String name;
private List<Mail> mails=new ArrayList<Mail>();
private List<Contact> contacts=new ArrayList<Contact>();


public void addMail(Mail mails) {
	this.mails.add(mails);
}

public void addMail(List<Mail> list) {
	this.mails=list;
}

public List<Mail> getMails() {
	return this.mails;
}

public void addContact(Contact contact) {
	this.contacts.add(contact);
}

public void addContact(List<Contact> list) {
	this.contacts=list;
}

public List<Contact> getContacts(){
	return this.contacts;
}

public void setPassword(String password) {
	this.password=password;
}

public String getPassword() {
	return this.password;
}

public void setUserName(String username) {
	this.username=username;
}

public String getUserName() {
	return this.username;
}

public void setName(String name) {
	this.name=name;
}

public String getName() {
	return this.name;
}

@Override
public App signIn(String username, String password) {
	// TODO Auto-generated method stub
	AppDirector director=new AppDirector();
	AppBuilder builder = new AppBuilderConcrete();
	director.construct(username,builder);
	return builder.getApp();
}

@Override
public boolean signUp(String username, String password, String name) {
	// TODO Auto-generated method stub
	String path="C:\\eclipseWorkspace\\System\\";//"C:\\eclipseWorkspace\\System\\parent\\"
	//add new user
	App br=new App();
	br.setName(name);
	br.setPassword(password);
	br.setUserName(username);
	List<App> _br=Folder.getAppList(path+"users.json");
	_br.add(br);
	Folder.writeList(_br, path+"users.json");
	//add folder to the user
	path+="parent\\";
    Folder.createDirectory(path,username);
    path+=username;
    //create the Inbox Folder
    Folder.createDirectory(path,"\\inbox");
    Folder.createDirectory(path+"\\inbox","\\attachment");
    //create the Trash Folder
    Folder.createDirectory(path,"\\trash");
    Folder.createDirectory(path+"\\trash","\\attachment");
    //create the Sent Folder
    Folder.createDirectory(path,"\\sent");
    Folder.createDirectory(path+"\\sent","\\attachment");
    ////create the draft Folder
    Folder.createDirectory(path,"\\draft");
    Folder.createDirectory(path+"\\draft","\\attachment");
    //create the user folder
    Folder.createDirectory(path,"\\user");
    //create Contacts JSON
    try {
		(new File(path+"\\contacts.json")).createNewFile();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return true;
}



}
