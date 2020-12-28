package com.example.gmail;

public class UserConcrete extends MailBuilder{
	public UserConcrete() {this.setType();}
	
	@Override
	public void setType() {
		// TODO Auto-generated method stub
		super.mail=new User();
	}
}
