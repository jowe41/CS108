package bank;

public class Account {
	private int id;
	private int currentBalance;
	private int numOfTrans;
	private static Object lock = new Object();
	
	
	public Account(int id, int currentBalance, int numOfTrans) {
		this.id = id;
		this.currentBalance = currentBalance;
		this.numOfTrans = numOfTrans;
	}
	
	@Override
	public String toString() {
		String info = "";
		info += "acct:" + Integer.toString(id) + " bal:" 
				+ Integer.toString(currentBalance) 
				+ " trans:" + Integer.toString(numOfTrans);
		return info;
	}
	
	public synchronized void transferTo(Account other, int amount) {
		synchronized(lock) {
			other.currentBalance += amount;
			this.currentBalance -= amount;
			other.numOfTrans++;
			this.numOfTrans++;
		}
	}
}
