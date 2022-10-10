import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This application will keep track of things like what classes are offered by
 * the school, and which students are registered for those classes and provide
 * basic reporting. This application interacts with a database to store and
 * retrieve data.
 */
public class SchoolManagementSystem {

    public static void getAllClassesByInstructor(String first_name, String last_name) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = String.format("select instructors.first_name, instructors.last_name, academic_titles.title, \r\n"
            		+ "classes.code, classes.name as class_name, terms.name as term\r\n"
            		+ "from instructors\r\n"
            		+ "left join academic_titles on instructors.academic_title_id = academic_titles.academic_title_id\r\n"
            		+ "left join class_sections on class_sections.instructor_id = instructors.instructor_id\r\n"
            		+ "left join classes on classes.class_id = class_sections.class_id\r\n"
            		+ "left join terms on terms.term_id = class_sections.term_id\r\n"
            		+ "WHERE first_name = '%s' AND last_name = '%s'", first_name, last_name);
            ResultSet resultSet = sqlStatement.executeQuery(sql);
            
            List<Instructor> instructors = new ArrayList<Instructor>();
            
            while(resultSet.next()) {
            	String first = resultSet.getString(1);
            	String last = resultSet.getString(2);
            	String title = resultSet.getString(3);
            	String code = resultSet.getString(4);
            	String name = resultSet.getString(5);
            	String term = resultSet.getString(6);
            	
            	Instructor inst = new Instructor(first, last, title, code, name, term);
            	instructors.add(inst);
            }
            resultSet.close();
            connection.close();
            
            System.out.println("First Name | Last Name | Title | Code | Name | Term");
            System.out.println("--------------------------------------------------------------------------------");
            
            for(Instructor inst : instructors) {
            	System.out.println(inst.toString());
            }
            
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void submitGrade(String studentId, String classSectionID, String grade) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String intGrade;
            if(grade.equals("A")) {
            	intGrade = "1";
            } else if (grade.equals("B")) {
            	intGrade = "2";
            } else if (grade.equals("C")) {
            	intGrade = "3";
            } else if (grade.equals("D")) {
            	intGrade = "4";
            } else {
            	intGrade = "5";
            }
            
            String sql = String.format("UPDATE class_registrations SET grade_id = '%s' WHERE student_id = '%s' AND class_section_id = '%s';", intGrade, studentId, classSectionID);
            
            sqlStatement.executeUpdate(sql);
            connection.close();
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Grade has been submitted!");
        } catch (SQLException sqlException) {
            System.out.println("Failed to submit grade");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void registerStudent(String studentId, String classSectionID) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = String.format("INSERT INTO class_registrations (student_id, class_section_id) VALUES ('%s', '%s');", studentId, classSectionID);
            
            sqlStatement.executeUpdate(sql);
            
            String sql1 = String.format("SELECT * FROM class_registrations WHERE class_section_id = '%s' AND student_id = '%s'", classSectionID, studentId);
            ResultSet resultSet = sqlStatement.executeQuery(sql1);
            
            resultSet.next();
            int registrationId = resultSet.getInt(1);
            int student_id = resultSet.getInt(3);
            int class_section_id = resultSet.getInt(2);
            connection.close();
            resultSet.close();
            
            System.out.println("Class Registration ID | Student ID | Class Section ID");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(registrationId + " | " + student_id + " | " + class_section_id);
            
        } catch (SQLException sqlException) {
            System.out.println("Failed to register student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void deleteStudent(String studentId) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = String.format("DELETE FROM students WHERE student_id = %s;", studentId);
            
            sqlStatement.executeUpdate(sql);
            connection.close();
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Student with id: "+ studentId + " was deleted");
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void createNewStudent(String firstName, String lastName, String birthdate) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = String.format("INSERT INTO students (first_name, last_name, birthdate) VALUES ('%s', '%s', '%s');", firstName, lastName, birthdate);
            
            sqlStatement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            ResultSet resultSet = sqlStatement.getGeneratedKeys();
            resultSet.next();
            int student_id = resultSet.getInt(1);
            connection.close();
            
            System.out.println("Student ID | First Name | Last Name | Birthdate");
            System.out.println("--------------------------------------------------------------------------------");
            
            Student newStudent = new Student(student_id, firstName, lastName, birthdate);
            
            System.out.println(newStudent.toString());
            
        } catch (SQLException sqlException) {
            System.out.println("Failed to create student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void listAllClassRegistrations() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = "select class_registrations.student_id, class_registrations.class_section_id, students.first_name, \r\n"
            		+ "students.last_name, classes.code, classes.name, terms.name as term,\r\n"
            		+ "grades.letter_grade as letter_grade\r\n"
            		+ "from class_registrations\r\n"
            		+ "left join students on students.student_id = class_registrations.student_id\r\n"
            		+ "left join class_sections on class_sections.class_section_id = class_registrations.class_section_id\r\n"
            		+ "left join classes on classes.class_id = class_sections.class_id\r\n"
            		+ "left join terms on terms.term_id = class_sections.term_id\r\n"
            		+ "left join grades on class_registrations.grade_id = grades.grade_id\r\n"
            		+ "order by grades.letter_grade";
            ResultSet resultSet = sqlStatement.executeQuery(sql);
            
            List<ClassRegistration> registration = new ArrayList<ClassRegistration>();
            while(resultSet.next()) {
            	int studentId = resultSet.getInt(1);
            	int classId = resultSet.getInt(2);
            	String first = resultSet.getString(3);
            	String last = resultSet.getString(4);
            	String code = resultSet.getString(5);
            	String name = resultSet.getString(6);
            	String term = resultSet.getString(7);
            	String grade = resultSet.getString(8);
            	
            	ClassRegistration classReg = new ClassRegistration(studentId, classId, first, last, code, name, term, grade);
            	registration.add(classReg);
            }
            
            resultSet.close();
            connection.close();
            
            System.out.println("Student ID | class_section_id | First Name | Last Name | Code | Name | Term | Letter Grade");
            System.out.println("--------------------------------------------------------------------------------");
            
            for(ClassRegistration reg : registration) {
            	System.out.println(reg.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClassSections() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = "select class_sections.class_section_id, classes.code, classes.name, terms.name as term\r\n"
            		+ "from class_sections\r\n"
            		+ "left join classes on classes.class_id = class_sections.class_id\r\n"
            		+ "left join terms on terms.term_id = class_sections.term_id\r\n"
            		+ "order by terms.name";
            ResultSet resultSet = sqlStatement.executeQuery(sql);
            
            List<ClassSection> sections = new ArrayList<ClassSection>();
            while(resultSet.next()) {
            	int id = resultSet.getInt(1);
            	String code = resultSet.getString(2);
            	String name = resultSet.getString(3);
            	String term = resultSet.getString(4);
            	
            	ClassSection sec = new ClassSection(id, code, name, term);
            	sections.add(sec);
            }
            
            resultSet.close();
            connection.close();
            
            System.out.println("Class Section ID | Code | Name | term");
            System.out.println("--------------------------------------------------------------------------------");
            
            for(ClassSection sec : sections) {
            	System.out.println(sec.toString());
            }
            
            
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClasses() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = "SELECT * FROM classes";
            ResultSet resultSet = sqlStatement.executeQuery(sql);
            
            List<sqlClass> classes = new ArrayList<sqlClass>();
            while(resultSet.next()) {
            	int id = resultSet.getInt(1);
            	String name = resultSet.getString(2);
            	String description = resultSet.getString(3);
            	String code = resultSet.getString(4);
            	int max = resultSet.getInt(5);
            	
            	sqlClass sqlClass = new sqlClass(id, name, description, code, max);
            	classes.add(sqlClass);
            }
            
            resultSet.close();
            connection.close();
            
            System.out.println("Class ID | Code | Name | Description");
            System.out.println("--------------------------------------------------------------------------------");
            
            for(sqlClass sClass : classes) {
            	System.out.println(sClass.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void listAllStudents() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            
            String sql = "SELECT * FROM students";
            ResultSet resultSet = sqlStatement.executeQuery(sql);
            
            List<Student> students = new ArrayList<Student>();
            while(resultSet.next()) {
            	int id = resultSet.getInt(1);
            	String first = resultSet.getString(2);
            	String last = resultSet.getString(3);
            	String birth = resultSet.getString(4);
            	
            	Student student = new Student(id, first, last, birth);
            	students.add(student);
            }
            
            resultSet.close();
            connection.close();
            
            System.out.println("Student ID | First Name | Last Name | Birthdate");
            System.out.println("--------------------------------------------------------------------------------");
            
            for(Student stud : students) {
            	System.out.println(stud.toString());
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /***
     * Splits a string up by spaces. Spaces are ignored when wrapped in quotes.
     *
     * @param command - School Management System cli command
     * @return splits a string by spaces.
     */
    public static List<String> parseArguments(String command) {
        List<String> commandArguments = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) commandArguments.add(m.group(1).replace("\"", ""));
        return commandArguments;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the School Management System");
        System.out.println("-".repeat(80));

        Scanner scan = new Scanner(System.in);
        String command = "";

        do {
            System.out.print("Command: ");
            command = scan.nextLine();
            ;
            List<String> commandArguments = parseArguments(command);
            command = commandArguments.get(0);
            commandArguments.remove(0);

            if (command.equals("help")) {
                System.out.println("-".repeat(38) + "Help" + "-".repeat(38));
                System.out.println("test connection \n\tTests the database connection");

                System.out.println("list students \n\tlists all the students");
                System.out.println("list classes \n\tlists all the classes");
                System.out.println("list class_sections \n\tlists all the class_sections");
                System.out.println("list class_registrations \n\tlists all the class_registrations");
                System.out.println("list instructor <first_name> <last_name>\n\tlists all the classes taught by that instructor");


                System.out.println("delete student <studentId> \n\tdeletes the student");
                System.out.println("create student <first_name> <last_name> <birthdate> \n\tcreates a student");
                System.out.println("register student <student_id> <class_section_id>\n\tregisters the student to the class section");

                System.out.println("submit grade <studentId> <class_section_id> <letter_grade> \n\tcreates a student");
                System.out.println("help \n\tlists help information");
                System.out.println("quit \n\tExits the program");
            } else if (command.equals("test") && commandArguments.get(0).equals("connection")) {
                Database.testConnection();
            } else if (command.equals("list")) {
                if (commandArguments.get(0).equals("students")) listAllStudents();
                if (commandArguments.get(0).equals("classes")) listAllClasses();
                if (commandArguments.get(0).equals("class_sections")) listAllClassSections();
                if (commandArguments.get(0).equals("class_registrations")) listAllClassRegistrations();

                if (commandArguments.get(0).equals("instructor")) {
                    getAllClassesByInstructor(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("create")) {
                if (commandArguments.get(0).equals("student")) {
                    createNewStudent(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("register")) {
                if (commandArguments.get(0).equals("student")) {
                    registerStudent(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("submit")) {
                if (commandArguments.get(0).equals("grade")) {
                    submitGrade(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("delete")) {
                if (commandArguments.get(0).equals("student")) {
                    deleteStudent(commandArguments.get(1));
                }
            } else if (!(command.equals("quit") || command.equals("exit"))) {
                System.out.println(command);
                System.out.println("Command not found. Enter 'help' for list of commands");
            }
            System.out.println("-".repeat(80));
        } while (!(command.equals("quit") || command.equals("exit")));
        System.out.println("Bye!");

        scan.close();
    }
}

