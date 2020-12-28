package com.example.gmail;

import java.util.ArrayList;
import java.util.List;

public abstract class Mail {
protected String date;
protected String sender;
protected List<String> recievers=new ArrayList<String>();
protected String subject;
protected String body;
protected int importance;
protected List<String> attachments=new ArrayList<String>();

protected String directory;



public void setDate(String date) {
	// TODO Auto-generated method stub
	this.date=date;
}


public String getDate() {
	// TODO Auto-generated method stub
	return this.date;
}


public void setSender(String sender) {
	// TODO Auto-generated method stub
	this.sender=sender;
}


public String getSender() {
	// TODO Auto-generated method stub
	return this.sender;
}


public void setRecievers(List<String> recievers) {
	// TODO Auto-generated method stub
	this.recievers=recievers;
}


public List<String> getRecievers() {
	// TODO Auto-generated method stub
	return this.recievers;
}


public void setSubject(String subject) {
	// TODO Auto-generated method stub
	this.subject=subject;
}


public String getSubject() {
	// TODO Auto-generated method stub
	return this.subject;
}


public void setBody(String body) {
	// TODO Auto-generated method stub
	this.body=body;
}


public String getBody() {
	// TODO Auto-generated method stub
	return this.body;
}


public void setImportance(int importance) {
	// TODO Auto-generated method stub
	this.importance=importance;
}


public int getImportance() {
	// TODO Auto-generated method stub
	return this.importance;
}


public void setAttachments(List<String> attachments) {
	// TODO Auto-generated method stub
	this.attachments=attachments;
}


public List<String> getAttachments() {
	// TODO Auto-generated method stub
	return this.attachments;
}


public void setDirectory(String directory) {
	// TODO Auto-generated method stub
	this.directory=directory;
}


public String getDirectory() {
	// TODO Auto-generated method stub
	return this.directory;
}

}
