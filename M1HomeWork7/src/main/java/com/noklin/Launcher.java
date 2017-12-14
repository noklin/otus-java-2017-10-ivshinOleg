package com.noklin;

import java.util.Arrays;

import com.noklin.atm.ATM;
import com.noklin.atm.ATMDepartment;
import com.noklin.atm.ATM.ATMBuilder;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.money.Money;
import com.noklin.atm.item.money.Rub;

public class Launcher {
	public static void main(String[] args) throws ATMServiceException {
		ATMBuilder<Money> builder = ATM.<Money>createBuilder(Rub::new);
		ATM<Money> atm = builder.addBucket(1).addBucket(10).addBucket(50).addBucket(100).addBucket(500).addBucket(1000)
				.addBucket(5000).addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(10)).addItem(new Rub(50))
				.build();

		ATMBuilder<Money> builder2 = ATM.<Money>createBuilder(Rub::new);
		ATM<Money> atm2 = builder2.addBucket(1).addBucket(10).addBucket(50).addBucket(100).addBucket(500)
				.addBucket(1000).addBucket(5000).addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(10))
				.addItem(new Rub(50)).build();
		
		atm2.deposit(new Rub(500));
		atm2.deposit(new Rub(500));
		atm2.deposit(new Rub(500));
		atm2.deposit(new Rub(500));
		atm2.deposit(new Rub(500));
		
		ATMDepartment depatrment = new ATMDepartment(Arrays.asList(atm, atm2));
		System.out.println(depatrment);
		System.out.println("total balance: " + depatrment.getTotalBalance());
		depatrment.backToCreationState();
		System.out.println(depatrment);
		System.out.println("total balance: " + depatrment.getTotalBalance());
	}
}