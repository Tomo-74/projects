import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This application will keep track of things like what classes are offered by
 * the school, and which students are registered for those classes and provide
 * basic reporting. This application interacts with a library database to store and
 * retrieve data.
 */
public class LibraryManagementSystem {
	static String userID;
	static String firstName;
	static String lastName;
	static int numCheckouts;	// The number of books a user currently has checked out
	
	/**
	 * Utility method to print a line of dashes for formatting.
	 */
	public static void printLine() { System.out.println("-".repeat(80)); }
	
	/**
	 * Prompts a user for his/her user ID number. Then communicates with the database to see
	 * if the provided ID matches a row in the users table. If a match is found, the user is
	 * "logged in", meaning his/her data is stored by the program and used to automatically
	 * populate future queries. If no match is found, an error is thrown and the user must try
	 * again.
	 */
	public static void logIn() {
		boolean loggedIn = false;
		Scanner scan = new Scanner(System.in);	// Cannot close this Scanner because doing so also closes System.in, which is needed for later scanners
		
		while(!loggedIn) {
			Connection connection = null;
			Statement sqlStatement = null;
			userID = "-1";	// Default to a user ID that is not in the database

			System.out.println("\n");
			
			try {
				// Prompt the user for his/her ID. This ID will be used for all subsequent checkouts and returns
				System.out.println("User ID #: ");
				userID = scan.nextLine();
				
				connection = Database.getDatabaseConnection();
				sqlStatement = connection.createStatement();
				String query = String.format("SELECT * FROM users WHERE user_id = %s;", userID);
				ResultSet rs = sqlStatement.executeQuery(query);
				
				// Parse the user's data
				while(rs.next()) {
					firstName = rs.getString(2);
					lastName = rs.getString(3);
					numCheckouts = rs.getInt(4);
				}
				
				if(firstName == null) {		// If the user does not exist in the users table
					throw new LogInException();
				} else {					// Else if the user exists
					loggedIn = true;
					System.out.println("User " + userID + " logged in");
					System.out.println("Welcome, " + firstName);
					printLine();
				}
			} catch(SQLException sqlException) {
				System.out.println("Failed to log in. Error in SQL syntax.");
				System.out.println(sqlException.getMessage());
			} catch(LogInException logInException) {
				System.out.println("Failed to log in user for ID " + userID + ": user does not exist in database.");
				printLine();
			} catch(Exception e) {
				System.out.println(e.toString());
	        }
		}
	}
	
    /*
     * Inserts a new user entry into the database's users table.
     * 
     * @param firstName the new user's first name
     * @param lastName the new user's last name
     */
    public static void createUser(String firstName, String lastName) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
             connection = Database.getDatabaseConnection();
             sqlStatement = connection.createStatement();

             String insert = String.format("INSERT INTO users(first_name, last_name) VALUES('%s', '%s');", firstName, lastName);
             int rs1 = sqlStatement.executeUpdate(insert);
             
             // Print out the new user's data from the table
             String query = String.format("SELECT user_id, first_name, last_name, num_checkouts FROM users WHERE first_name LIKE '%s' AND last_name LIKE '%s';", firstName, lastName);
             ResultSet rs2 = sqlStatement.executeQuery(query);
             
             System.out.println("User ID | First Name | Last Name | Current checkouts");
             printLine();
             
             while(rs2.next()) {
            	 String output = String.format("%d | %s | %s | %s", rs2.getInt(1), rs2.getString(2), rs2.getString(3), rs2.getString(4));
            	 System.out.println(output);
             }
        } catch (SQLException sqlException) {
            System.out.println("Failed to create user");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    /*
     * Deletes a user entry from the database's users table.
     */
    public static void deleteUser(String userID) {
        Connection connection = null;
        Statement sqlStatement = null;
        
        // Data of the user-to-be-deleted
        String firstName = "";
        String lastName = "";
        int numCheckouts = 1;	// For safety, assume the user has one book checked out

        try {
             connection = Database.getDatabaseConnection();
             sqlStatement = connection.createStatement();
             
             // Query the user's data before deleting
             String query = String.format("SELECT * FROM users WHERE user_id = %s", userID);
             ResultSet userData = sqlStatement.executeQuery(query);
                          
             // Parse the user's data
             while(userData.next()) {
            	 firstName = userData.getString(2);
            	 lastName = userData.getString(3);
            	 numCheckouts = userData.getInt(4);
             }
             
             if(numCheckouts > 0) {
            	 System.out.println("Cannot delete user " + userID + ": user currently has " + numCheckouts + " books checked out.");
             } else {
            	 // Delete the user
            	 String delete = String.format("DELETE FROM users WHERE user_id = %s;", userID);
            	 sqlStatement.executeUpdate(delete);
            	 printLine();
            	 String message = String.format("User " + userID + " %s %s deleted", firstName, lastName);
            	 System.out.println(message);
             }
             
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    
    /*
     * Prints information about book checkouts in the database, according to the specified mode.
     * 
     * USAGE: list checkouts <chrono | user ID>
     * 
     * Mode 1: list all checkouts 								--> list checkouts
     * Mode 2: list checkouts belonging to a particular user 	--> list checkouts <userID>
     * Mode 3: list checkouts in order of oldest to newest 		--> list checkouts chrono 
     * 
     * @param userID the ID of the user whose checkouts you want to see (mode 2). 0 if running in mode 1 or 3
     * @param mode the mode to run the function in
     */
    public static void listCheckouts(int userID, int mode) {
    	Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	
        	String queryAllCheckouts = "SELECT checkout_id, title, users.user_id, first_name, last_name, checkout_date "
        				 			 + "FROM checkouts "
        				 			 + "JOIN users ON checkouts.user_id = users.user_id "
        				 			 + "JOIN books ON checkouts.book_id = books.book_id "
        				 			 + "ORDER BY checkout_id;";
        	
        	String queryCheckoutsByUser = String.format("SELECT checkout_id, title, users.user_id, first_name, last_name, checkout_date "
        											  + "FROM checkouts "
        											  + "JOIN users ON checkouts.user_id = users.user_id "
        											  + "JOIN books ON checkouts.book_id = books.book_id "
        											  + "WHERE users.user_id = %d "
        											  + "ORDER BY checkout_id;", userID);
        	
        	String queryCheckoutsByDate = "SELECT checkout_id, title, users.user_id, first_name, last_name, checkout_date "
        								+ "FROM checkouts "
        								+ "JOIN users ON checkouts.user_id = users.user_id "
        								+ "JOIN books ON checkouts.book_id = books.book_id "
        								+ "ORDER BY checkout_date DESC;";
        	
        	String query = "";	// Select the query-to-use based on what mode was passed in
        	
        	switch(mode) {
        		case 1:
        			query = queryAllCheckouts;
        			break;
        		case 2:
        			query = queryCheckoutsByUser;
        			break;
        		case 3:
        			query = queryCheckoutsByDate;
        			break;
    			default:
    				query = queryAllCheckouts;
        	}
        	        	
        	ResultSet rs = sqlStatement.executeQuery(query);
        	System.out.println("Checkout ID # | Book Title | User ID | First Name | Last Name | Date Checked Out");
        	printLine();
        	
        	while(rs.next()) {
        		int checkoutID = rs.getInt(1);
        		String title = rs.getString(2);
        		int user_ID = rs.getInt(3);
        		String firstName = rs.getString(4);
        		String lastName = rs.getString(5);
        		Date dateCheckedOut = rs.getDate(6);
        		
        		String output = String.format("%d | %s | %s | %s | %s | %s", checkoutID, title, user_ID, firstName, lastName, dateCheckedOut);
        		System.out.println(output);
        	}
        } catch (SQLException sqlException) {
            System.out.println("Failed to retrieve chekouts");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    
    /*
     * Prints all books available in the library. Behind the scenes, this is done by
     * printing all entries in the books table in the database.
     */
    public static void listBooks() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
             connection = Database.getDatabaseConnection();
             sqlStatement = connection.createStatement();
             String query = "SELECT * FROM books;";
             ResultSet rs = sqlStatement.executeQuery(query);
             
             System.out.println("Book ID | Title | Author | Status | Available Copies | Section");
             printLine();
             
             while(rs.next()) {
            	 int bookID = rs.getInt(1);
            	 String title = rs.getString(2);
            	 String author = rs.getString(3);
            	 String status = rs.getString(4);
//            	 int numCopies = rs.getInt(5);
            	 int numAvailableCopies = rs.getInt(6);
            	 String section = rs.getString(7);
            	 
            	 String output = String.format("%d | %s | %s | %s | %d | %s", bookID, title, author, status, numAvailableCopies, section);
            	 System.out.println(output);
             }
        } catch (SQLException sqlException) {
            System.out.println("Failed to retrieve books");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    
    /*
     * Prints all users in the database
     */
    public static void listAllUsers() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
            String query = "SELECT user_id, first_name, last_name, num_checkouts FROM users;";
            ResultSet rs = sqlStatement.executeQuery(query);
            
            System.out.println("User ID | First Name | Last Name | Books Checked Out");
            printLine();
            
            while(rs.next()) {
            	int userID = rs.getInt(1);
            	String firstName = rs.getString(2);
            	String lastName = rs.getString(3);
            	int numCheckouts = rs.getInt(4);
            	
            	String output = String.format("%d | %s | %s | %d", userID, firstName, lastName, numCheckouts);
            	System.out.println(output);
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get users");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    
    /**
     * Checks out a book to a user. Only works if the books has available copies.
     * 
     * @param bookID the ID of the book to check out
     * @param userID the ID of the user to check the book out to
     */
    public static void checkout(String bookID, String userID) {
        Connection connection = null;
        Statement sqlStatement = null;
        int numAvailableCopies = 0;

        try {
             connection = Database.getDatabaseConnection();
             sqlStatement = connection.createStatement();
             
             // Query the number of available copies of the book
             String queryCopies = String.format("SELECT num_available_copies FROM books WHERE book_id = %s", bookID);
             ResultSet rs1 = sqlStatement.executeQuery(queryCopies);
             while(rs1.next()) numAvailableCopies = rs1.getInt(1);

             // If there are copies of the book available, check it out
             if(numAvailableCopies > 0) {

            	 // Create a new entry in the checkouts table
            	 String insertCheckout = String.format("INSERT INTO checkouts (book_id, user_id) VALUES (%s, %s);", bookID, userID);
            	 sqlStatement.executeUpdate(insertCheckout);
            	 
            	 // Decrement the number of available copies for the book
            	 String decrementCopies = String.format("UPDATE books SET num_available_copies = num_available_copies-1 WHERE book_id = %s", bookID);
            	 sqlStatement.executeUpdate(decrementCopies);
            	 numAvailableCopies--;
            	 
            	 // If there are now 0 available copies, set the book's status to 'Checked Out'
            	 if(numAvailableCopies == 0) {
            		 String updateStatus = String.format("UPDATE books SET status = 'Checked Out' WHERE book_id = %s", bookID);
            		 sqlStatement.executeUpdate(updateStatus);
            	 }
             } else {
            	 System.out.println("All copies of the book are currently checked out.");
             }
             
        } catch (SQLException sqlException) {
            System.out.println("Failed to check out book");
            System.out.println(sqlException.getMessage());
            
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    
//  public static void returnBook(int book_id) {
//  Connection connection = null;
//  Statement sqlStatement = null;
//
//  try {
//       connection = Database.getDatabaseConnection();
//       sqlStatement = connection.createStatement();
//       String query = "SELECT "
//       			+ "	   instructors.first_name,"
//       			+ "    instructors.last_name,"
//       			+ "    academic_titles.title,"
//       			+ "    classes.code,"
//       			+ "    classes.name,"
//       			+ "    terms.name "
//       			+   "FROM instructors "
//       			+   "JOIN academic_titles ON instructors.academic_title_id = academic_titles.academic_title_id "
//       			+   "JOIN class_sections ON instructors.instructor_id = class_sections.instructor_id "
//       			+   "JOIN classes ON class_sections.class_id = classes.class_id "
//       			+   "JOIN terms ON class_sections.term_id = terms.term_id ";
//       String whereClause = String.format("WHERE instructors.first_name LIKE '%s' AND instructors.last_name LIKE '%s'", first_name, last_name);
//       query += whereClause;
//
//       ResultSet rs = sqlStatement.executeQuery(query);
//       
//       System.out.println("First Name | Last Name | Title | Code | Name | Term");
//       printLine();
//       
//       while(rs.next()) {
//      	 String firstName = rs.getString(1);
//      	 String lastName = rs.getString(2);
//      	 String title = rs.getString(3);
//      	 String classCode = rs.getString(4);
//      	 String className = rs.getString(5);
//      	 String term = rs.getString(6);
//      	 
//      	 String output = String.format("%s | %s | %s | %s | %s | %s", firstName, lastName, title, classCode, className, term);
//      	 System.out.println(output);
//       }
//      
//  } catch (SQLException sqlException) {
//      System.out.println("Failed to get class sections");
//      System.out.println(sqlException.getMessage());
//
//  } finally {
//      try {
//          if (sqlStatement != null)
//              sqlStatement.close();
//      } catch (SQLException se2) {
//      }
//      try {
//          if (connection != null)
//              connection.close();
//      } catch (SQLException se) {
//          se.printStackTrace();
//      }
//  }
//}
    
    
    /**
     * Splits a string up by spaces. Spaces are ignored when wrapped in quotes.
     *
     * @param command - School Management System cli command
     * @return splits a string by spaces.
     */
    public static List<String> parseArguments(String command) {
        List<String> commandArguments = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) commandArguments.add(m.group(1).replace("\"", ""));
        return commandArguments;
    }

    public static void main(String[] args) {
    	printLine();
        System.out.println("Welcome to the St. Paul's Library Management System");
        printLine();

//        logIn();	// Prompt the user for his/her ID

    	String command = "";
        Scanner scan = new Scanner(System.in);

        do {
        	System.out.print("\nCommand: ");
        	command = scan.nextLine();
            
            List<String> commandArguments = parseArguments(command);
            command = commandArguments.get(0);
            commandArguments.remove(0);

            if (command.equals("help")) {
                System.out.println("-".repeat(38) + "Help" + "-".repeat(38));
                System.out.println("test connection \n\tTests the database connection");

                System.out.println("\nlist users \n\tLists all the users in the system");
                System.out.println("\nlist books \n\tLists all the books in the library");
                System.out.println("\nlist checkouts <chrono | user_ID>\n\tLists all current checkouts.\n\t"
                		+ "If option chrono is provided, lists the current checkouts in chronological order.\n\t"
                		+ "If option user_ID is provided, lists the current checkouts of the user with that ID.\n\t"
                		+ "If no option is provided, lists all the current checkouts.");

                System.out.println("\ncreate user <first_name> <last_name> \n\tCreates a user with the given name");
                System.out.println("\ndelete user <userId> \n\tDeletes the user with the given ID");
                System.out.println("\ncheckout book <book_id> <user_id> \n\tChecks out the given book under the given user");

//                System.out.println("\nsubmit grade <studentId> <class_section_id> <letter_grade> \n\tCreates a student");
                System.out.println("\nhelp \n\tLists help information");
                System.out.println("\nquit \n\tExits the program");
                System.out.println("\nexit \n\tExits the program");
            } else if (command.equals("test") && commandArguments.get(0).equals("connection")) {
                Database.testConnection();
            } else if (command.equals("list")) {
                if (commandArguments.get(0).equals("users")) listAllUsers();
                if (commandArguments.get(0).equals("books")) listBooks();
                if (commandArguments.get(0).equals("checkouts")) {
                	commandArguments.remove(0);
                	try {
                		command = commandArguments.get(0);	// Check for third word in command
                		if(command.equals("chrono")) listCheckouts(0, 3);	// If the third word is "chrono", run in mode 3: list checkouts in chronological order
                		else listCheckouts(Integer.parseInt(command), 2);	// Otherwise, run in mode 2: list checkouts of a given user
                	} catch (IndexOutOfBoundsException e) {
                		listCheckouts(0, 1);	// Run in mode 1: list all checkouts
                	} catch (Exception e) {
                		System.out.println(e.toString());
                		System.out.println("USAGE: list checkouts <chrono | userID>");
                	}
                }
            } else if (command.equals("create")) {
                if (commandArguments.get(0).equals("user")) {
                    createUser(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("return")) {
                if (commandArguments.get(0).equals("book")) {
//                    returnBook(commandArguments.get(1));
                }
            } else if (command.equals("checkout")) {
                checkout(commandArguments.get(0), commandArguments.get(1));
            } else if (command.equals("delete")) {
                if (commandArguments.get(0).equals("user")) {
                    deleteUser(commandArguments.get(1));
                }
            } else if (!(command.equals("quit") || command.equals("exit"))) {
                System.out.println(command);
                System.out.println("Command not found. Enter 'help' for list of commands");
            }
            printLine();
        } while (!(command.equals("quit") || command.equals("exit")));
        System.out.println("Bye!");
        scan.close();
    }
}