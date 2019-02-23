package assign5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	private static String inputHash;
	private static int maxLen;
	private static int numOfWorker;
	private static List<String> passwords = Collections.synchronizedList(new ArrayList<String>());
	private static Worker[] workers;
	private static CountDownLatch latch;
	
	public static void main(String[] args) {
		if (args.length == 1) {
			String hashValue = generateHash(args[0]);
			System.out.println(hashValue);
		} else if (args.length == 3) {
			inputHash = args[0];
			maxLen = Integer.parseInt(args[1]);
			numOfWorker = Integer.parseInt(args[2]);
			
			int step = CHARS.length / numOfWorker;
			workers = new Worker[numOfWorker];
			for (int i = 0; i < numOfWorker; i++) {
				workers[i] = new Worker(i * step, (i + 1) * step);
			}
			
			latch = new CountDownLatch(numOfWorker);
			
			for (int i = 0; i < numOfWorker; i++) {
				workers[i].start();
			}
			try {
				latch.await();
				for (String pwd : passwords) {
					System.out.println(pwd);
				}
				System.out.println("all done");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("The format of input is not correct. You could enter "
					+ "only the password or the hash value, the maximum "
					+ "length of password and the number of threads.");
		}
	}
	
	public static class Worker extends Thread{
		private int start;
		private int end;
		
		public Worker(int start, int end){
			this.start = start;
			this.end = end;
		}
		
		@Override
		public void run(){
			for (int i = start; i < end; i++) {
				findHash(""+CHARS[i]);
			}
			latch.countDown();
		}

		//Brute force
		private void findHash(String string) {
			// TODO Auto-generated method stub
			if (string.length() > maxLen) {
				return;
			}
			if (generateHash(string).equals(inputHash)) {
				passwords.add(string);
				return;
			}
			for (int i = 0; i < CHARS.length; i++) {
				findHash(string+CHARS[i]);
			}
		}
	}
	
	private static String generateHash(String string) {
		// TODO Auto-generated method stub
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(string.getBytes());
			return hexToString(md.digest());
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	// possible test values:
	// a ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb
	// fm 440f3041c89adee0f2ad780704bcc0efae1bdb30f8d77dc455a2f6c823b87ca0
	// a! 242ed53862c43c5be5f2c5213586d50724138dea7ae1d8760752c91f315dcd31
	// xyz 3608bca1e44ea6c4d268eb6db02260269892c0b42b86bbf1e77a6fa16c3c9282

}
