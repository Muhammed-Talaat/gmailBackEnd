package com.example.gmail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public class GmailManager {
private App app;
private String parPath="C:\\eclipseWorkspace\\System\\parent\\";
private List<Mail> sent=new ArrayList<Mail>();
private List<Mail> trash=new ArrayList<Mail>();
private List<Mail> inbox=new ArrayList<Mail>();
private List<Mail> draft=new ArrayList<Mail>();
private HashMap<String,List<Mail>> user=new HashMap<String,List<Mail>>();
private List<Mail> listInAction=new ArrayList<Mail>();
private List<String> userFolder=new ArrayList<String>();
private String mail;
private MultipartFile[]attachments;
private  GmailManager() {}
public String getParPath() {
	return parPath;
}
public void setParPath(String parPath) {
	this.parPath = parPath;
}
private static class holdingContainer {
    // Nested class is referenced after getInstance() is called
    private static final GmailManager uniqueInstance = new GmailManager();
}

public static GmailManager getInstance()
{
    return holdingContainer.uniqueInstance;
}

public void setApp(App app) {
	this.app=app;
}

public App getApp() {
	return this.app;
}

public Mail createMail(String mail,String type) {
	Mail ml=MailFactory.getMail(type, mail);
	ml.setDirectory(parPath+app.getUserName()+
			"\\"+type+"\\"+ml.getDate()+".json");
	if(getAttachments()!=null) {
		Folder.createDirectory(parPath+app.getUserName()+"\\"+type+"\\attachment\\", ml.getDate());
		attachments(app.getUserName(), type,ml.getDate());
	}
	//app.addMail(ml);
	return ml;
}

public List<String>addAttachments(){
	 List<String>list=new ArrayList<String>();
	    if(this.attachments!=null) {
	    	for(MultipartFile file:this.attachments)
	    		list.add(file.getOriginalFilename());
	    }
	    return list;
}



public void checkAttachments(String mail) {
	boolean check=new JSONObject(mail).getBoolean("check");
	if(!check)
		this.attachments=null;
}

public void attachments(String username,String type,String date) {
	for(MultipartFile file:attachments) {
		Path path=Path.of(parPath+username+"\\"+type+"\\"+"attachment"+"\\"+date+"\\"+file.getOriginalFilename());
		try {
			File fil=new File(path.toString());
			if(!fil.exists())
				Folder.moveAttachment(file.getInputStream(), path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


public void sendMail(Mail mail) {
	// TODO Auto-generated method stub
	//check if a receiver is existing or not
	List<App> list=Folder.getAppList("C:\\eclipseWorkspace\\System\\users.json");
	List<String> temp=new ArrayList<String>();
	list.forEach( t -> temp.add(t.getUserName().toLowerCase()) );
	List<String> _temp=new ArrayList<String>();
	mail.getRecievers().forEach( t-> _temp.add(t));
	_temp.removeIf( v -> (!temp.contains(v.toLowerCase())||v.compareToIgnoreCase(mail.getSender())==0));
	Queue<String> s = new LinkedList<String>();
	s.addAll(_temp);
	//s.forEach( st -> sendMail(mail,st) );
	//check if a receiver is existing or not
	while(!s.isEmpty()) {sendMail(mail,s.poll());}
	Folder.createMailFileDate(parPath
			+app.getUserName()+"\\sent", mail);
	this.sent.add(mail);
}



public void sendMail(Mail mail,String reciever) {
	List<String> _list=mail.getRecievers();
	List<String> list=new ArrayList<String>();
	String holder=mail.getDirectory();
	String temp[]=holder.split("\\\\");temp[temp.length-3]=reciever;
	temp[temp.length-2]="inbox";
	String _temp=String.join("\\",temp);
	mail.setDirectory(_temp);
	list.add(reciever);
	mail.setRecievers(list);
	String newPath=parPath+reciever+"\\inbox";
	if(getAttachments()!=null) {
	Folder.createDirectory(parPath+reciever+"\\inbox\\attachment\\", mail.getDate());
	attachments(reciever,"inbox",mail.getDate());}
	Folder.createMailFileDate(newPath, mail);
	mail.setRecievers(_list);
	mail.setDirectory(holder);
}



public boolean getIndex(List<String>list,String content) {
	int n=0;
	for(int i=0;i<list.size();i++) {
			if(list.get(i).compareToIgnoreCase(content)==0) {
				n++;break;
			}
	}
	if(n==0)return false;
	else return true;
}


public void draftMail(Mail mail) {
	// TODO Auto-generated method stub
	Folder.createMailFileDate(parPath
	+app.getUserName()+"\\draft", mail);
	this.draft.add(mail);
}

public String viewAttachment(String attachment) {
	List<Integer>list=getIndexByType("attachments", attachment);
	System.out.println(list);
	Mail mail=getActionList().get(list.get(0));
	String[]paths1=mail.getDirectory().split("\\\\");
	String[]paths2=new String[paths1.length-1];
	for(int i=0;i<paths1.length-1;i++)
		paths2[i]=paths1[i];
	String attachmentPath=String.join("\\",paths2);
	attachmentPath+="\\attachment\\"+mail.getDate()+"\\"+attachment;
	return attachmentPath;
}


public List<Mail> deleteMailbyDate(String... jsonString) {
	// TODO Auto-generated method stub
	List<Mail> ls=listInAction;
	for ( String st : jsonString ) {
	int j=getIndexByDate(st);
	Mail aug=ls.get(j);
	String newPath=getNewPath(aug.getDirectory(),"trash");
	Mail ml= convert(aug,"trash",newPath);
	//ls.remove(j);
	this.listInAction.remove(j);
	trash.add(ml);
	}
	return ls;
	//how we can change the path ?!!
}

public int getIndexByDate(String content) {
	 List<Mail>list=listInAction;
	 int j=-1;
	 for(int i=0;i<list.size();i++) {
		 if(list.get(i).date.compareToIgnoreCase(content)==0) {
			 j=i;break;
		 }
	 }
	 return j;
}

public List<Integer> getIndexByType(String type,String content) {
	List<Mail>list=listInAction;
	List<Integer>index=new ArrayList<Integer>();
	for(int i=0,v=list.size();i<v;i++) {
	    if(type.compareToIgnoreCase("date")==0) {
	    	if(list.get(i).date.compareToIgnoreCase(content)==0)
	    		index.add(i);
	    }
	    else if(type.compareToIgnoreCase("importance")==0) {
	    	if(list.get(i).importance==Integer.parseInt(content))
	    		index.add(i);
	    }
	    else if(type.compareToIgnoreCase("sender")==0) {
			if(list.get(i).sender.compareToIgnoreCase(content)==0)
				index.add(i);
		}
	    else if(type.compareToIgnoreCase("body")==0) {
			if(list.get(i).body.compareToIgnoreCase(content)==0)
				index.add(i);
		}
	    else if(type.compareToIgnoreCase("subject")==0) {
			if(list.get(i).subject.compareToIgnoreCase(content)==0)
				index.add(i);
		}
	    else if(type.compareToIgnoreCase("recievers")==0) {
	    	if(getIndex(list.get(i).recievers,content))
	    		index.add(i);
	    }
	    else if(type.compareToIgnoreCase("attachments")==0) {
	    	if(getIndex(list.get(i).attachments,content))
	    		index.add(i);
	    }
	}
	return index;
}


 

public boolean signUp(String name,String password,String username) {
	// TODO Auto-generated method stub
	LoggerProxy lg = new LoggerProxy();
	//extract the required fields
	if(lg.signUp(name, password, username)){
		this.app=lg.getApp();
		return true;
	};
	return false;
}

/*
 * public List<Mail> deleteMailbyDate(String... jsonString) {
	// TODO Auto-generated method stub
	String path1=aug.getDirectory();
	String[]paths1=path1.split("\\\\");
	String[]paths2=new String[paths1.length+2];
	for(int i=0;i<paths1.length-1;i++) paths2[i]=paths1[i];
	paths2[paths2.length-3]="attachment";
	paths2[paths2.length-2]=aug.getDate();
	for(String attachment:aug.getAttachments()) {
		paths2[paths2.length-1]=attachment;
		path1=String.join("\\",paths2);
		String type=paths2[paths2.length-4];
		paths2[paths2.length-4]="trash";
		String path2=String.join("\\",paths2);
		Folder.moveFileMail(path1,path2);
		paths2[paths2.length-4]=type;
	}

}
 * */

public static Mail convert(Mail mail,String Newtype,String Newpath) {
	Mail ml= (Newtype.compareToIgnoreCase("inbox")==0) ? new inbox():
		(Newtype.compareToIgnoreCase("sent")==0) ? new Sent():
			(Newtype.compareToIgnoreCase("tresh")==0) ? new trash():
				(Newtype.compareToIgnoreCase("draft")==0) ? new draft() : new User() ;
	//move the mail file
	String _[]=Newpath.split("\\\\");//parent//username//user//x//..json
	_=Arrays.copyOf(_, _.length-1);//parent//username//inbox//attachments//date//
	String aug=String.join("\\",_);
	String holder_=mail.getDirectory();
	mail.setDirectory(Newpath);
	Folder.createMailFileDate(aug, mail);
	(new File(holder_)).delete();
	// move the attachments
	String _aug[]=holder_.split("\\\\");
	_aug[_aug.length-1]="attachment\\"+mail.getDate();
	String aug_=String.join("\\",_aug);
	String holder=aug_;
	String tree[]=new File(aug_).list();aug_+="\\";
	if((tree!=null)&&(tree.length>0)) {
		String _newPath[]=getNewPathAttachments(aug_+tree[0],Newtype).split("\\\\");
		String _aug_=String.join("\\",Arrays.copyOf(_newPath, _newPath.length-1));
		new File(_aug_).mkdirs();
	for(String st : tree) {
		_aug[_aug.length-3]=Newtype;
		String newPath=getNewPathAttachments(aug_+st,Newtype);
		Folder.moveFileMail(aug_+st, newPath);
	}
	//delete the attachments in original location
	(new File(holder)).delete();
	ml.setAttachments(mail.getAttachments());}
	ml.setRecievers(mail.getRecievers());
	ml.setBody(mail.getBody());
	ml.setSender(mail.getSender());
	ml.setDate(mail.getDate());//"C:\\eclipseWorkspace\\System\\parent\\Ahmed\\inbox\\att\\date\\ahh.txt"
	ml.setDirectory(Newpath);
	ml.setImportance(mail.getImportance());
	ml.setSubject(mail.getSubject());
	
	return ml;
}

public MultipartFile[] getAttachments() {
	return attachments;
}
public void setAttachments(MultipartFile[] attachments) {
	this.attachments = attachments;
}

public static boolean familiar(String st) {
	return ((st.compareToIgnoreCase("sent")==0)||(st.compareToIgnoreCase("inbox")==0) ||
		   st.compareToIgnoreCase("draft") == 0 || st.compareToIgnoreCase("trash") == 0 );
}

public static String getNewPathAttachments(String oldPath,String Newtype){
	String path[]=oldPath.split("\\\\");
	//check the location of the files
	if(familiar(path[path.length-4].toLowerCase())){
		if(familiar(Newtype.toLowerCase())) {
		    path[path.length-4]=Newtype;}
		else {
			path[path.length-4]="user\\"+Newtype;
		}
	}
	else {
		if(!familiar(Newtype.toLowerCase())) {
		    path[path.length-4]=Newtype;}
		else {
			path[path.length-5]=Newtype;
			path[path.length-4]=path[path.length-3];
			path[path.length-3]=path[path.length-2];
			path[path.length-2]=path[path.length-1];
			path=Arrays.copyOf(path,path.length-1);
		}
	}
	return String.join("\\",path);
}

public static String getNewPath(String oldPath,String Newtype) {
	String path[]=oldPath.split("\\\\");
	//check the location of the files
	if(familiar(path[path.length-2].toLowerCase())){
		if(familiar(Newtype.toLowerCase())) {
		    path[path.length-2]=Newtype;}
		else {
			path[path.length-2]="user\\"+Newtype;
		}
	}
	else {//"C:\\eclipseWorkspace\\System\\parent\\Ahmed\\user\\aaa\\aa.json"
		//"C:\\eclipseWorkspace\\System\\parent\\Ahmed\\inbox\\aa.json"
		if(!"sentinboxdrafttrash".contains(Newtype.toLowerCase())) {
		    path[path.length-2]=Newtype;}
		else {
			path[path.length-2]=path[path.length-1];
			path[path.length-3]=Newtype;
			path=Arrays.copyOf(path,path.length-1);
		}
	}
	return String.join("\\",path);
}


public boolean signIn(String username, String password) {
	// TODO Auto-generated method stub
	LoggerProxy lg = new LoggerProxy();
	//extract the required fields
	this.app=lg.signIn(username, password);
	if(this.app!=null) {
		loadUserFiles(app.getUserName());
		loadMails(app.getMails());
		return true;
	}
	return false;
}

private void loadUserFiles(String userName) {
	// TODO Auto-generated method stub
	String path=parPath+userName+"\\user";
	//this.userFolder=Arrays.asList((new File(path)).list());
	String []aug=(new File(path).list());
	this.user=new HashMap<String,List<Mail>>();
	for( String st : aug ) {this.userFolder.add(st);}
	this.userFolder.forEach( t-> user.put(t, new ArrayList<Mail>()));
}


public void loadMails(List<Mail> mails) {
	this.sent=new ArrayList<Mail>();
	this.inbox=new ArrayList<Mail>();
	this.trash=new ArrayList<Mail>();
	this.draft=new ArrayList<Mail>();;
	for(Mail ml : mails) {
		if(ml instanceof Sent) {sent.add(ml);}
		else if(ml instanceof inbox) {inbox.add(ml);}
		else if(ml instanceof draft) {draft.add(ml);}
		else if(ml instanceof trash) {trash.add(ml);}
		else {
			String path[]=ml.getDirectory().split("\\\\");
			String folderName=path[path.length-2];
			user.get(folderName).add(ml);
		}
	}
}



public List<Contact> deleteContact(String contactName) {
	// TODO Auto-generated method stub
	this.app.addContact(Contact.deleteContact(this.app.getUserName(), contactName));
	return this.app.getContacts();
}

public List<Contact> modifyContact(String contactName, String NewName){
	this.app.addContact(Contact.modifyContact(this.app.getUserName(), contactName, NewName));
	return this.app.getContacts();
}

public List<Contact> createContact(String contactName) {
	// TODO Auto-generated method stub
	Contact cn=new Contact();
	cn.setOwner(this.app.getUserName());
	cn.setContactName(contactName);
	//this.app.addContact(cn);
	this.app.addContact(cn.addContactToFile());
	return this.app.getContacts();
}

public Contact deleteNameContact(String contactName,String deletedName) {
	// TODO Auto-generated method stub
	this.app.addContact(Contact.deleteName(this.app.getUserName(), contactName, deletedName));
	return getContactByName(contactName);
}

public Contact modifyNameContact(String contactName, String oldName, String newName) {
	// TODO Auto-generated method stub
	this.app.addContact(Contact.modifyName(this.app.getUserName(), contactName, oldName,newName));
	return getContactByName(contactName);
}

public Contact addNameContact(String contactName, String addedName) {
	// TODO Auto-generated method stub
	this.app.addContact(Contact.addName(this.app.getUserName(), contactName, addedName));
	return getContactByName(contactName);
}


public List<Mail> pagination(int numOfBage) {
	// TODO Auto-generated method stub
	List<Mail> list=new ArrayList<Mail>();
	if(listInAction!=null) {
	if((numOfBage-1)*10<listInAction.size()) {
	int _helper= (numOfBage*10)<=listInAction.size() ?
				(numOfBage*10) : listInAction.size();
	list=listInAction.subList((numOfBage-1)*10, _helper);}}
	return list;
}

public List<Mail>sent(){
	this.listInAction=Sort(sent);
	return this.listInAction;
}

public List<Mail>inbox(){
	this.listInAction=Sort(inbox);
	return this.listInAction;
}

public List<Mail>draft(){
	this.listInAction=Sort(draft);
	return this.listInAction;
}

public List<Mail>trash(){
	this.listInAction=Sort(trash);
	return this.listInAction;
}



public List<Mail>Sort(List<Mail>mails){
	return new Sorter().sortByType("date",mails,"decreasing");
}

public List<Mail> sortMails(String key,String state){
	this.listInAction=new Sorter().sortByType(key,listInAction , state);
	return this.listInAction;
}

public List<Mail> filterMails(String type,String contents){
	List<Mail> list=new ArrayList<Mail>();
	if(type.compareToIgnoreCase("subject")==0) {
		list=(new SubjectCriteria()).getByCriteria(listInAction, contents);
	}
	else if(type.compareToIgnoreCase("importance")==0) {
		list=(new ImportanceCriteria()).getByCriteria(listInAction, contents);
	}
	else if(type.compareToIgnoreCase("reciever")==0) {
		list=(new RecieverCriteria()).getByCriteria(listInAction, contents);
	}
	else if(type.compareToIgnoreCase("sender")==0) {
		list=(new SenderCriteria()).getByCriteria(listInAction, contents);
	}
	this.listInAction=list;
	return list;
}


public List<String> userButton() {
	// TODO Auto-generated method stub
	return userFolderPaging(1);
}

public List<String> userFolderPaging(int numOfBage){
	List<String> list=new ArrayList<String>();
	if((numOfBage-1)*10<userFolder.size()) {
	int _helper= (numOfBage*10)<=userFolder.size() ?
			(numOfBage*10) : userFolder.size();
    list=userFolder.subList((numOfBage-1)*10, _helper);}
	return list;
}

public void changeName(String oldName, String newName) {
	// TODO Auto-generated method stub
	String path=parPath+app.getUserName()+"\\user";
	//check if another has same new name!
	String nodes[]=(new File(path)).list();boolean flag=true;
	if( nodes!=null && nodes.length > 0 ) {
		for( String v : nodes ) {
			if(v.compareToIgnoreCase(newName)==0) {
				flag=false;break;
			}
		}
	}
	if(flag) {
	//change the path saved for all mails inside the Folder
	String aug=path+"\\"+oldName;
	List<Mail> temp=Folder.loadMailsFromFolder("user", aug);
	List<Mail> _temp=user.get(oldName);int i=0;
	for ( Mail m : temp ) {
		(new File(m.getDirectory())).delete();
		String aug_[]=aug.split("\\");
		aug_[aug_.length-1]=newName;
		Folder.createMailFileDate(String.join("\\",aug_), m);
		_temp.get(i++).setDirectory(String.join("\\",aug_)+"\\"+m.getDate());
	}
	Folder.renameDirectory(path, oldName, newName);
	Collections.replaceAll(userFolder, oldName, newName);
	List<Mail> temp_=user.get(oldName);
	user.remove(oldName);
	user.put(newName, temp_);}
}

public void deleteUserFolder(String name) {
	String path=parPath+app.getUserName()+"\\user"+"\\"+name;
	Folder.deleteDirectory(new File(path));
	userFolder.removeIf( t -> t.compareToIgnoreCase(name) == 0 );
}

public boolean addUserFolder(String name) {
	String path=parPath+app.getUserName()+"\\user";
	String []childrens = (new File(path)).list();
	if(childrens!=null && childrens.length>0) {
	for ( String st : childrens ) {if(st.compareToIgnoreCase(name)==0)
        return false;}}
	if(Folder.createDirectory(path+"\\", name)) {
		user.put(name, new ArrayList<Mail>());
		//System.out.println(userFolder.size());
		userFolder.add(name);
		//System.out.println();
		return Folder.createDirectory(path+"\\"+name+"\\", "attachment");
	}
	return false;
}


public void setUserMails(String name) {
	// TODO Auto-generated method stub
	this.listInAction=user.get(name);
}

public List<Mail> getActionList(){
	return this.listInAction;
}
public List<Contact> sortContact(String state) {
	// TODO Auto-generated method stub
	Sorter srt=new Sorter();
	this.app.addContact(srt.sortContact(state,this.app.getContacts()));
	return this.app.getContacts();
}

public List<Contact> pagingContact(int numOfBage) {
	// TODO Auto-generated method stub
	List<Contact> list=new ArrayList<Contact>();
	if((numOfBage-1)*10<app.getContacts().size()) {
	int _helper= (numOfBage*10)<=app.getContacts().size() ?
				(numOfBage*10) : app.getContacts().size();
	list=app.getContacts().subList((numOfBage-1)*10, _helper);}
	return list;
}


public Contact getContactByName(String name) {
	// TODO Auto-generated method stub
	Contact temp=new Contact();
	List<Contact> _temp=app.getContacts();
	for( Contact t : _temp ) {
		if(t.getContactName().compareToIgnoreCase(name)==0) {temp=t;break;}
	}
	return temp;
}
public void deleteDraft(String date) {
	// TODO Auto-generated method stub
	String path=this.parPath+app.getUserName()+"\\draft\\"+date+".json";
	new File(path).delete();
	
}

public boolean moveMailsToUserFolder(String[] st, String destination) {
	// TODO Auto-generated method stub
	//check if user folder exists
	String path=parPath+app.getUserName()+"\\user";
	String []childrens = (new File(path)).list();boolean flag=false;
	if( childrens!=null && childrens.length>0 ) {
	for ( String str : childrens )
	      {
		if(str.compareToIgnoreCase(destination) == 0 ) {
        flag=true;break;}}}
	if(flag) {
	List<Mail> ls=listInAction;
	for ( String str : st ) {
	int j=getIndexByDate(str);
	Mail aug=ls.get(j);
	String newPath=getNewPath(aug.getDirectory(),destination);
	Mail ml= convert(aug,destination,newPath);
	//ls.remove(j);
	this.listInAction.remove(j);
	user.get(destination).add(ml);
	}
	return true;
	}
	//how we can change the path ?!!
	return false;
}


public void deleteMailUser(String... date) {
	// TODO Auto-generated method stub
	for(String date_ : date) {
	int temp=getIndexByDate(date_);
	String dates=this.listInAction.get(temp).getDate();
	String path=this.listInAction.get(temp).getDirectory();
	this.listInAction.remove(temp);
	(new File(path)).delete();
	//delete attachments
	String aug[]=path.split("\\\\");
	aug[aug.length-1]="\\attachment\\"+dates;
	path=String.join("\\", aug);
	Folder.deleteDirectory(new File(path));}
}


}