package com.noklin.atm.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.noklin.atm.ATM;
import com.noklin.atm.ATM.Memento;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.Bucket;
import com.noklin.atm.item.Item;

public class Deposit<T extends Item> extends Operation<T>{

	public Deposit(ATM<T> atm) {
		super(atm);
	}
	
	@Override
	public void doOperaion() throws ATMServiceException {
		ATM<T> atm = getAtm();
		Memento<T> state = atm.getState();
		Map<Integer,Bucket<T>> buckets = state.getBuckets();
		buckets.entrySet().stream().forEach(entry -> {  
			entry.getValue().stream()
			.filter(item -> item.getNominal() == entry.getKey())
			.collect(Collectors.toSet())
			.forEach(item ->{
				entry.getValue().remove(item);
				addItem(state, item);
			});
		}); 
		atm.setState(state);
	}
	
	private void addItem(Memento<T> state, T item){
		Map<Integer,List<T>> itemPool = state.getItemPool();
		List<T> items = itemPool.get(item.getNominal());
		if(items == null){
			items = new ArrayList<>();
			itemPool.put(item.getNominal(), items);
		}
		items.add(item);
	}
}