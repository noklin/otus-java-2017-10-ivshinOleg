package com.noklin.atm.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.noklin.atm.ATM;
import com.noklin.atm.ATM.Memento; 
import com.noklin.atm.exception.DenialOfServiceException;
import com.noklin.atm.item.Bucket;
import com.noklin.atm.item.Item;

public class Withdraw<T extends Item> extends Operation<T>{

	private final int toWithdraw;
	public Withdraw(ATM<T> atm, int toWithdraw) {
		super(atm);
		this.toWithdraw = toWithdraw;
	}

	@Override
	public void doOperaion() throws DenialOfServiceException{
		ATM<T> atm = getAtm();
		Memento<T> state = atm.getState();
		Map<Integer,Bucket<T>> buckets = state.getBuckets();
		Map<Integer,List<T>> itemPool = state.getItemPool();
		int howMuch = toWithdraw;
		if(atm.getBalance() < howMuch){
			throw new DenialOfServiceException("Does not contains so much. Actual balance: " + atm.getBalance());
		}
		List<T> toWithdraw = new ArrayList<>();
		for(Map.Entry<Integer, List<T>> e : itemPool.entrySet()){
			for(T item : e.getValue()){					
				if(howMuch == item.getNominal() || howMuch > item.getNominal()){
					if(state.getBuckets().get(e.getKey()) != null){
						howMuch -= e.getKey();
						toWithdraw.add(e.getValue().get(0));
					}
				}else{
					break;
				}
			}
		}
		if(howMuch != 0){ 
			throw new DenialOfServiceException("Can't withdraw " + howMuch);
		}
		toWithdraw.forEach(item -> {
			buckets.get(item.getNominal()).add(item);
			itemPool.get(item.getNominal()).remove(item);
		}); 
		atm.setState(state);
	}

}