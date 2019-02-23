package bank;

public class Transaction {
	private int amount;
	private int id1;//from id1
	private int id2;//to id2
	
	public Transaction(int id1, int id2, int amount) {
		this.amount = amount;
		this.id1 = id1;
		this.id2 = id2;
	}
	
	public int getId1() {
		return this.id1;
	}
	
	public int getId2() {
		return this.id2;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	@Override
	public String toString() {
		String info = "From:" + Integer.toString(this.id1) + " To:" + Integer.toString(this.id2) + " Amount:" + Integer.toString(this.amount);
		return info;
	}
}
