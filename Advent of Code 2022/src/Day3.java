import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Day3 {
	private static boolean hasNext3Lines(Scanner lineCheck) {
		boolean retVal=true;
		try {
			lineCheck.nextLine();
			lineCheck.nextLine();
			lineCheck.nextLine();
		} catch(NoSuchElementException e) {
			retVal=false;
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		return retVal;
	}

	public static void main(String[] args) throws FileNotFoundException {
		File input=new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day3.txt");
		Scanner lineCheck=new Scanner(input);
		Scanner scan=new Scanner(input);
		ArrayList<String> matches=new ArrayList<>();
		int sum=0;
		
		while(hasNext3Lines(lineCheck)) {
			String firstLine=scan.nextLine();
			String secondLine=scan.nextLine();
			String thirdLine=scan.nextLine();

			int len1=firstLine.length();
			int len2=secondLine.length();
			int len3=thirdLine.length();
			
			char[] arr1=new char[len1];
			char[] arr2=new char[len2];
			char[] arr3=new char[len3];
			char[] targetChars=new char[52];
			
			boolean foundMatch=false;

			for(int i=0; i<len1; i++) arr1[i]=firstLine.charAt(i);
			for(int j=0; j<len2; j++) arr1[j]=firstLine.charAt(j);
			for(int k=0; k<len3; k++) arr1[k]=firstLine.charAt(k);
			for(int l=0; l<targetChars.length; l++) {
//				if(arr1.contains)
			}
		}
		/*
		for(String s : matches) {
			char curChar=s.charAt(0);
			if(Character.isUpperCase(curChar)) sum+=(int)curChar-38;
			else sum+=((int)curChar-96);
		}
		scan.close();
		System.out.println(sum);
		*/
	}
}


/*

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {
	public static void main(String[] args) throws FileNotFoundException {
		File input=new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day3.txt");
		Scanner scan=new Scanner(input);
		ArrayList<String> matches=new ArrayList<>();
		int sum=0;
		
		while(scan.hasNextLine()) {
			String curLine=scan.nextLine();
			int splitIdx=curLine.length() / 2;
			String r1=curLine.substring(0, splitIdx);
			String r2=curLine.substring(splitIdx, curLine.length());
			
			boolean foundMatch=false;
			
			for(int i=0; i<r1.length(); i++) {
				for(int j=0; j<r2.length(); j++) {
					if(r1.substring(i,i+1).equals(r2.substring(j,j+1))) {
						matches.add(r1.substring(i,i+1));
						foundMatch=true;
						break;
					}
				}
				if(foundMatch) break;
			}
		}
		
		for(String s : matches) {
			char curChar=s.charAt(0);
			if(Character.isUpperCase(curChar)) sum+=(int)curChar-38;
			else sum+=((int)curChar-96);
		}
		scan.close();
		System.out.println(sum);
	}
}

*/