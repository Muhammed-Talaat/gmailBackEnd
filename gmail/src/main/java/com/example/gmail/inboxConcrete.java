package com.example.gmail;
public class inboxConcrete extends MailBuilder{

	public inboxConcrete() {this.setType();}
	
	@Override
	public void setType() {
		// TODO Auto-generated method stub
		super.mail=new inbox();
	}


}
