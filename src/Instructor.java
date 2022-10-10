
public class Instructor {

	String firstName;
	String lastName;
	String title;
	String code;
	String className;
	String term;
	
	public Instructor (String first, String last, String t, String c, String name, String tm) {
		firstName = first;
		lastName = last;
		title = t;
		code = c;
		className = name;
		term = tm;
	}
	
	public String toString() {
		String str = firstName + " | " + lastName + " | " + title + " | " + code + " | " + className + " | " + term; 
		return str;
	}
	
}
