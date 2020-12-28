package com.example.gmail;

import java.util.List;
import java.util.stream.Collectors;

public class SenderCriteria implements CriteriaMail {

	@Override
	public List<Mail> getByCriteria(List<Mail> list, String contents) {
		// TODO Auto-generated method stub
		return  list.stream().filter(num ->
		(num.getSender().compareToIgnoreCase(contents)==0)).collect(Collectors.toList());
	}
}
