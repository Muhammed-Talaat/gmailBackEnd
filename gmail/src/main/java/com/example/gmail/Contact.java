package com.example.gmail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Contact {
private String owner;
private String contactName;
private List<String> names=new ArrayList<String>();

public static List<Contact> addName(String Ownername,String contactName, String addedName) {
	String path="C:\\eclipseWorkspace\\System\\parent\\"+Ownername+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	Contact temp=new Contact();
	for(Contact ct : list) {
		if(ct.contactName.compareToIgnoreCase(contactName)==0) {
			temp=ct;
			break;
		}
	}
	temp.names.removeIf( nat -> nat.compareToIgnoreCase(addedName)==0);
	temp.names.add(addedName);
	return temp.addContactToFile();
}

public void addName(List<String> list) {
	this.names=list;
}

public void setContactName(String contactName) {
	this.contactName=contactName;
}

public void setOwner(String owner) {
	this.owner=owner;
}

public String getOwner() {
	return this.owner;
}

public String getContactName() {
	return this.contactName;
}

public List<String> getNames(){
	return this.names;
}

public static List<Contact> deleteName(String Ownername,String contactName,String deletedName) {
	String path="C:\\eclipseWorkspace\\System\\parent\\"+Ownername+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	Contact temp=new Contact();
	for(Contact ct : list) {
		if(ct.contactName.compareToIgnoreCase(contactName)==0) {
			temp=ct;
			temp.names.removeIf( nat -> nat.compareToIgnoreCase(deletedName)==0);
			return temp.addContactToFile();
		}
	}
	return list;}

public static List<Contact> modifyName(String owner,String contactName,String old,String New) {
	String path="C:\\eclipseWorkspace\\System\\parent\\"+owner+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	Contact temp=new Contact();
	for(Contact ct : list) {
		if(ct.contactName.compareToIgnoreCase(contactName)==0) {
			temp=ct;
			break;
		}
	}
	Collections.replaceAll(temp.names,old,New.toLowerCase());
	return temp.addContactToFile();}

public static List<Contact> modifyContact(String owner, String contactName,String NewName){
	String path="C:\\eclipseWorkspace\\System\\parent\\"+owner+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	for(Contact ct : list) {
		if(ct.contactName.compareToIgnoreCase(contactName)==0) {
			ct.contactName=NewName;
			break;
		}
	}
	Folder.writeList(list, path);
	return list;
}

public static List<Contact> deleteContact(String owner, String contactName) {
	String path="C:\\eclipseWorkspace\\System\\parent\\"+owner+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	list.removeIf(nat -> nat.contactName.compareToIgnoreCase(contactName)==0);
	Folder.writeList(list, path);
	return list;
}

public List<Contact> addContactToFile(){
	String path="C:\\eclipseWorkspace\\System\\parent\\"+this.owner+"\\contacts.json";
	List<Contact> list=Folder.getContactList(path);
	list.removeIf(nat -> nat.contactName.compareToIgnoreCase(this.contactName)==0);
	list.add(this);
	Folder.writeList(list, path);
	return list;
}

public static List<Contact> getContact(List<Contact> list, String contactName){
	return (new ArrayList<Contact>()).stream().filter(num -> 
	(num.getContactName().compareToIgnoreCase(contactName)==0)).collect(Collectors.toList());
}

}
