/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author slaye
 */
public class JDBC {

    static Scanner in = new Scanner(System.in);
    static final String DB_URL
            = "jdbc:derby://localhost:1527/JDBCDB";

    static final String DB_DRV
            = "org.apache.derby.jdbc.ClientDriver";
    static final String DB_USER = "preston";
    static final String DB_PASSWD = "wong";

    static Connection connection = null;
    static Statement statement = null;
    static ResultSet resultSet = null;

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        boolean isRunning = true;

        while (isRunning) {
            boolean isValid = false;
            int menuChoice = -1;
            while (!isValid) {//keeps looping until menu choice is valid

                //prints out menu
                System.out.println("Choose an option:"
                        + "\n1: List all writing groups"
                        + "\n2: List all the data for a group specified by the user (include all tables)"
                        + "\n3: List all publishers"
                        + "\n4: List all the data for a pubisher specified by the user (include all tables)"
                        + "\n5: List all book titles"
                        + "\n6: List all the data for a book specified by the user"
                        + "\n7: Insert a new book"
                        + "\n8: Insert a new publisher and update all book published by one publisher to be published by the new pubisher"
                        + "\n9: Remove a book specified by the user"
                        + "\n10: Exit Program");

                if (in.hasNextInt()) {
                    menuChoice = in.nextInt();
                    if (menuChoice > 0 && menuChoice <= 10) {
                        isValid = true;
                    } else {
                        System.out.println("You must input a valid number from the choices. Try again.");
                    }
                } else {
                    System.out.println("You must input a valid number from the choices. Try again.");
                    in.next();
                }
            }//while(!isValid)

            switch (menuChoice) {
                case 1:
                    listWritingGroups();
                    break;
                case 2:
                    listSpecifiedGroup();
                    break;
                case 3:
                    listPublishers();
                    break;
                case 4:
                    listSpecifiedPublisher();
                    break;
                case 5:
                    listBooks();
                    break;
                case 6:
                    listSpecifiedBook();
                    break;
                case 7:
                    insertBook();
                    break;
                case 8:
                    insertPub();
                    break;
                case 9:
                    removeBook();
                    break;
                case 10:
                    System.out.println("Exiting program...");
                    isRunning = false;
                    break;
            }

        }//while (isRunning)

        in.close();
    }

    //Number 1
     public static void listWritingGroups() throws SQLException {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM writinggroups");
            System.out.printf("%-20s\t%-15s\t%-15s%s\n",
                    "Group Name",
                    "Head Writer",
                    "Year Formed",
                    "Subject"
            );
            while (resultSet.next()) {
                System.out.printf("%-20s\t%-15s\t%-15d%s\n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4)
                );
            }

        } catch (SQLException ex) {

        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void listSpecifiedGroup() {
        String groupName;
        in.nextLine();
        System.out.println("Enter the name of the group to show data.");
        groupName = in.nextLine();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM writinggroups WHERE groupname='" + groupName + "'");
            if (resultSet.next() == false) {
                System.out.println("ERROR. Invalid group name");
            } else {

                System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "GroupName", "HeadWriter", "YearFormed", "Subject");
                do {
                    System.out.printf("%-20s\t%-20s\t%-20d\t%s\n",
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4));
                } while (resultSet.next());
            }
            System.out.println();

        } catch (SQLException ex) {
        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void listPublishers() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM publishers");
            System.out.printf("%-20s\t%-20s\t%-20s%-15s\n",
                    "Publisher Name",
                    "Publisher Address",
                    "Publisher Phone",
                    "Publisher Email"
            );
            while (resultSet.next()) {
                System.out.printf("%-20s\t%-20s\t%-20s%s\n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );

            }
        } catch (SQLException ex) {

        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void listSpecifiedPublisher() {
        String pubName;
        System.out.println("Enter the name of the publisher to show data.");
        in.nextLine();
        pubName = in.nextLine();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM publishers WHERE publishername='" + pubName + "'");
            if (resultSet.next() == false) {
                System.out.println("ERROR. Invalid publisher name");
            } else {
                System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "PublisherName", "PublisherAddress", "PublisherPhone", "PublisherEmail");
                do {
                    System.out.printf("%-20s\t%-20s\t%-20s\t%s\n",
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4));
                } while (resultSet.next());
            }
            System.out.println();

        } catch (SQLException ex) {
        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void listBooks() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM books");
            System.out.printf("%-20s\n",
                    "Book Title"
            );
            while (resultSet.next()) {
                System.out.printf("%-20s\n",
                        resultSet.getString(2)
                );
            }

        } catch (SQLException ex) {

        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void listSpecifiedBook() {
        String bookName;
        System.out.println("Enter the name of the book to show data.");
        in.nextLine();
        bookName = in.nextLine();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM books WHERE booktitle='" + bookName + "'");
            if (resultSet.next() == false) {
                System.out.println("ERROR. Invalid book title");
            } else {
                System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\t%s\n", "GroupName", "BookTitle", "PublisherName", "YearPublished", "NumberPages");
                do {
                    System.out.printf("%-20s\t%-20s\t%-20s\t%-20d\t%d\n",
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getInt(5));
                } while (resultSet.next());
            }
            System.out.println();

        } catch (SQLException ex) {
        } finally {
            try {

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }

    }

    public static void insertBook() throws SQLException, ClassNotFoundException {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);

            in.nextLine();
            System.out.println("Input Group Name: ");
            String groupName;
            groupName = in.nextLine();
            System.out.println("Input Book Name: ");
            String bookName;
            bookName = in.nextLine();
            System.out.println("Input Publisher Name: ");
            String pubName;
            pubName = in.nextLine();
            System.out.println("Input Year Published: ");
            int yearPub;
            yearPub = in.nextInt();
            System.out.println("Input Number of Pages: ");
            int numPages;
            numPages = in.nextInt();

            String sql = "INSERT INTO Books VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, bookName);
            preparedStatement.setString(3, pubName);
            preparedStatement.setInt(4, yearPub);
            preparedStatement.setInt(5, numPages);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | InputMismatchException ex) {
            System.out.println("ERROR. Could not insert Book.");
        } finally {
            try {
                in.nextLine();
                connection.close();
            } catch (SQLException ex) {

            }
        }

    }

    public static void insertPub() {
        String pubName;
        String pubAddress;
        String pubPhone;
        String pubEmail;
        System.out.println("Enter new publisher's name.");
        in.nextLine();
        pubName = in.nextLine();
        System.out.println("Enter new publisher's address.");
        pubAddress = in.nextLine();
        System.out.println("Enter new publisher's Phone.");
        pubPhone = in.nextLine();
        System.out.println("Enter new publisher's email.");
        pubEmail = in.nextLine();

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            String sql = "INSERT INTO publishers VALUES (?,?,?,?)";
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pubName);
            preparedStatement.setString(2, pubAddress);
            preparedStatement.setString(3, pubPhone);
            preparedStatement.setString(4, pubEmail);
            preparedStatement.executeUpdate();
            System.out.println();
            preparedStatement.close();
        } catch (SQLException ex) {
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                System.out.println("ERROR. Could not insert Publisher.");
            }
        }
        transferBooks(pubName);
    }

    public static void transferBooks(String newPub) {
        String oldPub;
        System.out.println("Enter the name of the publisher that will be bought out by the new publisher");
        oldPub = in.nextLine();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            String sql = "UPDATE Books SET PublisherName = ? WHERE PublisherName = ?";
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPub);
            preparedStatement.setString(2, oldPub);
            preparedStatement.executeUpdate();
            statement.executeQuery(sql);

            System.out.println();
            preparedStatement.close();
        } catch (SQLException ex) {
            System.out.println("ERROR. Could not transfer books because of invalid existing publisher.");

        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void removeBook() {
        String bookName;
        in.nextLine();
        System.out.println("Enter the name of the book to remove.");
        bookName = in.nextLine();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            String sql = "DELETE FROM books WHERE booktitle = ?";
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bookName);
            int deleted = preparedStatement.executeUpdate();
            if (deleted <= 0)
                System.out.println("ERROR. No such book exists in the database.");
            
            preparedStatement.close();
            System.out.println();

        } catch (SQLException ex) {
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }

    }

  
}
