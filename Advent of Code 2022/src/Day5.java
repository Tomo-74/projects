import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Day5 {
	public static void main(String[] args) throws FileNotFoundException {
		File input = new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day5.txt");
		Scanner scan = new Scanner(input);
		
		ArrayList<String> stacks = new ArrayList<>();
		
		for(int i=0; i<3; i++) {
			String line=scan.nextLine();
//			System.out.println(line);
			Scanner chars=new Scanner(line);
			while(chars.hasNext()) {
				String curChar=chars.next();
				String crate="0";
				System.out.print(curChar);
				if(curChar.equals("[")) {
					crate=chars.next();
					System.out.println(crate);
				}
			}
		}
	}
}
