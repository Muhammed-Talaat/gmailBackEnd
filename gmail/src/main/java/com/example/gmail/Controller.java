package com.example.gmail;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class Controller {
	
	//sent the whole mail object to be sent
	@PostMapping("/sentMail")
	public void sentMail(@RequestBody String jsonString) {
		GmailManager mn=GmailManager.getInstance();
		mn.checkAttachments(jsonString);
		Mail mail=mn.createMail(jsonString, "sent");
		List<String>list=mn.addAttachments();
	    mail.setAttachments(list);
	    mn.sendMail(mail);
	}
	

	//sent the whole mail object to be but in draft
	@PostMapping("/draftMail")
	public void draftMail(@RequestBody String jsonString) {
		GmailManager mn=GmailManager.getInstance();
		Mail mail=mn.createMail(jsonString,"draft");
		mn.draftMail(mail);
	}
	
	@GetMapping("/load/sent")
	public List<Mail>loadSentMail(){
		GmailManager mn= GmailManager.getInstance();
		mn.sent();
		return mn.pagination(1);
	}
	
	@GetMapping("/load/inbox")
	public List<Mail>loadInboxMail(){
		GmailManager mn= GmailManager.getInstance();
		mn.inbox();
		return mn.pagination(1);
	}
	
	@GetMapping("/load/draft")
	public List<Mail>loadDraftMail(){
		GmailManager mn= GmailManager.getInstance();
		mn.draft();
		return mn.pagination(1);
	}
	
	@GetMapping("/load/delete")
	public List<Mail> loadDeleteMail(){
		GmailManager mn=GmailManager.getInstance();
		mn.trash();
		return mn.pagination(1);
	}
	
	//send relative location of the selected mails
	//ie.. inbox, sent, draft, a folder inside user folder
	//Prevent user from making a folder with names like inbox, sent, trash, draft
	//send an array of dates of selected mail(s)
	//in the form of {date:['date1','date2','date3'..etc]}, each date in format of "yyyy-MM-dd-HH-mm-ss" //like ISO 8601
	//the final form be like {address:'inbox..',date:['date1',..,'date N']}
	//then a list of mails will be return as a response
	@PostMapping("/deleteMail")
	public List<Mail> deleteMail(@RequestBody String jsonString) {
		GmailManager mn=GmailManager.getInstance();
		JSONArray jsonArray=new JSONArray((new JSONObject(jsonString)).get("date").toString());
		String st[]=new String[jsonArray.length()];
		for(int i=0;i<jsonArray.length();i++) {
			st[i]=jsonArray.getString(i);}
		mn.getApp().addMail(mn.deleteMailbyDate(st));
		return mn.getApp().getMails();
	}
	
	

	//send an array of dates of selected mail(s)
	//in the form of {date:['date1','date2','date3'..etc]}, each date in format of "yyyy-MM-dd-HH-mm-ss" //like ISO 8601
	//the final form be like {address:'user folder name'..',date:['date1',..,'date N']}
	//then a boolean be return as a response
	@PostMapping("/moveMail")
	public boolean moveMail(@RequestBody String jsonString) {
		GmailManager mn=GmailManager.getInstance();
		JSONArray jsonArray=new JSONArray((new JSONObject(jsonString)).get("date").toString());
		String st[]=new String[jsonArray.length()];
		for(int i=0;i<jsonArray.length();i++) {
			st[i]=jsonArray.getString(i);}
		JSONObject jsonObject=new JSONObject(jsonString);
		String destination=jsonObject.getString("address");
		return mn.moveMailsToUserFolder(st,destination);
	}
	
	//used on sign up, send required metadata
	//then get a boolean identifier then if true calling inside the getAccount() method
	@GetMapping("/createAccount/{username}/{password}/{name}")
	public boolean createAccount(@PathVariable String username,
			@PathVariable String password,@PathVariable String name) {
	return GmailManager.getInstance().signUp(username,password,name);
	}

	//used on sign in, send required metadata
	//then get a boolean identifier then if true calling inside the getAccount()
	@GetMapping("/signIn/{username}/{password}")
	public boolean signIn(@PathVariable String username,
			@PathVariable String password) {
	return GmailManager.getInstance().signIn(username,password);
	}
	
	@GetMapping("/getAccount")
	public App getAccount(){
		return GmailManager.getInstance().getApp();
	}
	
	//Contacts Manipulation
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	//delete the whole contact, sent the name of the contact as a path variable, then get list of contacts 
	@GetMapping("/deleteContact/{contactName}")
	 public List<Contact> deleteContact(@PathVariable String contactName) {
		GmailManager mn=GmailManager.getInstance();
	    return mn.deleteContact(contactName);
    }
	  
	//modify existing contact 
	//sent the name of the contact as a path variable, and the new name
	//then get list of contacts 
	@GetMapping("/modifyContact/{contactName}/{newName}")
	 public List<Contact> modifyContact(@PathVariable String contactName,
			 @PathVariable String newName) {
		GmailManager mn=GmailManager.getInstance();
	    return mn.modifyContact(contactName,newName);
    }
	
	@GetMapping("/deleteDraft/{date}")
	 public void modifyContact(@PathVariable String date) {
		GmailManager mn=GmailManager.getInstance();
	    mn.deleteDraft(date);
   }
	
	//add contact, send the name, receive a list of contacts
	//if another contact with same name exists will be eliminated
	@GetMapping("/addContact/{contactName}")
	public List<Contact> addContact(@PathVariable String contactName) {
		GmailManager mn=GmailManager.getInstance();
	    return mn.createContact(contactName);
	}
	
	//delete a name from specific contact
	//send name of selected contact then name will be deleted from it
	@GetMapping("/deleteNameFromContact/{contactName}/{deletedName}")
    public Contact deleteNameFromContact(@PathVariable String contactName,
    		@PathVariable String deletedName) {
		GmailManager mn=GmailManager.getInstance();
	    return mn.deleteNameContact(contactName,deletedName);
	}
		  
	//modify existing name in existing contact 
	//sent the name of the contact as a path variable, and the old name, and the new
	//then get list of contacts 
	@GetMapping("/modifyNameFromContact/{contactName}/{oldName}/{newName}")
	 public Contact modifyNameFromContact(@PathVariable String contactName,
			 @PathVariable String oldName,@PathVariable String newName) {
		GmailManager mn=GmailManager.getInstance();
	    return mn.modifyNameContact(contactName,oldName,newName);
	}
		
	//add name in contact, send the name of contact, then new name to be added
	//Receive a list of contacts
	//if another name with same name exists will be eliminated
	@GetMapping("/addNameFromContact/{contactName}/{addedName}")
	public Contact addNameFromContact(@PathVariable String contactName,
			@PathVariable String addedName) {
			GmailManager mn=GmailManager.getInstance();
		return mn.addNameContact(contactName,addedName);
	}
    
	//sort contact then call the corresponding page, state is increasing or decreasing
	@GetMapping("/sortContact/{state}")
	public List<Contact> sortContact(@PathVariable String state) {
			GmailManager mn=GmailManager.getInstance();
		return mn.sortContact(state);
	}
	
	@GetMapping("/pagingContact/{numOfBage}")
	public List<Contact> pagingContact(@PathVariable int numOfBage) {
			GmailManager mn=GmailManager.getInstance();
		return mn.pagingContact(numOfBage);
	}
	
	//get a contact by its name
	@GetMapping("/getContactByName/{name}")
	public Contact getContactByName(@PathVariable String name) {
			GmailManager mn=GmailManager.getInstance();
		return mn.getContactByName(name);
	}
	
    //get standard 10 mails from a specific page
    //send the location at which user search like above and the number of page
    //paging method is required in another subject, when sorting the mails, call
    //paging with corresponding current page which the user selected
    @GetMapping("/paging/{numOfBage}")
	public List<Mail> paging(@PathVariable int numOfBage) {
			GmailManager mn=GmailManager.getInstance();
			return mn.pagination(numOfBage);
	}
    
    //sort mails, send the subject to sort based on 'key', and state 'increasing or decreasing'
    //send the location at which user search like above and the number of page
    @GetMapping("/sortMails/{key}/{state}")
	public List<Mail> sortMails(@PathVariable String key,
			@PathVariable String state) {
			GmailManager mn=GmailManager.getInstance();
			return mn.sortMails(key,state);}
    
    
    //filter mails, send the type ie.."importance", "subject", "sender" and "reciever"
    //all as strings, then call paging method with corresponding relativeLocation
    //and page number is ""1""
    @GetMapping("/filterMails/{type}/{contents}")
	public List<Mail> filterMails(@PathVariable String type,
			@PathVariable String contents) {
			GmailManager mn=GmailManager.getInstance();
			return mn.filterMails(type,contents);}
    
    //used when searching, when user want to select a mail to open in the normal way without searching,
    //send the type as date and the content of selected mail's name
    @GetMapping("/search/{type}/{content}")
    public List<Mail> search(@PathVariable String type,@PathVariable String content) {
    	GmailManager gm=GmailManager.getInstance();
    	List<Integer>indexs=gm.getIndexByType(type, content);
    	List<Mail>mail=new ArrayList<Mail>();
    	for(int index:indexs) 
    		mail.add(gm.getActionList().get(index));
    	return mail;
    }
    
    

    @GetMapping("/viewAttachment/{attachment}")
    public void attachmentUpload(@PathVariable String attachment) {
    	System.out.println(attachment);
    	GmailManager mn=GmailManager.getInstance();
    	System.out.println(mn.viewAttachment(attachment));
    	try {
    		Process process=Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+mn.viewAttachment(attachment));
    		process.waitFor();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    //files Manipulation
  	//////////////////////////////////////////////////////////////////////////////////////////////
    
    //called after clicking in "user" folders button 
    @GetMapping("/userButton")
   	public List<String> userButton() {
   			GmailManager mn=GmailManager.getInstance();
   			return mn.userButton();}
    
    //called in user when paging
    @GetMapping("/userPaging/{numOfBage}")
   	public List<String> userPaging(@PathVariable int numOfBage) {
   			GmailManager mn=GmailManager.getInstance();
   			return mn.userFolderPaging(numOfBage);}
    
    //change name of users folder, after calling this method call userPaging with current page
    @GetMapping("/changeName/{oldName}/{newName}")
   	public void changeName(@PathVariable String oldName,
   			@PathVariable String newName) {
   			GmailManager mn=GmailManager.getInstance();
   			 mn.changeName(oldName,newName);}
    
    //delete a user folder by its name, after calling this method call userPaging with current page
    @GetMapping("/deleteUserFolder/{Name}")
   	public void deleteUserFolder(@PathVariable String Name) {
    	GmailManager mn=GmailManager.getInstance();
    	mn.deleteUserFolder(Name);
    }
    
    //add a user folder with a name, after calling this method call userPaging with current page
    //check if the name exists, return false if another exists with same name 
    @GetMapping("/addUserFolder/{Name}")
   	public boolean addUserFolder(@PathVariable String Name) {
    	GmailManager mn=GmailManager.getInstance();
    	return mn.addUserFolder(Name);
    }
    
    //called when entering in a user folder to get mails from it, pass the name of folder
    @GetMapping("/enterUserFolder/{Name}")
    public List<Mail> enterUserFolder(@PathVariable String Name){
    	GmailManager mn=GmailManager.getInstance();
    	mn.setUserMails(Name);
    	return mn.pagination(1);
    }
    
    //delete a mail from user folder by date
    @PostMapping("/deleteMailuser")
	public void deleteMailuser(@RequestBody String jsonString) {
		GmailManager mn=GmailManager.getInstance();
		JSONArray jsonArray=new JSONArray((new JSONObject(jsonString)).get("date").toString());
		String st[]=new String[jsonArray.length()];
		for(int i=0;i<jsonArray.length();i++) {
			st[i]=jsonArray.getString(i);}
		mn.deleteMailUser(st);
	}
    
    
    @PostMapping("/attachments")
	public void attachments(@RequestParam("file") MultipartFile[] files) {
    	GmailManager mn=GmailManager.getInstance();
		mn.setAttachments(files);
		for(MultipartFile file:mn.getAttachments())
			try {
				file.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
    
	}



 