package com.example.gmail;

public class MailFactory {

	public static Mail getMail(String name,String mail) {
		MailDirector md=new MailDirector();
		if(name.compareToIgnoreCase("inbox")==0) {
			MailBuilder mb = new inboxConcrete();
			md.construct(mail, mb);
			return mb.getMails();
		}
		else if(name.compareToIgnoreCase("sent")==0) {
			MailBuilder mb = new sentConcrete();
			md.construct(mail, mb);
			return mb.getMails();
		}
		else if(name.compareToIgnoreCase("draft")==0) {
			MailBuilder mb = new draftConcrete();
			md.construct(mail, mb);
			return mb.getMails();
		}
		else if(name.compareToIgnoreCase("trash")==0) {
			MailBuilder mb = new trashConcrete();
			md.construct(mail, mb);
			return mb.getMails();
		}
		else if(name.compareToIgnoreCase("user")==0) {
			MailBuilder mb = new UserConcrete();
			md.construct(mail, mb);
			return mb.getMails();
		}
		return null;
		
	}
	
}
