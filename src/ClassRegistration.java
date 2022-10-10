
public class ClassRegistration {

	int studentId;
	int classId;
	String first;
	String last;
	String code;
	String name;
	String term;
	String grade;
	
	public ClassRegistration(int sId, int cId, String f, String l, String c, String n, String t, String g) {
		studentId = sId;
		classId = cId;
		first = f;
		last = l;
		code = c;
		name = n;
		term = t;
		grade = g;
	}
	
	
	public String toString() {
		String str = studentId + " | " + classId + " | " + first + " | " + last + " | " + code + " | " + name + " | " + term + " | " + grade;
		return str;
	}
	
	
}
