

public class Student {
	
	int studentId;
	String firstName;
	String lastName;
	String birthdate;
	
	public Student (int id, String first, String last, String date) {
		studentId = id;
		firstName = first;
		lastName = last;
		birthdate = date;
	}
	
	public String toString() {
		String str = studentId + " | " + firstName + " | " + lastName + " | " + birthdate;
		
		return str;
	}

}
