// Jacob Gofman
// Professor Ernest Davis
// Data Structures, Honors Section
// Programming Assignment 4

import java.util.ArrayList;
import java.util.Hashtable;


public class Function {

	private String name;
	private ArrayList<Statement> stmtArray;
	public static Hashtable<String, Function> FunctionTable = new Hashtable<String, Function>();
	
	public Function(String str)
	{
		name = str;
		stmtArray = new ArrayList<Statement>();
		FunctionTable.put(str, this);
	}

	public Function(Statement stmt)
	{
		stmtArray.add(stmt);		
	}
	
	public void setName(String str) { this.name = str; }
	
	public String getName() { return this.name; }

	public void addStmt(Statement stmt) { this.stmtArray.add(stmt); } // Add assign/call statements to function definition
	
	public ArrayList<Statement> getStmt() { return this.stmtArray; }

}
