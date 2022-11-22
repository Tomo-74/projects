
public class Book {
	private String title;
	private String author;
	
	public Book() {
		this.title = "";
		this.author = "";
	}
	
	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
	
	public String getTitle() { return this.title; }
	
	public String getAuthor() { return this.author; }
	
	public String toString() {
		return "\"" + title + "\" by " + author; 
	}
}
