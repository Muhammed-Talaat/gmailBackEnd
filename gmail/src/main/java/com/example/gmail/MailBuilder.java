package com.example.gmail;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public abstract class MailBuilder {
protected Mail mail;

public void addSender(String sender) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(sender);
	mail.sender=jsonObject.getString("Sender");
}


public void addRecievers(String recievers) {
	// TODO Auto-generated method stub
	System.out.println(recievers);
	JSONArray jsonArray=new JSONArray(new JSONObject(recievers).get("Recievers").toString());
	for(Object obj:jsonArray) 
		mail.recievers.add((String)obj);
}


public void addSubject(String subject) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(subject);
	mail.subject=jsonObject.getString("Subject");
}


public void addBody(String body) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(body);
	mail.body=jsonObject.getString("Body");
}


public void addImportance(String importance) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(importance);
	mail.importance=jsonObject.getInt("Importance");
}


public void addDate(String date) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(date);
	mail.date=jsonObject.getString("Date");
}


/*public void addAttacments(String attacments) {
	// TODO Auto-generated method stub
	JSONObject jsonObject=new JSONObject(attacments);
	String jsonString=jsonObject.get("Attachments").toString();
	JSONArray jsonArray=new JSONArray(jsonString);
	for(int i=0;i<jsonArray.length();i++) {
		mail.attachments.add(jsonArray.getString(i));
	}
	System.out.println(mail.attachments);
}

public void addAttacments(String attacments) {
	// TODO Auto-generated method stub
	JSONArray jsonArray=new JSONArray(new JSONObject(attacments).get("Attachments"));
	for(Object obj:jsonArray) {
		MultipartFile file=(MultipartFile)obj;
		mail.attachments.add(file.getOriginalFilename());
	}
	System.out.println(mail.attachments);
}*/

public void addAttacments(String attacments) {
	// TODO Auto-generated method stub
	/*JSONArray jsonArray=new JSONArray(new JSONObject(attacments).get("Attachments"));
	for(Object obj:jsonArray) {
		MultipartFile file=(MultipartFile)obj;
		mail.attachments.add(file.getOriginalFilename());
	}
	System.out.println(mail.attachments);*/
}

public Mail getMails() {
	// TODO Auto-generated method stub
	return this.mail;
}

public abstract void setType();


}
