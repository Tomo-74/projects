import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CheckOut {
	private final int BOOK_LIMIT = 10;
	private ArrayList<Book> bookList;
	
	private LocalDate date = LocalDate.now();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
	private Date dateCheckedOut;
	private Date dateDue;
	private Calendar c;

	public CheckOut() {
		this.bookList = null;
		this.dateCheckedOut = this.dateDue = null;
	}
	
	public CheckOut(ArrayList<Book> bookList) {
		if(bookList.size() > 10) System.out.println("Checkout limit is 10 books. Please try again with fewer books.");
		else {
			this.bookList = bookList;
			c = Calendar.getInstance();
			c.setTime(new Date());
			dateCheckedOut = c.getTime();
			c.add(Calendar.DATE, 31);
			dateDue = c.getTime();
		}
	}
	
	private String bookListToString() {
		String ts = "";
		String copy;
		for(Book b: bookList) {
			ts += b.toString() + "\n";
		}
		copy = ts;
		return copy;
	}

	public String toString() {
		return "Books out:\n" + bookListToString() + "\nDate out: " + dateCheckedOut + "\nDate due: " + dateDue;
	}
}
