package com.noklin.atm;
 
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier; 

import com.noklin.atm.exception.ATMServiceException; 
import com.noklin.atm.item.Bucket;
import com.noklin.atm.item.Item;
import com.noklin.atm.operation.Deposit;
import com.noklin.atm.operation.Operation;
import com.noklin.atm.operation.Withdraw; 

public class ATM<T extends Item>{
	
	private ATM(){}
	private static final Comparator<? super Integer> DESC_ORDER = Comparator.reverseOrder();
	private Map<Integer,Bucket<T>> buckets = new TreeMap<>(DESC_ORDER);
	private Map<Integer,List<T>> itemPool = new TreeMap<>(DESC_ORDER);
	private List<Operation<T>> operationHistory = new ArrayList<>(); 
	
	public Set<Item> withdraw(int howMuch) throws ATMServiceException{ 
		Set<Item> result = new HashSet<>();
		Operation<T> operation = new Withdraw<>(this, howMuch); 
		operation.doOperaion();
		operationHistory.add(operation); 
		buckets.forEach((k,v)->{
			v.forEach(result::add);
		});
		return result;
	}

	public void deposit(T item) throws ATMServiceException{  
		Operation<T> operation = new Deposit<>(this, item); 
		operation.doOperaion();
		operationHistory.add(operation); 
	}

	/**
	 * Получить текущий баланс.
	 * */
	public int getBalance(){ 
		int summ = 0; 
		for(Map.Entry<Integer, List<T>> entry : itemPool.entrySet()){
			for(T item : entry.getValue()){
				summ += item.getNominal();
			}
		}
		return summ;
	} 
	
	/**
	 * Откатиться до первоначального состояния.
	 * @throws ATMServiceException - если не получилось
	 * */
	public void backToCreationState() throws ATMServiceException{
		int lastOperationIndex = operationHistory.size() - 1;
		while(lastOperationIndex --> 0){
			operationHistory.get(lastOperationIndex).undoOperaion();
		}
	} 
	
	private void addBucket(Bucket<T> bucket){
		buckets.put(bucket.getNominal(), bucket);
	}

	private void addItem(T item){
		List<T> items = itemPool.get(item.getNominal());
		if(items == null){
			items = new ArrayList<>();
			itemPool.put(item.getNominal(), items);
		}
		items.add(item);
	}

	public static <T extends Item> ATMBuilder<T> createBuilder(Supplier<T> supplier){
		return new ATMBuilder<T>(){ 
			ATM<T> atm = new ATM<>();
			Supplier<T> instanceSource = supplier;
			@Override
			public ATMBuilder<T> setItemSource(Supplier<T> instanceSource) {
				this.instanceSource = instanceSource;
				return this;
			}
			
			@Override
			public ATMBuilder<T> addItem(int nominal) {
				T item = instanceSource.get();
				item.setNominal(nominal);
				return addItem(item);
			}
			
			@Override
			public ATMBuilder<T> addBucket(int nominal) {
				return addBucket(new Bucket<>(nominal));
			}

			@Override
			public ATMBuilder<T> addItem(T item) {
				atm.addItem(item); 
				return this;
			}

			@Override
			public ATMBuilder<T> addBucket(Bucket<T> item) {
				atm.addBucket(item);
				return this;
			}

			@Override
			public ATM<T> build() { 
				return atm;
			} 
		};
	}
	
	public static interface ATMBuilder<T extends Item>{
		ATMBuilder<T> setItemSource(Supplier<T> instanceSource);
		ATMBuilder<T> addItem(int nomilal);
		ATMBuilder<T> addBucket(int nominal);
		ATMBuilder<T> addItem(T item);
		ATMBuilder<T> addBucket(Bucket<T> item);
		ATM<T> build();
	}
	
	/**
	 * Получить текущее сосотоние АТМ.
	 * */
	public Memento<T> getState(){
		return new Memento<>(buckets , itemPool);
	}
	
	/**
	 * Задать текущее сосотоние АТМ.
	 * */
	public void setState(Memento<T> state){
		buckets = state.getBuckets();
		itemPool = state.getItemPool();
	}
	
	public static class Memento<T extends Item>{
		private Map<Integer,Bucket<T>> buckets;
		private Map<Integer,List<T>> itemPool;
		
		Memento(Map<Integer,Bucket<T>> buckets, Map<Integer,List<T>> itemPool){
			this.buckets = new TreeMap<>(DESC_ORDER);
			buckets.forEach((k,v) -> {
				this.buckets.put(k, new Bucket<>(v));
			});
			this.itemPool = new TreeMap<>(DESC_ORDER);
			itemPool.forEach((k,v) -> {
				this.itemPool.put(k, new ArrayList<>(v));
			});
		}

		public Map<Integer, Bucket<T>> getBuckets() {
			return buckets;
		}
		
		public Map<Integer, List<T>> getItemPool() {
			return itemPool;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("ATM\n");
		sb.append("Available buckets:\n");
		buckets.forEach((k,v) -> {
			sb.append("\t").append(v).append("\n");
		});
		sb.append("Available items:\n");
		itemPool.forEach((k,v) -> {
			sb.append("\t").append(v).append("\n");
		});
		sb.append("Balance: ").append(getBalance()).append("\n");
		return sb.toString();
	}
}