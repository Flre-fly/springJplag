import java.util.Scanner;

import static java.util.Collections.reverse;


public class Main {
	public static String sum(String x, String y)
	{
		int num;
		int carry = 0;
		boolean flag = false;
		String result ="";
		//2090 + 110 이라고 하면 일단
		StringBuffer sb1 = new StringBuffer(x);
		StringBuffer sb2 = new StringBuffer(y);
		x = sb1.reverse().toString();
		y = sb2.reverse().toString();

		while (x.length() < y.length()) {
			x += '0';
		}
		while (x.length() > y.length()) {
			y += '0';
		}
		for(int i=0;i<x.length();i++){
			//int IAtX = Character.getNumericValue(x.charAt(i));
			//int IAtY = Character.getNumericValue(y.charAt(i));
			int IAtX = x.charAt(i) - '0';
			int IAtY = y.charAt(i) - '0';
			if(flag) {
				IAtX++;//한자리수 올림
				flag = false;
			}
			if(IAtX + IAtY  >= 10){

				flag = true;
				//그리고 다음 result에다가 1을 올려줘야하는데
			}
			result += (IAtX + IAtY) % 10;



		}
		if(flag) {
			result += "1";
		}
		StringBuffer resultsb = new StringBuffer(result);
		result = resultsb.reverse().toString();
		return result;
	}
	public static String fibo(int n){
		String pprev = "0";
		String prev = "1";
		String temp;
		if(n==0) return "0";
		else if (n==1) return "1";
		else {
			for(int i=0;i<n-1;i++){
				temp = prev;
				prev = sum(pprev, prev);
				pprev = temp;
			}
			return prev;
		}

	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		System.out.println(fibo(n));

	}


}
