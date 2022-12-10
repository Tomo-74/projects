import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1 {
	public static void main(String[] args) throws FileNotFoundException {
		File calList=new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day1.txt");
		Scanner scan=new Scanner(calList);
		String nextLine=scan.nextLine();
		int sum=0;
		int first=0;
		int second=0;
		int third=0;
		
		while(scan.hasNextLine()) {
			while(nextLine!="") {
				sum+=Integer.parseInt(nextLine);
				if(scan.hasNextLine()) nextLine=scan.nextLine();
				else break;
			}
			if(sum>third && sum>second && sum>first) {
				third=second;
				second=first;
				first=sum;
			} else if(sum>third && sum>second) {
				third=second;
				second=sum;
			} else if(sum>third) {
				third=sum;
			}
			sum=0;
			if(scan.hasNextLine()) nextLine=scan.nextLine();
		}
		System.out.println(first+second+third);
	}
}
