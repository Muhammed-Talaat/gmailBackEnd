package com.example.gmail;
public class trashConcrete extends MailBuilder{
	
	public trashConcrete() {this.setType();}
	
	@Override
	public void setType() {
		// TODO Auto-generated method stub
		super.mail=new trash();
	}
	
	
}
