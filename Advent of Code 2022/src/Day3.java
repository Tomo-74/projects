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
			
			ArrayList<String> aL1=new ArrayList<>();
			ArrayList<String> aL2=new ArrayList<>();
			ArrayList<String> aL3=new ArrayList<>();
			char[] targetChars=new char[52];
			
			String commonChar;
			
			for(int i=0; i<len1; i++) { aL1.add(firstLine.substring(i,i+1)); }
			for(int j=0; j<len2; j++) { aL2.add(secondLine.substring(j,j+1)); }
			for(int k=0; k<len3; k++) { aL3.add(thirdLine.substring(k,k+1)); }

			for(int l=0; l<targetChars.length; l++) {
				if(aL1.contains(targetChars[l]) && aL2.contains(targetChars[l]) && aL3.contains(targetChars[l])) {
					commonChar=Character.targetChars[l];
					break;
				}
			}
			System.out.println(commonChar);		}
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