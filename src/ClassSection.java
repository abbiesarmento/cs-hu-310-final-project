
public class ClassSection {

	int id;
	String code;
	String name;
	String term;
	
	public ClassSection( int i, String c, String n, String t) {
		id = i;
		code = c;
		name = n;
		term = t;
	}
	
	public String toString() {
		String str = id + " | " + code + " | " + name + " | " + term;
		return str;
	}
	
}
