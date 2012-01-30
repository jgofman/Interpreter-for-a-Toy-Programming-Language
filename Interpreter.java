// Jacob Gofman
// Professor Ernest Davis
// Data Structures, Honors Section
// Programming Assignment 4

import java.util.Scanner;


public class Interpreter {

	static boolean defining = false;
	
	public static void main(String[] args)
	{
		boolean notDone = true;
		String funcName = "";
		Scanner value = new Scanner(System.in);
		System.out.println("Welcome to Interpreter!");
		
		// Nav Menu Start
		while (notDone)
		{
			// Directions of possible commands
			System.out.println("\nAssign <variable name> <expression> - Assign a prefix-formatted expression to variable");
			System.out.println("Define <function name> - Define a function name (beginning of the function definition)");
			System.out.println("End <function name> - Ends the function definition initiated by Define <function> command");
			System.out.println("Call <function name> - Calls pre-defined function by name");
			System.out.println("For <expression> <statement> - Initiates 'for' loop on the 'statement' for an 'expression' # of times ");
			System.out.println("Quit - Terminate the program.");
			System.out.println("\nINPUT: ");
			String input = value.nextLine();
			
			// Split input into words and capitalize them
			String[] commands = input.split("\\s+");
			
			if (commands[0].equalsIgnoreCase("assign") && (commands.length >= 3)) { // Assign
				Statement stmt1 = new Statement(input, commands);
				if (!defining) { // if not defining, evaluate statement
					String result = stmt1.evaluatePrefix();
					if (result.equalsIgnoreCase("Invalid")) {
						System.out.println("\nInvalid operation. Variable not found.");
					}
				} else { // add to function
					Function.FunctionTable.get(funcName).addStmt(stmt1);
				}
			} else if (commands[0].equalsIgnoreCase("define") && (commands.length == 2)) { // Define
				if (!defining) { // if not defining, then define if statement is deemed correct
					if (!checkExist(commands[1])) {
						System.out.println("Defining " + commands[1]);
						define(commands[1]);
						funcName = commands[1];
					} else { 
						System.out.println("\nInvalid operation. Function name missing.");
					}
				} else { // if already defining, return invalid
					System.out.println("\nInvalid operation. You cannot define a function within a function.");
				}
			} else if (commands[0].equalsIgnoreCase("end") && (commands.length == 2)) { // End
				if (!defining) { // if not defining
					System.out.println("\nInvalid operation. There is no function being defined.");
				} else { // if defining, then end the function definition
					if (checkExist(commands[1])) {
						System.out.println("Ending " + commands[1]);
						end();
					} else {
						System.out.println("\nInvalid operation. Function name missing.");
					}
				}
			} else if (commands[0].equalsIgnoreCase("call") && (commands.length == 2)) { // Call
				Statement stmt1 = new Statement(input, commands);
				if (!defining) { // if not defining, call function
					if (checkExist(commands[1])) {
						stmt1.call(commands[1]);
					} else {
						System.out.println("\nFunction with this name does not exist or is missing.");
					}
				} else { // if defining, add to function definition (statement array)
					Function.FunctionTable.get(funcName).addStmt(stmt1);
				}
			} else if (commands[0].equalsIgnoreCase("for") && (commands.length >= 4)) {
				Statement stmt1 = new Statement(input, commands);
				if (!defining) { // if not defining evaluate for loop
					stmt1.forLoop(stmt1);
				} else { // if defining add statement to function definition (statement array)
					Function.FunctionTable.get(funcName).addStmt(stmt1);
				}
			} else if (commands[0].equalsIgnoreCase("quit") && (commands.length == 1)) { // Quit
				System.out.println("\nThank you for using Interpreter. Have a nice day!");
				System.exit(0);
			} else { // Unrecognized input
				System.out.println("\nInput not recognized, try again: ");
			}
		}
		// Nav Menu End
	}
		
	// Checks if function already exists in FunctionTable
	public static boolean checkExist(String name) { return Function.FunctionTable.containsKey(name); }
	
	public static void define(String fName) // enable define function
	{
		defining = true;
		Function f = new Function(fName);
	}
	
	public static void end() // end define function
	{
		if (defining == true) defining = false;
	}	
}
