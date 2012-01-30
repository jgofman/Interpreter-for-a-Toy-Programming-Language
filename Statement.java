// Jacob Gofman
// Professor Ernest Davis
// Data Structures, Honors Section
// Programming Assignment 4

import java.util.ArrayList;
import java.util.Stack;


public class Statement {
	
	private String stringValue;
	private String[] cmds;
	private static boolean printOutput = true;
	
	public Statement(String str, String[] c)
	{
		this.stringValue = str;
		this.cmds = c; 
	}
	
	public void call(String fName) // Call function
	{
		Function f = Function.FunctionTable.get(fName);
		ArrayList<Statement> fStmtArray = f.getStmt();

		System.out.println("Calling " + fName);
		
		for (int i = 0; i < fStmtArray.size(); i++) // evaluate all statements in function
		{
			String result = fStmtArray.get(i).evaluatePrefix();
			if (result.equalsIgnoreCase("Invalid")) {
				System.out.println("\nInvalid operation. Variable not found.");
			}
		}
	}
	
	public void forLoop(Statement stmt) // Evaluate for loop
	{
		double numTimes;
		Statement newStmt, exprStmt;
		String[] newStmtCmds, exprCmds; // assign/call statement, for expression
		String newStmtString = "", exprString = "assign myNumTimes ";
		int count = 0, indexMid = 0;
		
		for (int i = 0; i < stmt.cmds.length; i++) // Make sure there is only one statement; i.e. 1 assign or 1 call
		{
			if (count < 2) {
				if (stmt.cmds[i].equalsIgnoreCase("assign")) {
					count++;
					indexMid = i; // Record index of statement beginning
				} else if (stmt.cmds[i].equalsIgnoreCase("call")) {
					count++;
					indexMid = i;
				}
			} else {
				System.out.println("\nInvalid statement.");
				break;
			}
		}
		
		exprCmds = new String[indexMid+1];
		newStmtCmds = new String[stmt.cmds.length-indexMid];
		
		if (count < 2) {
			for (int i = 0; i < (indexMid); i++) // Create expression statement
			{
				exprCmds[i+1] = (stmt.cmds[i]);
				if (i != 0) exprString += (stmt.cmds[i] + " ");
			}
			
			exprCmds[0] = "assign";
			exprCmds[1] = "myNumTimes";
			
			exprStmt = new Statement(exprString, exprCmds);
			
			for (int i = indexMid; i < stmt.cmds.length; i++) // Create assign/call statement to be evaluated
			{
				newStmtCmds[i-indexMid] = (stmt.cmds[i]);
				newStmtString += (stmt.cmds[i] + " ");
			}
			newStmt = new Statement(newStmtString, newStmtCmds);
			Variable myNumTimes = new Variable("myNumTimes", 0);
			printOutput = false;
			exprStmt.evaluatePrefix();
			printOutput = true;
			numTimes = myNumTimes.getValue(); // Evaluate expression statement
			
			for (int i = 0; i < numTimes; i++)
			{
				newStmt.evaluatePrefix(); // Evaluate assign/call statement based on the result of the expression statement
			}
			
			System.out.println("\nEvaluated For Loop");			
		}
	}
	
	public String evaluatePrefix() // Evaluate and read prefix expression
	{
		Stack<String> operands = new Stack<String>(); 
		Stack<String> operators = new Stack<String>();
		double holdValue = 0.0, readValue = 0.0, tempResult = 0.0;
		boolean performAgain = false;
		double result = 0.0;
		
		for (int i = 2; i < cmds.length; i++) // Read through all characters
		{
			if (cmds[i] != null)
			{
				if (i == 2) { // Index representing the next element after the variable whose value we are calculating
					if (cmds[i].matches("^[0-9]+[.]*[0-9]*$")) { // If number
						result = Double.parseDouble(cmds[i]);
						break;
					} else if (cmds[i].matches("^[A-Za-z]*$")) { // If variable
						if(Variable.SymbolTable.containsKey(cmds[i])) // Check if variable exists
						{
							result = Variable.SymbolTable.get(cmds[i]).getValue();
							break;
						} else {
							return "Invalid"; // Variable doesn't exist
						}
					} else  if (cmds[i].equals("+") || cmds[i].equals("-") || cmds[i].equals("*") || cmds[i].equals("/")) {
						operators.push(cmds[i]);
						operands.push("___");
					}
				} else if (cmds[i].equals("+") || cmds[i].equals("-") || cmds[i].equals("*") || cmds[i].equals("/")) { // If it's an operator
					operators.push(cmds[i]);
					operands.push("___");
				} else { // If it's an operand
					if (operands.peek().equals("___")) // If top of stack is the marker
					{
						operands.push(cmds[i]);
					} else { // If top of stack is variable or number
						String hold = "", sign = "";
	
						while (!operators.isEmpty())
						{
							if (!operands.peek().equals("___"))
							{
								hold = operands.pop(); // Grab operand
								operands.pop(); // Discard marker
								sign = operators.pop(); // Grab operator relating to marker
							}
							
							if (Variable.SymbolTable.containsKey(hold)) //if hold is a variable
							{
								holdValue = Variable.SymbolTable.get(hold).getValue();
							} else if (hold.matches("^[0-9]+[.]*[0-9]*$")) { // if hold is a number
								holdValue = Double.parseDouble(hold);
							} else { // invalid
								return "Invalid";
							}
							
							if (performAgain == false) // if not looping again to evaluate sub statement
							{							
								if (Variable.SymbolTable.containsKey(cmds[i])) // if variable
								{
									readValue = Variable.SymbolTable.get(cmds[i]).getValue();
								} else if (cmds[i].matches("^[0-9]+[.]*[0-9]*$")) { // if number
									readValue = Double.parseDouble(cmds[i]);
								} else { // invalid
									return "Invalid";
								}
							}
							
							switch (sign.charAt(0)) // Perform calculation based on operator
							{
							case '+': tempResult = holdValue + readValue; break;
							case '-': tempResult = holdValue - readValue; break;
							case '*': tempResult = holdValue * readValue; break;
							case '/': tempResult = holdValue / readValue; break;
							default: break;
							}
							
							if (!operands.isEmpty()) // if operands exist
							{
								if (operands.peek().equals("___")) { // if marker
									operands.push("" + tempResult);
									performAgain = false;
									break;
								} else { //run again
									readValue = tempResult;
									performAgain = true;
								}
							} else {
								if (i == cmds.length - 1)
								{
									result = tempResult;
								}
							}
						}
						
						if ((operators.isEmpty()) && (operands.size() == 1))
						{
							result = tempResult;
						}
					}
				}
			}
			
		}
		
		if (!Variable.SymbolTable.containsKey(cmds[1]))
		{
			Variable newVar = new Variable(cmds[1],result);
		} else {
			Variable.SymbolTable.get(cmds[1]).setValue(result);
		}
		
		if (printOutput) System.out.println("Assigning " + result + " to " + cmds[1]);
		
		return "";
	}
}
