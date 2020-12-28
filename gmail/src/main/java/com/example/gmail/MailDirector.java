package com.example.gmail;

public class MailDirector {
private MailBuilder mailbuilder;

public void construct(String mail,MailBuilder mailbuilders) {
	this.mailbuilder=mailbuilders;
	//CAUTION don't change the order of the methods
	//mailbuilder.addAttacments(mail);
	mailbuilder.addBody(mail);
	mailbuilder.addDate(mail);
	mailbuilder.addImportance(mail);
	mailbuilder.addRecievers(mail);
	mailbuilder.addSubject(mail);
	mailbuilder.addSender(mail);
}
}
