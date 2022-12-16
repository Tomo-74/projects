import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day4 {
	public static void main(String[] args) throws FileNotFoundException {
		File input=new File("C:\\Users\\tomol\\git\\Personal\\Advent of Code 2022\\Input\\Day4.txt");
		Scanner scan=new Scanner(input);
		int overlap=0;
		
		while(scan.hasNextLine()) {
			String curLine=scan.nextLine();
			String p1=curLine.substring(0, curLine.indexOf(','));						// Pairs
			String p2=curLine.substring(curLine.indexOf(',')+1, curLine.length());
			int l1=Integer.parseInt(p1.substring(0, p1.indexOf("-")));					// Lower range 1
			int u1=Integer.parseInt(p1.substring(p1.indexOf("-")+1, p1.length()));		// Upper range 1
			int l2=Integer.parseInt(p2.substring(0, p2.indexOf("-")));					// Lower range 2
			int u2=Integer.parseInt(p2.substring(p2.indexOf("-")+1, p2.length()));		// Upper range 2
			int[] zone1=new int[u1-l1+1];	// "Zones" representing the areas a particular elf has to clean
			int[] zone2=new int[u2-l2+1];
			
			// Expand the first range and populate an array with it
			int middleNum=l1;
			for(int i=0; i<u1-l1+1; i++) {
				zone1[i]=middleNum;
				middleNum++;
			}
			
			// Expand the second range and populate an array with it
			middleNum=l2;
			for(int j=0; j<u2-l2+1; j++) {
				zone2[j]=middleNum;
				middleNum++;
			}
			
			// Compare the two arrays to find occurrences of overlap
			for(int i=0; i<zone1.length; i++) {
				boolean found=false;
				for(int j=0; j<zone2.length; j++) {
					if(zone1[i]==zone2[j]) {
						found=true;
						overlap++;
						break;
					}
				}
				if(found) break;
			}
		}
		System.out.println(overlap);
	}
}
// 2 completely contains
// 4 overlaps