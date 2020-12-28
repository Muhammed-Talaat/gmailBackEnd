package com.example.gmail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorter {
	private String compareState;
	private boolean stateIsDecreasing;
	private Comp comp;
	private CompContact compcontact;
	
	public Sorter() {
		this.comp=new Comp();
		this.compareState="date";
		this.compcontact=new CompContact();
		this.stateIsDecreasing=true;
	}
	
	class Comp implements Comparator<Mail>{
		@Override
		public int compare(Mail o1, Mail o2) {
			// TODO Auto-generated method stub
			//Data is in format "yyyy-MM-dd-HH-mm-ss" //ISO 8601
			if(compareState.compareToIgnoreCase("date")==0) {
			return (!stateIsDecreasing) ? o1.getDate().compareToIgnoreCase(o2.getDate()):
				o2.getDate().compareToIgnoreCase(o1.getDate());}
			
			else if(compareState.compareToIgnoreCase("sender")==0) {
				return (stateIsDecreasing) ? o1.getSender().compareToIgnoreCase(o2.getSender()):
					o2.getSender().compareToIgnoreCase(o1.getSender());}
			
			else if(compareState.compareToIgnoreCase("subject")==0) {
				return (stateIsDecreasing) ? o1.getSubject().compareToIgnoreCase(o2.getSubject()):
					o2.getSubject().compareToIgnoreCase(o1.getSubject());}
			
			else if(compareState.compareToIgnoreCase("body")==0) {
				return (stateIsDecreasing) ? o1.getBody().compareToIgnoreCase(o2.getBody()):
					o2.getBody().compareToIgnoreCase(o1.getBody());}
			
			else if(compareState.compareToIgnoreCase("importance")==0) {
				return (!stateIsDecreasing) ? o1.getImportance()-o2.getImportance():
					o2.getImportance()-o1.getImportance();}
			
			else if(compareState.compareToIgnoreCase("recievers")==0) {
				return (stateIsDecreasing) ? o1.getRecievers().get(0).compareToIgnoreCase(o2.getRecievers().get(0)):
					o2.getRecievers().get(0).compareToIgnoreCase(o1.getRecievers().get(0));}
			
			else {
				return (o1.getAttachments().size()>0&&o2.getAttachments().size()>0) ?
						(stateIsDecreasing) ? o1.getAttachments().get(0).compareToIgnoreCase(o2.getAttachments().get(0)):
							o2.getAttachments().get(0).compareToIgnoreCase(o1.getAttachments().get(0)):
								(o1.getAttachments().size()==0&&o2.getAttachments().size()==0) ? 0:
									(o2.getAttachments().size()==0) ? (stateIsDecreasing) ? 
											-1:1:(stateIsDecreasing) ? 1 : -1;
			}
		}
	}
	
	class CompContact implements Comparator<Contact>{
		@Override
		public int compare(Contact o1, Contact o2) {
			return(stateIsDecreasing)?o1.getContactName().compareToIgnoreCase(o2.getContactName()):
				o2.getContactName().compareToIgnoreCase(o1.getContactName());
		}}

	public List<Mail> sortByType(String key,List<Mail> list,String state){
		compareState=key;
		stateIsDecreasing=!(state.compareToIgnoreCase("increasing")==0);
		Collections.sort(list, comp);
		return list;
	}
	
    public List<Contact> sortContact(String key,List<Contact> list){
    	compareState="contact";
    	stateIsDecreasing=!(key.compareToIgnoreCase("increasing")==0);
		Collections.sort(list, compcontact);
		return list;
    }
	
}
