package bank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;

public class Bank {
	private static Worker[] workers;
	private static Account[] accounts;
	private static ArrayBlockingQueue<Transaction> blockingQ;
	
	private static int numOfWorker;
	private static int accountSize = 20;
	private static int queueSize = 15;
	private static int initialBalance = 1000;
	private static int initialNumOfTrans = 0;
	
	private static Transaction nullTran = new Transaction(-1, 0, 0);
	
	private static CountDownLatch latch;
	
	
	private static class Worker extends Thread{
		@Override
		public void run() {
			try {
				Transaction currentTrans = blockingQ.take();
				while (!currentTrans.equals(nullTran)) {
					int id1 = currentTrans.getId1();
					int id2 = currentTrans.getId2();
					int amount = currentTrans.getAmount();
					if (id1 != -1) {
						Account account1 = accounts[id1];
						Account account2 = accounts[id2];
						account1.transferTo(account2, amount);
						}
					currentTrans = blockingQ.take();
					}
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				latch.countDown();
			}
		}
	
	public static void main(String[] args) {
		numOfWorker = Integer.parseInt(args[1]);
		workers = new Worker[numOfWorker];
		for (int i = 0; i < numOfWorker; i++) {
			workers[i] = new Worker();
		}
		
		accounts = new Account[accountSize];
		for (int i = 0; i < accountSize; i++) {
			accounts[i] = new Account(i, initialBalance, initialNumOfTrans);
		}
		
		blockingQ = new ArrayBlockingQueue<Transaction>(queueSize);
		latch = new CountDownLatch(numOfWorker);
		
		for (int i = 0; i < numOfWorker; i++) {
			workers[i].start();		
		}
		
		String fileName = args[0];
		readFile(fileName);
		
		try {
			latch.await();
			for (int i = 0; i < accountSize; i ++) {
				System.out.println(accounts[i]);
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} 
	}

	private static void readFile(String fileName) {
		// TODO Auto-generated method stub
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String read = br.readLine();
			
			while(read != null) {
				String[] info = read.split(" ");
				Transaction newTran = new Transaction(Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]));

				try {
					blockingQ.put(newTran);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				
				read = br.readLine();
			}
			
			for (int i = 0; i < numOfWorker; i++) {
				try {
					blockingQ.put(nullTran);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			fr.close();
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}
	
}
