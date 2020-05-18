import java.util.*;
import java.time.LocalDate; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.sql.*; // connect with  DB

/**
 * Main class
 * This class is the entry point to the application
 * 
 * @author Israel Bango
 */
public class Main {
	static int projNumber = 0; // this static variable will keep track of project number
	static Scanner userInput = new Scanner(System.in); // Scanner object to read user inputs throughout the program
	/* set variables for DB connection*/
	static Connection connection; //DB connection object
	static Statement statement ; // SQL statements to DB object
	static PreparedStatement preparedStmt; // Object  to represent a precompiled SQL statement.
	static ResultSet results ; // hold data retrieved from a database
	
	/**
	 * <h3>Main method</h3> Entry point of program <br>
	 * Allows the user to choose options from a menu and calls respective methods
	 * 
	 * @param args It stores Java command line arguments 
	 * @since version 1(cap_stone 1)
	 * 
	 */
	public static void main(String[] args) {
		// Try to establish connection with the database
		try {
			connectDB(); // connect to database
			statement =  connection.createStatement(); // create direct line to DB for running queries
			//preparedStmt =  connection.createStatement();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print("You can't do much because there's no connectio with the DB\n");
		}
		
		System.out.print("=====================================================================================\n");
		System.out.print("==================== Structural engineering firm \" Poised\" ========================\n");
		System.out.println("===================================================================================\n");

		while (true) {
			System.out.print("\n\n==============================Main menu======================================\n");
			System.out.print("\nPlease choose an option from the menu bellow\n");
			System.out.print("\n1 - Create new Project\n" + "2 - Update existing projects\n"
					+ "3 - View all projects from the database\n" + "4 - Finalize existing projects\n"
					+ "5 - View pending projects\n" + "6 - View overdue projects \n"
					+ "7 - Find project by number or name\n" + "8 - Exit program\n");

			String option = userInput.nextLine(); // read input entered by user
			switch (option) {
			case "1":
				// Create new project
				try {
					createNew();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't save project to the data base\n");
				}
				break;

			case "2":
				// Update Projects
				try {
					updateProject();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't Update project\n");
				}
				break;

			case "3":
				// See all projects
				System.out.print("======================= ALL PROJECTS ==============================\n");
				try {
					getExistingProjects();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't get projects from the data base\n");	
				}
				break;

			case "4":
				// Finalize projects
				try {
					finalizeMenu();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't finalize project\n");	
				}
				break;

			case "5":
				// See pending projects
				try{
					pendingProject();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't retrieve pending projects\n");
				}
				break;

			case "6":
				// See overdue projects
				try {
					overDueProject();
				} catch(SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't retrieve overdue projects\n");
					
				}
				break;
				
			case "7":
				// find project by number or name
				try {
					findProject();
				}catch(SQLException e) {
					e.printStackTrace();
					System.out.print("Couldn't find project\n");
					
				}
				break;
				
			case "8":
				// Close up all connections
				try {
					results.close();  
					statement.close ();   
					preparedStmt.close();
					connection.close ();
					userInput.close();
				} catch (Exception e) {
					
				}
				System.out.print("Exiting program... bye! \n");
				System.exit(0); // terminate program
				
			default:
				System.out.print("Invalid selection. Try again\n");
				break;

			}

		}

	}

	/**
	 * Method converts user date input to LocalDate object if it cannot convert the
	 * user input it throws an exception that is caught in the calling function
	 * 
	 * @param strDate The date format in string type that will be converted to
	 *                LocalDate object
	 * @return date object
	 * @since version 1
	 * 
	 */
	static LocalDate getDateFormat(String strDate) {
		LocalDate dateObj = null;
		DateTimeFormatter myFormatObj = null;
		myFormatObj = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // format how date object will be
		dateObj = LocalDate.parse(strDate, myFormatObj); // convert string entered by user to LocalDate object
		return dateObj; // return object to calling function
	}

	/**
	 * Method saves Person information related to the project. Ask user for person
	 * details and save it to the project
	 * 
	 * @param project The project whose person we want to save information
	 * @param whoIs   who the person is. whether a customer, contractor or architect
	 * @since version 1
	 * 
	 */
	static void insertPersonInfo(Project project, String whoIs) {
		String name = "";
		String email = "";
		String phone = "";
		String add = "";

		// ask for user input
		System.out.print("Enter name: ");
		name = userInput.nextLine();
		while (true) {
			try {
				System.out.print("Enter email: ");
				email = userInput.nextLine();

				// validate email address
				// regular expression bellow for email validation
				String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." 
						+ "[a-zA-Z0-9_+&*-]+)*@" 
						+ "(?:[a-zA-Z0-9-]+\\.)+[a-z"
						+ "A-Z]{2,7}$";

				if (email.matches(emailRegex)) { // if email is valid
					break;
				} else { // email is not valid
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("*Invalid email*");
				continue;
			}

		}

		// Makes sure the user enters valid phone number format
		while (true) {
			try {
				System.out.print("Enter Telephone: ");
				phone = userInput.nextLine();
				// validate phone number
				// 1) Begins with 0 or +27
				// 2) then contains 9 digits
				if (phone.matches("[0/+27]?[0-9]+") && (phone.length() >= 9 && phone.length() <= 12)) {
					// if phone matches regular expression
					// This is a valid number
					break;
				} else { // invalid number
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("*Invalid phone number*");
				continue;
			}
		}

		System.out.print("Enter address:\n");
		add = userInput.nextLine();

		// save information based on role of person in project
		if (whoIs == "customer") {
			// add customer information to customer object
			project.setCustomer(name, phone, email, add, whoIs);
			
		} else if (whoIs == "architect") {
			// add architect information to architect object
			project.setArchitect(name, phone, email, add, whoIs);

		} else if (whoIs == "manager") {
			// add contractor information to contractor object
			project.setManager(name, phone, email, add, whoIs);
			
		} else if (whoIs == "engineer") {
			project.setEngineer(name, phone, email, add, whoIs);
		}
	}

	
	/**
	 * Method creates a new project. <br>
	 * Asks user to enter project details and save them to database
	 * 
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 1
	 * 
	 */
	static void createNew() throws SQLException {
		Project project = new Project(); // Project object
		System.out.print("\n=======Create New Project======= \n\n");

		// ===== ask for project information=========
		System.out.print("Enter project name: \n");
		String projName = userInput.nextLine(); // read user inputFile
		project.setName(projName); // save input to project
		
		projNumber = countProjectsInDB(); // count the number of projects in DB and assign to projNumber
		
		System.out.println("== The number of this project is " + (++projNumber) + " ==\n"); // current project number
		project.setNumber(projNumber); // save project number to project

		System.out.print("Enter project address:\n");
		String projAdd = userInput.nextLine(); // read user input
		project.setAddress(projAdd); // save input to project

		System.out.print("Enter project erf number:\n");
		String erf = userInput.nextLine(); // read user input
		project.setErfNumber(erf); // save input to project

		System.out.print("Enter project type(eg:house, apartment etc...):\n");
		String buildType = userInput.nextLine();
		project.setType(buildType);

		// try block bellow makes sure user enter number for amount being charged
		while (true) {
			try {
				System.out.print("What is the amount being charged for this  project ?:\n");
				String projPrice = userInput.nextLine();
				project.setFee(Double.parseDouble(projPrice));
				break;
			} catch (Exception e) {
				System.out.println("*make sure you enter a monetary value*\n");
				continue;
			}
		}

		// call method bellow that makes sure the user enters the correct date format
		// Method receives the location of the project in the list we want to enter due
		// date
		enterProjectDueDate(project);

		// ==== ask for customer's information===
		System.out.print("________________________________________________\n");
		System.out.print("Enter Customer\'s details\n\n");

		insertPersonInfo(project, "customer"); // save customer information to project

		// This code bellow checks whether project name was captured or not
		// if not; save project name with building type name plus the surname of
		// customer
		projName = projName.replaceAll(" ", ""); // remove any white space from the if statement bellow to ignore white
												// spaces

		if (projName.isEmpty()) {
			String str = project.getCustomer().getName(); // get name of customer
			String[] arrOfStr = str.split(" ", 0); // split user name in order to get surname(last name)

			// save project name with building type and surname of customer
			String newName = project.getType() + " " + arrOfStr[arrOfStr.length - 1];
			project.setName(newName);
		}
		
		// ==== ask for manager information====
		System.out.print("________________________________________________\n");
		System.out.print("Enter Manager\'s details\n\n");
		insertPersonInfo(project, "manager"); // save contractor information to project
		
		// ==== ask for architect's information====
		System.out.print("________________________________________________\n");
		System.out.print("Enter Architect\'s details\n\n");
		insertPersonInfo(project, "architect"); // save architect information to project
		
		// ==== ask for manager information====
		System.out.print("________________________________________________\n");
		System.out.print("Enter Engineer\'s details\n\n");
		insertPersonInfo(project, "engineer"); // save contractor information to project
		
		saveProjToDB(project); // save this project to Database
	}
	
	/**
	 * Method saves project passed as parameter to the dataBase<br>
	 * SQL queries are performed to do that
	 * 
	 * @param project The project to be saved to Database
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 4
	 * 
	 */
	static void saveProjToDB(Project project) throws SQLException {
		try {
			
		/* Save project to DB */
		
		String query = " INSERT INTO Project(projNumber, projName, buildtype, address, ErFNum, fee, amountPaid, projDateAssigned, projDeadLine)"  
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";  // Query with placeholder for the data
		
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, project.getNumber());
		preparedStmt.setString(2, project.getName());
		preparedStmt.setString(3, project.getType());
		preparedStmt.setString(4, project.getAddress());
		preparedStmt.setString(5, project.getErfNumber());
		preparedStmt.setDouble(6, project.getFee());
		preparedStmt.setDouble(7, project.getAmountPaid());
		preparedStmt.setDate(8, java.sql.Date.valueOf(project.getDate()));
		preparedStmt.setDate(9, java.sql.Date.valueOf(project.getDeadline()));
		preparedStmt.execute(); // run query
		
		// Save customer to customer table
		query = " INSERT INTO Customer (name, telephone, email, address, projNumber) VALUES(?,?,?,?,?)"; // Query with placeholder for the data
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, project.getCustomer().getName());
		preparedStmt.setString(2, project.getCustomer().getPhone());
		preparedStmt.setString(3, project.getCustomer().getEmail());
		preparedStmt.setString(4, project.getCustomer().getAddress());
		preparedStmt.setInt(5, project.getNumber());
		preparedStmt.execute(); // run query
		
		// Save manager to manager table
		query = " INSERT INTO Manager (name, telephone, email, address, projNumber) VALUES(?,?,?,?,?)"; // Query with placeholder for the data to DB
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, project.getManager().getName());
		preparedStmt.setString(2, project.getManager().getPhone());
		preparedStmt.setString(3, project.getManager().getEmail());
		preparedStmt.setString(4, project.getManager().getAddress());
		preparedStmt.setInt(5, project.getNumber());
		preparedStmt.execute(); // run query
		
		// Save architect to architect table
		query = " INSERT INTO Architect (name, telephone, email, address, projNumber) VALUES(?,?,?,?,?)"; // Query with placeholder for the data to DB
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, project.getArchitect().getName());
		preparedStmt.setString(2, project.getArchitect().getPhone());
		preparedStmt.setString(3, project.getArchitect().getEmail());
		preparedStmt.setString(4, project.getArchitect().getAddress());
		preparedStmt.setInt(5, project.getNumber());
		preparedStmt.execute(); // run query
		
		// Save engineer to engineer table
		query = " INSERT INTO Engineer (name, telephone, email, address, projNumber) VALUES(?,?,?,?,?)"; // Query with placeholder for the data to DB
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, project.getEngineer().getName());
		preparedStmt.setString(2, project.getEngineer().getPhone());
		preparedStmt.setString(3, project.getEngineer().getEmail());
		preparedStmt.setString(4, project.getEngineer().getAddress());
		preparedStmt.setInt(5, project.getNumber());
		preparedStmt.execute(); // run query
		
		/* UPDATE Project table in DB to insert customer, manager, architect and engineer Id */
		
		// 1) First query the Ids of customer, manager, architect and engineer for this project
		
		query = "SELECT  (SELECT Customer.customerID "  
				+ "       FROM  Customer "  
				+ "       WHERE projNumber = ? "  
				+ "      ) AS cusID,"  
				
				+ "      (SELECT Manager.managerID "  
				+ "       FROM  Manager" 
				+ "       WHERE projNumber = ? "  
				+ "      ) AS mangID," 
				
				+ "      (SELECT Architect.architectID "  
				+ "       FROM   Architect"  
				+ "       WHERE projNumber = ? "  
				+ "      ) AS archID, "
				
				+ "      (SELECT  Engineer.engineerID "  
				+ "       From Engineer "  
				+ "       WHERE projNumber = ? " 
				+ "      ) AS engID";
		
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, project.getNumber());
		preparedStmt.setInt(2, project.getNumber());
		preparedStmt.setInt(3, project.getNumber());
		preparedStmt.setInt(4, project.getNumber());
		results = preparedStmt.executeQuery(); //run query and assign result to  results variable
		
		// results is always expected to return one row because of unique Project number
		int ids[] = new int[4];
		while(results.next()) {
			ids[0] = results.getInt("cusID");
			ids[1] = results.getInt("mangID");
			ids[2] = results.getInt("archID");
			ids[3] = results.getInt("engID");
		}
		
		query = "UPDATE Project SET  customerID = ?, managerID = ? , architectID = ?, engineerID = ? WHERE  projNumber = ?";
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, ids[0]);
		preparedStmt.setInt(2, ids[1]);
		preparedStmt.setInt(3, ids[2]);
		preparedStmt.setInt(4, ids[3]);
		preparedStmt.setInt(5, project.getNumber());
		preparedStmt.execute(); // run query
		
		System.out.print("** Project details saved successufully **\n");
		
		} catch (SQLException e) {
			// Throw exception to calling function
			throw e;
		}
	}
	
	/**
	 * Method counts the number of Project in Database<br>
	 * 
	 * @return number of projects in DB
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 4
	 * 
	 */
	static int countProjectsInDB() throws SQLException {
		String countProj = "SELECT COUNT(projNumber) as countProj FROM Project"; // query to get the count of projects in DB
		results = statement.executeQuery(countProj);
		// get result of countProj query
		int count = 0;
		while(results.next()) {
			count = results.getInt("countProj");
		}
		return count;
	}

	/**
	 * Method asks user to enter project due date. The due date is entered in String
	 * format and the getDateFormat method is called that them converts the user
	 * input to LocalDate Object. <br>
	 * If input date cannot be converted something went wrong with the format; let
	 * the user now
	 * 
	 * @param project specified project
	 * @since version 1
	 * 
	 */
	 static void enterProjectDueDate(Project project) {
		while (true) {
			try {
				// Call getDateFormat function to convert user input to LocalDate and save
				// deadline to project
				System.out.print("Enter the project due date(format: day month year; eg: 09 November 2020):\n");
				String dateStr = userInput.nextLine(); // read user input
				project.setDeadline(getDateFormat(dateStr));
				break; // if it gets here all is well. Come out of the loop
			} catch (Exception e) {
				// if something went wrong ask user for date again
				System.out.print(
						"*Make sure date format is correct \nFormat: day month year \nMake use of two digits for the day eg: 01\n"
								+ "and first letter of month must be captalized eg: January*\n\n");
				continue;
			}
		}
	}

	/**
	 * Method updates some features of existing projects based on user's choice and updates 
	 * to database  namely:
	 * <br>
	 * <ul>
	 * <li>Project due date</li>
	 * <li>Amount paid to date</li>
	 * <li>Manager's details</li>
	 * </ul>
	 * <br>
	 * if there are no projects available the user will not be able to continue. If the
	 * project number is not valid, it will let the user know
	 * 
	 * @param projectList The list of projects currently available
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 1
	 * 
	 */
	static void updateProject() throws SQLException {
		System.out.print("=========================Update existing Projects=================================\n\n");

		while (true) {
			System.out.print("Note that there are currently " + countProjectsInDB() + " project(s) registered.\n"
					+ "Project numbers start from 1\n");

			if (countProjectsInDB() == 0) { // there are 0 projects do not allow user to continue
				return;
			}

			// ask user for project number that he/she wants to edit
			System.out.print("\nEnter the project number you want to update: ");
			int projIndex = 0; // index of project in the table
			try {
				String i = userInput.nextLine();
				projIndex = Integer.parseInt(i); // convert user input to integer
			} catch (Exception e) {
				System.out.print("**wrong input format!**");
				return;
			}

			// code bellow checks if project number is valid
			if (projIndex >= 1 && projIndex <= countProjectsInDB()) {
				String query;
				
				// Create a "fake" project object to be used to update actual project in DB
				Project project = new Project();
				project.setNumber(projIndex); //This "fake" project is the same number as actual project
			
				// Show user options available to update
				System.out.print("\nChoose an option from the menu bellow\n");
				System.out
						.print("\n1 - Change project due date\n" + "2 - Change total amount of the fee paid to date \n"
								+ "3 - Update Manager\'s contact details\n" + "4 - Go back to main menu\n");

				String opt = userInput.nextLine();
				switch (opt) {
				case "1":
					System.out.print("\n=========== Change project due date===========\n\n");
					enterProjectDueDate(project); // method for new due date
					
					query = "UPDATE Project SET projDeadLine = ? WHERE projNumber = ?";
					preparedStmt = connection.prepareStatement(query);
					preparedStmt.setDate(1, java.sql.Date.valueOf(project.getDeadline()));
					preparedStmt.setInt(2, project.getNumber());
					preparedStmt.execute(); // run query
					
					System.out.print("***Due date updated successfully!***\n\n");
					return; // go back to main menu

				case "2":
					System.out.print("\n==========Change total amount of the fee paid to date ===========\n\n");

					// try block bellow makes sure user enters monetary value
					while (true) {
						try {
							System.out.print("Enter total amount of the fee paid to date for project " + (projIndex) + " :");
							String amountStr = userInput.nextLine();
							double amountPaid = Double.parseDouble(amountStr);
							project.setAmountPaid(amountPaid); // save amount paid to project
							
							query = "UPDATE Project SET amountPaid = ? WHERE projNumber = ?";
							preparedStmt = connection.prepareStatement(query);
							preparedStmt.setDouble(1, project.getAmountPaid());
							preparedStmt.setInt(2, project.getNumber());
							preparedStmt.execute(); // run query
							
							System.out.print("***total amount of the fee paid to date updated successfully!***\n\n");
							break;
						} catch (Exception e) {
							System.out.println("*make sure you enter a monetary value*");
							continue;
						}
					}
					return; // go back to main menu;

				case "3":
					System.out.print("\n=============Update Manager\'s contact details======================\n\n");
					System.out.print("Manager\'s details for project " + (projIndex) + ":\n");
					insertPersonInfo(project, "manager");
					
					query = "UPDATE Manager SET name = ?, telephone = ?, email = ?, address = ? WHERE projNumber = ? ";
					preparedStmt = connection.prepareStatement(query);
					preparedStmt.setString(1, project.getManager().getName());
					preparedStmt.setString(2, project.getManager().getPhone());
					preparedStmt.setString(3, project.getManager().getEmail());
					preparedStmt.setString(4, project.getManager().getAddress());
					preparedStmt.setInt(5, project.getNumber());
					preparedStmt.execute(); // run query
					
					System.out.print("***Manager\'s details updated successfully!***\n\n");
					return;

				case "4":
					return; // go back to main menu

				default:
					System.out.print("Invalid menu Selection\n\n");
					break;
				}

			} else {
				System.out.print("You have entered and an invalid project number!\n");
				return;
			}
		}

	}

	/**
	 * Method shows pending projects from database to the user.<br>
	 * A pending project is a project that has not been completed yet.
	 * if the project is not finalized it is pending.
	 * isFinalize() returns true or false
	 * 
	 * @param projectList The list of projects currently available that will be
	 *                    traversed to check for pending projects
	 *                    
	 * @throws SQLException SQL exception if something goes wrong
	 * 
	 * @since version 3
	 * 
	 */
	static void pendingProject() throws SQLException {
		System.out.print("====================Pending Projects=========================== \n\n");

		if (countProjectsInDB() == 0) {
			System.out.print("No projects available\n");
		} else {
			// Query for projects that have not been finalized
			String query = "SELECT Customer.name, Customer.telephone, Customer.email, Customer.address,"
					+ " Project.projNumber, Project.projName, Project.buildtype, Project.address, Project.ErFNum, Project.fee, Project.amountPaid," 
					+ " Project.isComplete, Project.projDateAssigned, Project.projDeadLine, Project.dateFinalized," 
					+ " Manager.name, Manager.telephone, Manager.email, Manager.address,"  
					+ " Architect.name, Architect.telephone, Architect.email, Architect.address,"  
					+ " Engineer.name, Engineer.telephone, Engineer.email, Engineer.address " 
					+ " FROM Customer, Project, Manager, Architect, Engineer " 
					+ " WHERE (Project.customerID = Customer.customerID)"
					+ " AND (Project.managerID = Manager.managerID) " 
					+ " AND (Project.engineerID = Engineer.engineerID) " 
					+ " AND (Project.architectID = Architect.architectID)"
					+ " AND (Project.isComplete = 0)";
			
			results = statement.executeQuery(query); // run query
			
			//Display projects that have not been finalized
			boolean isFound =  false; // flag to check if pending project(s) is found
			while(results.next()) {
				isFound = true; // pending project is found
				displayProjects(); // display pending project
			}
			
			if(!isFound) { // pending project is not found
				System.out.print("There are no pending projects!\n");
			}
		}
		goBacktoMenu(); // go back to main menu	
	}


	/**
	 * Display overdue projects currently present in Database.<br>
	 * An overdue project is a project that is not finalized and its deadline is passed
	 * 
	 * @param projectList The list of projects currently available that will be
	 *                    traversed <br>
	 *                    to check overdue projects
	 *                    
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 3
	 * 
	 */
	static void overDueProject() throws SQLException {
		System.out.print("====================Overdue projects ========================= \n\n");

		if (countProjectsInDB() == 0) {
			System.out.print("No projects available\n");
		} else {
			// Display projects that are overdue
			// a project is overdue if it is not finalized and the deadline is passed
			String query = "SELECT Customer.name, Customer.telephone, Customer.email, Customer.address,"
					+ " Project.projNumber, Project.projName, Project.buildtype, Project.address, Project.ErFNum, Project.fee, Project.amountPaid," 
					+ " Project.isComplete, Project.projDateAssigned, Project.projDeadLine, Project.dateFinalized," 
					+ " Manager.name, Manager.telephone, Manager.email, Manager.address,"  
					+ " Architect.name, Architect.telephone, Architect.email, Architect.address,"  
					+ " Engineer.name, Engineer.telephone, Engineer.email, Engineer.address " 
					+ " FROM Customer, Project, Manager, Architect, Engineer " 
					+ " WHERE (Project.customerID = Customer.customerID)"
					+ " AND (Project.managerID = Manager.managerID) " 
					+ " AND (Project.engineerID = Engineer.engineerID) " 
					+ " AND (Project.architectID = Architect.architectID)"
					+ " AND (Project.isComplete = 0)"
					+ " AND  CURDATE() > (Project.projDeadLine)";
			
			results = statement.executeQuery(query); // run query
			
			//Display projects that are overdue
			boolean isFound =  false; // flag to check if overdue project(s) is found
			while(results.next()) {
				isFound = true; // overdue project is found
				displayProjects(); // display overdue project
			}
			
			if(!isFound) { // overdue project is not found
				System.out.print("There are no overdue projects!\n");
			}
		}
		goBacktoMenu(); // go back to main menu	
	}
		
	/**
	 * Method finds projects by name or number based on user input. <br>
	 * If number or name input by the user is is not found, it lets the user know.
	 * 
	 * @param projectList The list of projects currently available that will be
	 *                    traversed to find project
	 *                    
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 3
	 * 
	 */
	static void findProject() throws SQLException{
		System.out.print("====================Find projects========================= \n\n");
		
		System.out.print("Enter project number or name to find it:\n");
		String input = userInput.nextLine();
		
		String query = "SELECT Customer.name, Customer.telephone, Customer.email, Customer.address,"
				+ " Project.projNumber, Project.projName, Project.buildtype, Project.address, Project.ErFNum, Project.fee, Project.amountPaid," 
				+ " Project.isComplete, Project.projDateAssigned, Project.projDeadLine, Project.dateFinalized," 
				+ " Manager.name, Manager.telephone, Manager.email, Manager.address,"  
				+ " Architect.name, Architect.telephone, Architect.email, Architect.address,"  
				+ " Engineer.name, Engineer.telephone, Engineer.email, Engineer.address " 
				+ " FROM Customer, Project, Manager, Architect, Engineer " 
				+ " WHERE (Project.customerID = Customer.customerID)"
				+ " AND (Project.managerID = Manager.managerID) " 
				+ " AND (Project.engineerID = Engineer.engineerID) " 
				+ " AND (Project.architectID = Architect.architectID)"
				+ " AND (Project.projName like ? OR Project.projNumber like ?)";
		
		preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, "%" + input + "%");
		preparedStmt.setString(2, "%" + input + "%");
	    results = preparedStmt.executeQuery(); //  run query
	    
	    if (!input.isEmpty()) { // if input by user is not empty	
	    	boolean hasSearch = false; // flag to determine if search was found anything
	    	
	    	while(results.next()) {
	    		hasSearch = true; // search  found something
	    		displayProjects();
	    	}
	    	
	    	if(!hasSearch) { // if no search was found
	    		System.out.print("No results were found for your search\n");
	    	}
	    	
	    } else { // input by user was empty
	    	System.out.print("Empty search\n");	
	    }

		goBacktoMenu(); // go back to main menu
}

	/**
	 * Method calls finalizeProject() to finalize a given project.<br> 
	 * It asks for the project number the user wants to finalize and finalizes it.
	 * Once the project has been finalized it saves changes to database
	 * 
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 1
	 * 
	 */
	static void finalizeMenu() throws SQLException {
		System.out.print("========================Finalize project==========================\n\n");
		System.out.print("Note that there are currently " +  countProjectsInDB() + " project(s) registered.\n"
				+ "Project numbers start from 1\n");

		if (countProjectsInDB() == 0) { // there are 0 projects do not allow user to continue
			return;
		}

		// ask user for project number that he/she wants to finalize
		System.out.print("\nEnter the project number you want to finalize: ");
		int projIndex = 0;
		try {
			String i = userInput.nextLine();
			projIndex = Integer.parseInt(i); // convert user input to integer
		} catch (Exception e) {
			System.out.print("**wrong input format!**");
			return;
		}
		
		// code bellow checks if project number is valid
		if (projIndex >= 1 && projIndex <= countProjectsInDB()) {
			
			/* Get customer from the DB based on Project number */
			String query = "SELECT * " 
					+ " FROM Customer "  
					+ " Where Customer.projNumber = ?";
			
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, projIndex);
			results = preparedStmt.executeQuery(); // run query
			
			// store result of query above in variables bellow
			String name = " ";
    		String email = " ";
    		String telephone = " ";
    		String address = " ";
			while(results.next()) {
				name = results.getString("Customer.name");
	    		email = results.getString("Customer.email");
	    		telephone = results.getString("Customer.telephone");
	    		address = results.getString("Customer.address");
			}
			
			/* Get fee of  project and amount paid by customer from the DB */
			String queryFeeAndPaid = "SELECT Project.fee, Project.amountPaid "  
					+ " FROM Project "  
					+ " Where Project.projNumber = ? ";
			
			preparedStmt = connection.prepareStatement(queryFeeAndPaid);
			preparedStmt.setInt(1, projIndex);
			results = preparedStmt.executeQuery(); // run query	
			
			double fee = 0.0, amountPaid = 0.0;
			while (results.next()) {
				fee = results.getDouble("Project.fee");
				amountPaid =  results.getDouble("Project.amountPaid");
			}
			
			// Create a "mirror" project  to be used to finalized actual project in DB
			// This project object stores project data from the DB
			Project project = new Project();
			project.setNumber(projIndex); 
			project.setCustomer(name, telephone, email, address, "customer"); // assign customer details
			project.setFee(fee); // fee of Project
			project.setAmountPaid(amountPaid); // paid amount of project
			
			project.finalizeProject(project); // finalize project

		} else {
			System.out.print("Make sure you enter a valid project number!\n");
		}

	}
	
	/**
	 * Method connects to DataBase
	 *@throws SQLException SQL exception if something goes wrong
	 *
	 */
	 static void connectDB()  throws SQLException {
		 connection = DriverManager.getConnection(
				 "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
				 "otheruser" ,
				 "swordfish"
				 );
	 }
	
	/**
	 * This method gets projects from Database.<br>
	 * Projects are read from DB and shown to the user
	 * 
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 4
	 * 
	 */
	static void getExistingProjects() throws SQLException  { 
		results = statement.executeQuery ("SELECT Customer.name, Customer.telephone, Customer.email, Customer.address,"
				+ " Project.projNumber, Project.projName, Project.buildtype, Project.address, Project.ErFNum, Project.fee, Project.amountPaid," 
				+ " Project.isComplete, Project.projDateAssigned, Project.projDeadLine, Project.dateFinalized," 
				+ " Manager.name, Manager.telephone, Manager.email, Manager.address,"  
				+ " Architect.name, Architect.telephone, Architect.email, Architect.address,"  
				+ " Engineer.name, Engineer.telephone, Engineer.email, Engineer.address " 
				+ " FROM Customer, Project, Manager, Architect, Engineer " 
				+ " WHERE (Project.customerID = Customer.customerID)"
				+ " AND (Project.managerID = Manager.managerID) " 
				+ " AND (Project.engineerID = Engineer.engineerID) " 
				+ " AND (Project.architectID = Architect.architectID)");
	 	
		// Display existing projects
		boolean isFound = false; // flag to check if there are any project(s) in DB
    	while(results.next()) { 
    		isFound = true; // a project is found
    		displayProjects(); // Display  project
    	}
    	
    	if(!isFound) { // no project is  found
			System.out.print("There are projects in database!\n");
		}
    	
    	goBacktoMenu();
    }
	
	/**
	 *  This method displays project details to the user from the database.
	 * Project details are shown in a comprehensive manner
	 * 
	 * @throws SQLException SQL exception if something goes wrong
	 * @since version 3
	 * 
	 */
	static void displayProjects() throws SQLException {
		String content = " "; // this variable will store all information pertaining to the project
		
		content = "**************Project number: " + results.getInt("Project.projNumber") + " ************************\n\n";
		
		// display YES OR NO based on isCompletet value in DB
		if(!results.getBoolean("Project.isComplete")) {
			content += "Is project finalized ?:  NO \n";
			
		} else if (results.getBoolean("Project.isComplete")) {
			
			content += "Is project finalized ?:  YES \n";
		} 
		content += "Project name: " + results.getString("Project.projName") + "\n";
		content += "Type of building: " + results.getString("Project.buildtype") + "\n";
		content += "The physical address for the project: " + results.getString("Project.address")  + "\n";
		content += "ERF number: " + results.getString("Project.ErFNum" )+ "\n";
		content += "The total fee charged for the project: " + results.getDouble("Project.fee") + "\n";
		content += "The total amount paid to date: " + results.getDouble("Project.amountPaid") + "\n";
		content += "Project date assigned: " + results.getDate("Project.projDateAssigned") + "\n";
		content += "Project deadline: " + results.getDate("Project.projDeadLine") + "\n";
		content += "Project date finalized: " + results.getDate("Project.dateFinalized") + "\n";
		content += "\n";
		content += "Customer\'s details:\n";
		content += "Name: " + results.getString("Customer.name") + "\n";
		content += "Email: " + results.getString("Customer.email") + "\n";
		content += "Telephone: " + results.getString("Customer.telephone") + "\n";
		content += "address: " + results.getString("Customer.address")+ "\n";
		content += "\n";
		
		content += "Manager\'s details:\n";
		content += "Name: " + results.getString("Manager.name") + "\n";
		content += "Email: " + results.getString("Manager.email") + "\n";
		content += "Telephone: " + results.getString("Manager.telephone") + "\n";
		content += "address: " + results.getString("Manager.address")+ "\n";
		content += "\n";
		
		content += "Architect\'s details:\n";
		content += "Name: " + results.getString("Architect.name") + "\n";
		content += "Email: " + results.getString("Architect.email") + "\n";
		content += "Telephone: " + results.getString("Architect.telephone") + "\n";
		content += "address: " + results.getString("Architect.address")+ "\n";
		content += "\n";
		
		content += "Engineer\'s details:\n";
		content += "Name: " + results.getString("Engineer.name") + "\n";
		content += "Email: " + results.getString("Engineer.email") + "\n";
		content += "Telephone: " + results.getString("Engineer.telephone") + "\n";
		content += "address: " + results.getString("Engineer.address")+ "\n";
		content += "\n";
  	
		System.out.print(content); // display projects
	}

	/**
	 * This method allows user to return to main menu by pressing the <b>enter</b> key
	 * 
	 * @since version 3
	 * 
	 */
	static void goBacktoMenu() {
		System.out.print("\n*** Press enter to go back to main menu ***\n");
		try {
			System.in.read();
		} catch (Exception e) {
			
		}
	}
}