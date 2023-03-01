interface BankAccountInterface {
  double getBalance();
  String getName();
  void deposit(double amount) throws Exception;
  void withdraw(double amount) throws Exception;
  void transfer(double amount, BankAccount destination) throws Exception;
}

abstract class BankAccount {
  private String name;
  private double balance;
  
  public BankAccount(String name, double initialDeposit) throws Exception {
    if (name.length() <= 2) throw new Exception();
    this.name = name;
    if (balance <= 0) throw new Exception();
    balance = initialDeposit;
  }
  public void deposit(double amount) throws Exception {
    if (amount <= 0) throw new Exception();
    balance += amount;
  }
  public void withdraw(double amount) throws Exception {
    if (amount <= 0) throw new Exception();
    if (amount > balance) throw new Exception();
    balance -= amount;
  }
  public void transfer(double amount, BankAccount destination) throws Exception {
    withdraw(amount);
    destination.balance += amount;
  }
}

class CheckingAccount extends BankAccount {
  private String name;
  private double balance;
  
  public CheckingAccount(String name, double initialDeposit) throws Exception {
    super(name, initialDeposit);
  }
}

class SavingsAccount extends BankAccount {
  private String name;
  private double balance;
  
  private double interestRate;
  private int withdrawCount;
  public SavingsAccount(String name, double initialDeposit, double interestRate) 
  throws Exception {
    super(name, initialDeposit);
    if (interestRate < 0) throw new Exception();
    this.interestRate = interestRate;
    withdrawCount = 0;
  }
  public void addInterest() {
    balance *= 1 + interestRate;
  }
  public int getWithdrawCount() {
    return withdrawCount;
  }
  public void withdraw(double amount) throws Exception {
    if (withdrawCount == 6) throw new Exception();
    super(amount);
    withdrawCoun++;
  }
}

class SavingsAccountKid extends SavingsAccount [
  private String parentName;
  public SavingsAccountKid(String name, double initialDeposit, double interestRate, String parentName)
  throws Exception {
    super(name, initialDeposit, interestRate);
    this.parentName = parentName;
  }
  
  public int withdraw(double amount, String parentName) throws Exception {
    if (!parentName.equals(this.parentName)) throw new Exception();
    super(amount);
  }
}
