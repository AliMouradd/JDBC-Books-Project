

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class Main {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME ;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String sql;
            ResultSet rs = null;
            PreparedStatement preparedStatement = null;
            
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            boolean quit = false;
            do{
                System.out.println("Choose menu item:\n");
                System.out.println("1)List all writing groups\n" +
                    "2)List all the data for a group specified by the user .\n" +
                    "3)List all publishers\n" +
                    "4)List all the data for a publisher specified by the user.\n" +
                    "5)List all book titles\n" +
                    "6)List all the data for a single book specified by the user.\n" +
                    "7)Insert a new book\n" +
                    "8)Insert a new publisher and update all book published by one publisher to be published by the new pubisher.\n" +
                    "9)Remove a single book specified by the user.\n "+
                    "10) Quit.");
                int menuItem = in.nextInt();
                switch(menuItem){
                    case 1:
                        //All Writing Group
                        stmt = conn.createStatement();
                        sql = "SELECT groupname FROM writinggroups";
                        rs = stmt.executeQuery(sql);
                        
                        System.out.printf("%-30s%n", "Groupname");
                        while (rs.next()) {
                        //Retrieve by column name
                        String groupname = rs.getString("GROUPNAME");
                        //Display values
                        System.out.printf("%-30s%n",
                                dispNull(groupname));
                        }
                        System.out.println("");
                        break;
                    case 2:
                        //specific writing group
                        stmt = conn.createStatement();
                        sql = "select writinggroups.GROUPNAME, writinggroups.YEARFORMED, books.BOOKTITLE, publishers.PUBLISHERNAME from writinggroups\n" +
                                "natural join books\n" +
                                "natural join publishers\n" +
                                "where writinggroups.GROUPNAME=?";
                        preparedStatement = conn.prepareStatement(sql);

                        String findGroup;
                        in.nextLine();
                        System.out.print("Enter a specific Writing Group: ");
                        findGroup = in.nextLine();

                        preparedStatement.setString(1, findGroup);
                        rs = preparedStatement.executeQuery();

                        System.out.printf("%-30s %-30s %-40s %-30s%n", "GroupName","Year Formed","Book Title", "Publisher Name");
                        while (rs.next()) {
                            //Retrieve by column name
                            String groupName = rs.getString("groupname");
                            String yearformed = rs.getString("yearformed");
                            String bookTitle = rs.getString("booktitle");
                            String pubName = rs.getString("publishername");
                            System.out.printf("%-30s %-30s %-45s %-30s%n",
                                    dispNull(groupName), dispNull (yearformed), dispNull(bookTitle), dispNull(pubName));
                        }
                        System.out.println("");
                        break;
                    case 3:
                        // All publisher info
                        stmt = conn.createStatement();
                        sql = "SELECT publishername FROM Publishers";
                        rs = stmt.executeQuery(sql);

                        System.out.printf("%-30s%n", "Name");
                        while (rs.next()) {
                            //Retrieve by column name
                            String name = rs.getString("publishername");

                            //Display values
                            System.out.printf("%-30s%n",dispNull(name));
                        }
                        System.out.println("");
                        break;
                    case 4:
                    //List all the data for a pubisher specified by the user.
                        stmt = conn.createStatement();
                        sql = "select publishers.PUBLISHERNAME, writinggroups.GROUPNAME, writinggroups.HEADWRITER, books.BOOKTITLE from publishers\n" +
                              "natural join books\n" +
                              "natural join writinggroups\n" +
                              "where publishers.PUBLISHERNAME=?";
                        preparedStatement =conn.prepareStatement(sql);

                        String publisherName;
                        in.nextLine();
                        System.out.print("Enter a specific Publishers name: ");
                        publisherName = in.nextLine();

                        preparedStatement.setString(1,publisherName);
                        rs = preparedStatement.executeQuery();

                        System.out.printf("%-30s %-30s %-30s %-30s%n", "Publisher","Writing Group",
                                "Head Writer","Book Title");
                        while (rs.next()) {
                            //Retrieve by column name
                            String name = rs.getString("publishername");
                            String groupName = rs.getString("GROUPNAME");
                            String headWriter = rs.getString("HEADWRITER");
                            String bookTitle = rs.getString("BOOKTITLE");

                            //Display values
                            System.out.printf("%-30s %-30s %-30s %-30s%n",
                                    dispNull(name),dispNull(groupName),dispNull(headWriter),dispNull(bookTitle));
                        }
                        System.out.println("");
                        break;
                        
                    case 5:
                        //List all Book Titles
                        stmt = conn.createStatement();
                        sql = "select books.booktitle from books";
                        rs = stmt.executeQuery(sql);
                        System.out.println("List of all books:");
                        while (rs.next())   {
                            String allBooks = rs.getString("BOOKTITLE");
                            System.out.println(allBooks);
                        }
                        System.out.println();
                        break;
                    case 6:
                        //List all data for a single book specified by the user
                        stmt = conn.createStatement();
                        sql = "select books.BOOKTITLE, books.YEARPUBLISHED, books.NUMBERPAGES, books.GROUPNAME, books.PUBLISHERNAME from books\n" +
                              "natural join books\n" +
                              "natural join writinggroups\n" +
                              "where books.BOOKTITLE=?";
                        preparedStatement = conn.prepareStatement(sql);

                        String findBook;
                        in.nextLine();
                        System.out.print("Enter a speific Book Title: ");
                        findBook = in.nextLine();

                        preparedStatement.setString(1, findBook);
                        rs = preparedStatement.executeQuery();

                        System.out.printf("%-45s%-18s%-18s%-25s%-4s\n", "Book Title", "Year Published", "Number of Pages", "Group Name", "Publisher Name");
                        while (rs.next()) {
                            //Retrieve by column name
                            String bookTitle = rs.getString("BOOKTITLE");
                            String yearPublished = rs.getString("YEARPUBLISHED");
                            String numberPages = rs.getString("NUMBERPAGES");
                            String groupName = rs.getString("GROUPNAME");
                            String publishName = rs.getString("PUBLISHERNAME");

                            System.out.printf("%-45s%-18s%-18s%-25s%-4s\n",
                                    dispNull(bookTitle), dispNull(yearPublished), dispNull(numberPages), dispNull(groupName), dispNull(publishName));
                        }
                        System.out.println();
                        break;
                    case 7:
                        // Insert New Book
                        stmt = conn.createStatement();
                        String bookTitle;
                        String yearPublished;
                        String numberPages;
                        String groupName;
                        String publishName;
                        in.nextLine();

                        System.out.print("Enter the title of the book you want to add: ");
                        bookTitle = in.nextLine();
                        while (bookTitle.length() > 40){
                            System.out.print( "The title of the book (" + bookTitle + ") is over 40 characters. \nEnter a shorter title (character count: " + bookTitle.length() + "): " );
                            bookTitle = in.nextLine();
                        }
                        //Title is now under 40; next step, enter the year it was published
                        System.out.print("Enter the year the book was published: ");
                        yearPublished = in.nextLine();
                        while (yearPublished.length() > 4){
                            System.out.print( "The year published (" + yearPublished + ") is over 4 characters. \nEnter a different year (character count: " + yearPublished.length() + "): " );
                            yearPublished = in.nextLine();
                        }
                        //Year is now under 4 characters; next step, enter the number of pages
                        System.out.print("Enter the number of pages in the book: ");
                        numberPages = in.nextLine();
                        while (numberPages.length() > 100){
                            System.out.print( "The number of pages (" + numberPages + ") is over 100 characters. \nEnter a different amount (character count: " + numberPages.length() + "): " );
                            numberPages = in.nextLine();
                        }
                        //Number of pages now down to >100 characters, next step, enter the Writing Group
                        System.out.print("Enter the name of the writing group: ");
                        groupName = in.nextLine();
                        while (groupName.length() > 20){
                            System.out.print( "The group name (" + groupName + ") is over 20 characters. \nEnter a shorter group name (character count: " + groupName.length() + "): " );
                            groupName = in.nextLine();
                        }

                        System.out.print("Enter the name of the book's publisher: ");
                        publishName = in.nextLine();
                        while (publishName.length() > 20){
                            System.out.print( "The publisher (" + publishName + ") is over 20 characters. \nEnter a shorter name (character count: " + publishName.length() + "): " );
                            publishName = in.nextLine();
                        }

                        ///VERIFIES PUBLISHER//////
                        boolean pubExists = false;
                        boolean groupExists = false;
                        sql = "SELECT publishername FROM publishers";
                        rs = stmt.executeQuery(sql);
                        //Verify publisher
                        while (rs.next()){
                            String pubCompare = rs.getString("publishername");
                            if (pubCompare.equals ( publishName )) {
                                pubExists = true;
                            }
                        }
                        if (pubExists == false){
                            System.out.println("Publisher does not exist.");
                        }
                        ///////////////////////////////////////
                        ///VERIFIES WRITING GROUP//////
                        ///////////////////////////////////
                        sql = "SELECT groupname FROM writinggroups";
                        rs = stmt.executeQuery(sql);
                        //Verify publisher
                        while (rs.next()){
                            String groupCompare = rs.getString("groupname");
                            if (groupCompare.equals ( groupName )) {
                                groupExists = true;
                            }
                        }
                        if (groupExists == false){
                            System.out.println("Writing Group does not exist.");
                        }

                        //Verification Done
                        preparedStatement = conn.prepareStatement(sql);
                        if (pubExists & groupExists)    {

                            sql = "INSERT INTO books(booktitle, yearpublished,numberpages,groupname,publishername)" +
                                  "values ( ?,?,?,?,?)";
                            preparedStatement = conn.prepareStatement(sql);

                            preparedStatement.setString(1,bookTitle);
                            preparedStatement.setString(2,yearPublished);
                            preparedStatement.setString(3,numberPages);
                            preparedStatement.setString(4,groupName);
                            preparedStatement.setString(5,publishName);

                            preparedStatement.executeUpdate();
                        }

                        else    {
                            System.out.println("Cancelling Book Insert...\n");
                        }
                        System.out.println("");
                        break;
                    case 8:
                        //Insert New publisher
                        stmt = conn.createStatement();
                        String newPublishName;
                        String newPublisherAddress;
                        String newPublisherPhone;
                        String newPublisherEmail;
                        in.nextLine();
                        System.out.print("Enter the name of the Publisher you want to add:");
                        newPublishName = in.nextLine();
                        Boolean inputValidated = false;
                        while (!inputValidated)
                            {
                                preparedStatement = conn.prepareStatement("select publishername from publishers where publishername = ?");
                                preparedStatement.setString(1, newPublishName);

                                rs = preparedStatement.executeQuery();

                                if (rs.next())
                                {
                                    System.out.println("Publisher is already in the database");
                                    System.out.print("Enter new publisher name: ");
                                    newPublishName = in.nextLine();
                                }
                                else
                                {
                                    inputValidated = true;
                                }
                            }
                        while (newPublishName.length() > 20){
                            System.out.print( "The Publisher's name (" + newPublishName + ") is over 20 characters. \nEnter a shorter name (character count: " + newPublishName.length() + "): " );
                            newPublishName = in.nextLine();
                        }
                        //Title is now under 40; next step, enter the year it was published
                        System.out.print("Enter the publishers address: ");
                        newPublisherAddress = in.nextLine();
                        while (newPublisherAddress.length() > 50){
                            System.out.print( "The publisher's address (" + newPublisherAddress + ") is over 50 characters. \nEnter a shorter address (character count: " + newPublisherAddress.length() + "): " );
                            newPublisherAddress = in.nextLine();
                        }
                        //Year is now under 4 characters; next step, enter the number of pages
                        System.out.print("Enter the Publisher's Phone Number: ");
                        newPublisherPhone = in.nextLine();
                        while (newPublisherPhone.length() > 12){
                            System.out.print( "The phone number (" + newPublisherPhone + ") is over 12 characters. \nEnter a number (character count: " + newPublisherPhone.length() + "): " );
                            newPublisherPhone = in.nextLine();
                        }
                        //Number of pages now down to >100 characters, next step, enter the Writing Group
                        System.out.print("Enter Publisher's Email: ");
                        newPublisherEmail = in.nextLine();
                        while (newPublisherEmail.length() > 30){
                            System.out.print( "The email (" + newPublisherEmail + ") is over 30 characters. \nEnter a shorter email (character count: " + newPublisherEmail.length() + "): " );
                            newPublisherEmail = in.nextLine();
                        }

                        String oldPublisherName;
                        System.out.print("Enter the name of the Publisher you want to update: ");
                        oldPublisherName = in.nextLine();
                        inputValidated = false;
                        while (!inputValidated)
                                    {
                                        preparedStatement = conn.prepareStatement("select publishername from publishers where publishername = ?");
                                        preparedStatement.setString(1, oldPublisherName);

                                        rs = preparedStatement.executeQuery();

                                        if (!rs.next())
                                        {
                                            System.out.println("That publisher is not in the database");
                                            System.out.print("Publisher Name to Change: ");
                                            oldPublisherName = in.nextLine();                      
                                        }
                                        else
                                        {
                                            inputValidated = true;
                                        }                        
                                    }
                        sql ="Insert into publishers (publishername,publisheraddress,publisherphone,publisheremail)"
                                +"values (?,?,?,?)";
                        preparedStatement = conn.prepareStatement(sql);

                        preparedStatement.setString(1,newPublishName);
                        preparedStatement.setString(2,newPublisherAddress);
                        preparedStatement.setString(3,newPublisherPhone);
                        preparedStatement.setString(4,newPublisherEmail);

                        preparedStatement.executeUpdate();

                        preparedStatement = conn.prepareStatement("update books set publisherName = ? where publisherName = ?");
                        preparedStatement.setString(1, newPublishName);
                        preparedStatement.setString(2, oldPublisherName);

                        preparedStatement.executeUpdate();
                        System.out.println("Publisher successfully added and changed!");
                        System.out.println("");
                        break;
                    case 9:
                        //book delete
                        in.nextLine();
                        System.out.print("Book to Delete: ");
                        String booksTitle = in.nextLine();
                                              
                        preparedStatement = conn.prepareStatement("delete from books where booktitle = ?");
                        preparedStatement.setString(1, booksTitle);

                        int success = preparedStatement.executeUpdate();
                        if(success == 1)
                        {                       
                            System.out.println("Book Successfully deleted");                     
                        }
                        else
                        {                      
                            System.out.println("Specified book " + booksTitle + " does not exist. Try again");                       
                        }
                            
                            System.out.println("");
                            break;
                    case 10:
                        quit = true;
                        break;
                    default:
                        System.out.println("Invalid Choice.");
                    
                }
            }while(!quit);
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end FirstExample}

