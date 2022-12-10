import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

/*
 * A B C
 * X Y Z
 * 
 * Rock beats scissors (A beats Z, X beats C)
 * Paper beats rock (B beats X, Y beats A)
 * Scissors beats paper (C beats Y, Z beats B)
 */


public class Day2 {
	public static void main(String[] args) throws FileNotFoundException {
		File guide=new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day2.txt");
		Scanner scan=new Scanner(guide);
		int total=0;
		
		while(scan.hasNextLine()) {
			String curLine=scan.nextLine();
			String opp=curLine.substring(0,1);
			String me=curLine.substring(2,3);
			
			/* Possible outcomes:
			 * R R	D
			 * R P	W
			 * R S	L
			 * 
			 * P R	L
			 * P P	D
			 * P S	W
			 * 
			 * S R	W
			 * S P	L
			 * S S	D
			 */

			// Change me choice based on if I need to win, lose, or draw
			if(opp.equals("A") && me.equals("X")) me="Z";	
			else if(opp.equals("A") && me.equals("Y")) me="X"; 
			else if(opp.equals("A") && me.equals("Z")) me="Y";
			else if(opp.equals("C") && me.equals("X")) me="Y";
			else if(opp.equals("C") && me.equals("Y")) me="Z";
			else if(opp.equals("C") && me.equals("Z")) me="X";

			// Compare choices
			if(opp.equals("A") && me.equals("X")) total+=(3+1);
			else if(opp.equals("A") && me.equals("Y")) total+=(6+2);
			else if(opp.equals("A") && me.equals("Z")) total+=(0+3);
			else if(opp.equals("B") && me.equals("X")) total+=(0+1);
			else if(opp.equals("B") && me.equals("Y")) total+=(3+2);
			else if(opp.equals("B") && me.equals("Z")) total+=(6+3);
			else if(opp.equals("C") && me.equals("X")) total+=(6+1);
			else if(opp.equals("C") && me.equals("Y")) total+=(0+2);
			else if(opp.equals("C") && me.equals("Z")) total+=(3+3);
			else System.out.println("Unnacounted-for possibility 2");
		}
		scan.close();
		System.out.println(total);
	}
}
