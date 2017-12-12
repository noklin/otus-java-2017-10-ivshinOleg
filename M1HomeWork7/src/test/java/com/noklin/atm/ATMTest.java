package com.noklin.atm;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.noklin.atm.ATM.ATMBuilder;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.item.money.Money;
import com.noklin.atm.item.money.Rub;

public class ATMTest {
 
	@Test
	public void test0() throws ATMServiceException{ 
		ATMBuilder<Money> builder = ATM.<Money>createBuilder(Rub::new);
		ATM<Money> atm1 = builder.addBucket(1)
				.addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(1))
				.addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(1)).addItem(new Rub(1))
				.build();

		ATMBuilder<Money> builder2 = ATM.<Money>createBuilder(Rub::new);
		ATM<Money> atm2 = builder2.addBucket(10).addBucket(1).addBucket(500)
				.addItem(new Rub(10)).addItem(new Rub(10)).addItem(new Rub(10)).addItem(new Rub(1))
				.addItem(new Rub(10)).addItem(new Rub(10)).addItem(new Rub(10)).addItem(new Rub(1))
				.addItem(new Rub(500)).addItem(new Rub(500)).addItem(new Rub(500)).addItem(new Rub(1))
				.build();
		
		ATMDepartment<Money> depatrment = new ATMDepartment<>(Arrays.asList(atm1, atm2));
		long totalBalaneceBeforeActions = depatrment.getTotalBalance();
		
		atm1.withdraw(5);
		atm1.deposit(new Rub(1));
		atm1.deposit(new Rub(1));
		atm1.withdraw(5);

		atm2.withdraw(10);
		atm2.deposit(new Rub(500));
		atm2.deposit(new Rub(10));
		atm2.withdraw(1);
		depatrment.backToCreationState();
		long totalBalaneceAfterComeback= depatrment.getTotalBalance();
		assertTrue(totalBalaneceAfterComeback == totalBalaneceBeforeActions);
	}
	
	 
	
}