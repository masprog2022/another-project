package tests.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import entities.Account;
import tests.factory.AccountFactory;

public class AccountTests {
	
	@Test
	public void depositShouldIncreaseAndDescountFeeBalanceWhenPositiveAmount() { // o deposito deve incrementar e descontar a taxa quando o montante e positivo
		double amount = 200.0;
		double exptectedValue = 196.0;
		Account acc = AccountFactory.createEmptyAccount();
		
		acc.deposit(amount);
		
		Assertions.assertEquals(exptectedValue, acc.getBalance());
	}
	
	@Test
	public void depositShouldDoNothingWhenNegativeAmount() { // Nao deve fazer nada quando o montante e negativo
		
		double exptectedValue = 100.0;
		Account acc = AccountFactory.createAccount(exptectedValue);
		double amount = -200.0;
		
		acc.deposit(amount);
		Assertions.assertEquals(exptectedValue, acc.getBalance());
	}
	
	@Test
	public void fullWithDrawShouldClearBalanceAndReturnFullBalance() { // Deveria limpar a conta
		
		double exptectedValue = 0.0;
		double initialBalance = 800.0;
		Account acc = AccountFactory.createAccount(initialBalance);
		
		double result = acc.fullWithDraw();
		
		Assertions.assertTrue(exptectedValue == acc.getBalance());
		Assertions.assertTrue(result == initialBalance);	
		
	}
	
	@Test
	public void fullWithDrawShouldDecreaseBalanceWhenSufficientBalance() { // deveria sacar dinheiro quando tem saldo suficiente na conta
		
		Account acc = AccountFactory.createAccount(800.0);
		acc.withdraw(500.0);
		
		Assertions.assertEquals(300.0, acc.getBalance());
		
	}
	
	@Test
	public void withDrawShouldThrowExceptionWhenInsufficientBalance() { // Deveria lancar uma excessao quando o saldo e insuficiente
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Account acc = AccountFactory.createAccount(800.0);
			acc.withdraw(801.0);
		});
		
		
	}

}
