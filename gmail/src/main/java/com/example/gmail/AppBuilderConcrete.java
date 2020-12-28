package com.example.gmail;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppBuilderConcrete implements AppBuilder {
private App app=new App();


	@Override
	public void loadPassword(String username) {
		// TODO Auto-generated method stub
		List<App> list=Folder.getAppList("C:\\eclipseWorkspace\\System\\users.json");
		for(App st : list) {if(st.getUserName().compareToIgnoreCase(username)==0) {
			app.setName(st.getName());
			app.setPassword(st.getPassword());break;
		};}
	}
	@Override
	public void loadUsername(String username) {
		// TODO Auto-generated method stub
		app.setUserName(username);
	}

	@Override
	public void loadMails(String username) {
		// TODO Auto-generated method stub
		//get all emails
		String path="C:\\eclipseWorkspace\\System\\parent\\"+username+"\\";
		List<Mail> ls=new ArrayList<Mail>();
		//load all inbox
		ls.addAll(Folder.loadMailsFromFolder("inbox", path+"inbox"));
		//load all sent
		ls.addAll(Folder.loadMailsFromFolder("sent", path+"sent"));
		//load all trash
		ls.addAll(Folder.loadMailsFromFolder("trash", path+"trash"));
		//load all draft
		ls.addAll(Folder.loadMailsFromFolder("draft", path+"draft"));
		//load all user malis
		path+="user";
		String []nodes=(new File(path)).list();
		for(String st : nodes) {
			String aug=path+"\\"+st;
			ls.addAll(Folder.loadMailsFromFolder("user", aug));
		}
		app.addMail(ls);
	}
	@Override
	public App getApp() {
		// TODO Auto-generated method stub
		return this.app;
	}

	@Override
	public void loadContacts(String username) {
		// TODO Auto-generated method stub
		//load contacts
		String path="C:\\eclipseWorkspace\\System\\parent\\"+username+"\\contacts.json";
		List<Contact> ls=Folder.getContactList(path);
		app.addContact(ls);
	}
	
	public static boolean checkDate30(String date){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"); 
		String []now = dtf.format(LocalDateTime.now()).split("-");
		Calendar t2 = Calendar.getInstance();
		t2.set(Integer.parseInt(now[0]),Integer.parseInt(now[1]),Integer.parseInt(now[2]),Integer.parseInt(now[3]),
	    		  Integer.parseInt(now[4]),Integer.parseInt(now[5]));
		Calendar t1=Calendar.getInstance();
		String prev[]=date.split("-");
		t1.set(Integer.parseInt(prev[0]), Integer.parseInt(prev[1]), Integer.parseInt(prev[2]),
				Integer.parseInt(prev[3]), Integer.parseInt(prev[4]), Integer.parseInt(prev[5]));
		return (t2.getTimeInMillis()-t1.getTimeInMillis()<2592000000.0);
	}
}
