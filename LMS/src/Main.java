import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ArrayList<Book> bookList = new ArrayList<>();
		
		for(int i=0; i<args.length; i+=2) bookList.add(new Book(args[i], args[i+1]));
		
		CheckOut checkout = new CheckOut(bookList);
		System.out.println(checkout.toString());
	}
}
