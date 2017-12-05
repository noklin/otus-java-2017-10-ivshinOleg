package com.noklin.atm;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.noklin.atm.ATM.ATMBuilder;
import com.noklin.atm.exception.ATMServiceException;
import com.noklin.atm.exception.DenialOfServiceException;
import com.noklin.atm.item.money.Money;
import com.noklin.atm.item.money.Rub;

public class ATMTest {

	private ATM<Money> majorMoneyATM;
	
	@Before
	public void initMajorMoneyATM(){
		Map<Integer,Integer> itemNominalsAndCounts = new HashMap<>();
		itemNominalsAndCounts.put(1, 1000);
		itemNominalsAndCounts.put(10, 1000);
		itemNominalsAndCounts.put(50, 1000);
		itemNominalsAndCounts.put(100, 1000);
		itemNominalsAndCounts.put(500, 1000);
		itemNominalsAndCounts.put(1000, 1000);
		itemNominalsAndCounts.put(5000, 1000);
		majorMoneyATM = createATM(itemNominalsAndCounts, Arrays.asList(1, 10, 50, 100, 500, 1000, 5000));  
	}
	
	private String test0AssertionMsg = "АТМ должен вернуть правильный баланс после инициализации";
	@Test
	public void test0(){
		assertTrue(test0AssertionMsg, majorMoneyATM.getBalance() == 1*1000 + 10*1000 + 50*1000 + 100*1000 
				+ 500*1000 + 1000*1000 + 5000*1000);
	}
	
	private String test1AssertionMsg = "АТМ должен вернуть правильный баланс после пополнения";
	@Test
	public void test1() throws ATMServiceException{
		int balance = majorMoneyATM.getBalance();
		majorMoneyATM.deposit(new Rub(1));
		assertTrue(test1AssertionMsg, majorMoneyATM.getBalance() == balance + 1);
	}

	private String test2AssertionMsg = "АТМ должен вернуть правильный баланс после списания";
	@Test
	public void test2() throws ATMServiceException{  
		int balance = majorMoneyATM.getBalance();
		majorMoneyATM.withdraw(1);
		assertTrue(test2AssertionMsg, majorMoneyATM.getBalance() == balance - 1);
	}
	
 
	
	private String test5AssertionMsg = "АТМ должен уметь востанавливать состояние инициализации после действий над ним";
	@Test
	public void test5() throws ATMServiceException{  
		int balance = majorMoneyATM.getBalance();
		majorMoneyATM.deposit(new Rub(1));
		majorMoneyATM.withdraw(1000);
		majorMoneyATM.deposit(new Rub(5000));
		majorMoneyATM.backToCreationState();
		assertTrue(test5AssertionMsg, majorMoneyATM.getBalance() == balance);
	}
	
	@Test(expected = DenialOfServiceException.class)
	public void shouldThrowDenialOfServiceExceptionIfRequestMoreThanATMContains() throws ATMServiceException{
		majorMoneyATM.withdraw(Integer.MAX_VALUE); 
	}

	
	private ATM<Money> createATM(Map<Integer,Integer> itemNominalsAndCounts, Collection<Integer> bucketNominals){
		ATMBuilder<Money> builder = ATM.<Money>createBuilder(Rub::new);
		itemNominalsAndCounts.forEach((k,v) -> {
			while(v --> 0){
				builder.addItem(k);
			}
		});
		bucketNominals.forEach(builder::addBucket);
		return builder.build();
	}
	
}
