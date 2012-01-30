// Jacob Gofman
// Professor Ernest Davis
// Data Structures, Honors Section
// Programming Assignment 4

import java.util.Hashtable;


public class Variable {

	private String name;
	private double value;
	public static Hashtable<String, Variable> SymbolTable = new Hashtable<String, Variable>();
	
	public Variable(String str, double num)
	{
		name = str;
		value = num;
		SymbolTable.put(name, this);
	}
	
	public void setName(String str) { name = str; }
	
	public String getName() { return name; }

	public void setValue(double num) { value = num; }
	
	public double getValue() { return value; }
}
