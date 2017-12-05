package com.noklin.atm.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.noklin.atm.ATM;
import com.noklin.atm.ATM.Memento;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.Bucket;
import com.noklin.atm.item.Item;

public class Deposit<T extends Item> extends Operation<T>{

	private T item;
	public Deposit(ATM<T> atm, T item) {
		super(atm);
		this.item = item;
	}
	
	@Override
	public void doOperaion() throws ATMServiceException {
		ATM<T> atm = getAtm();
		Memento<T> state = atm.getState();
		Map<Integer,Bucket<T>> buckets = state.getBuckets(); 
		Bucket<T> bucket = buckets.get(item.getNominal());
		if(bucket == null) {
			throw new ATMServiceException("Item not supported");
		}
		addItem(state, item);
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