
public class sqlClass {
	int class_id;
	String name;
	String description;
	String code;
	int maximum_students;
	
	public sqlClass(int id, String n, String d, String c, int ms) {
		class_id = id;
		description = d;
		name = n;
		code = c;
		maximum_students = ms;
	}
	
	public String toString() {
		String str = class_id + " | " + code + " | " + name + " | " + description;
		return str;
	}
}
