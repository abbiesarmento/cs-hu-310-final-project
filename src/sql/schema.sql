/*
  Put all table create statements in this file as well as any UDFs or stored
  procedures.

  NOTE: This file must be able to run repetitively without any errors
*/

CREATE DATABASE IF NOT EXISTS cs_hu_310_final_project; 
USE cs_hu_310_final_project; 
DROP TABLE IF EXISTS class_registrations; 
DROP TABLE IF EXISTS grades; 
DROP TABLE IF EXISTS class_sections; 
DROP TABLE IF EXISTS instructors; 
DROP TABLE IF EXISTS academic_titles; 
DROP TABLE IF EXISTS students; 
DROP TABLE IF EXISTS classes;
DROP TABLE IF EXISTS terms;
DROP FUNCTION IF EXISTS convert_to_grade_point; 
 
CREATE TABLE IF NOT EXISTS classes( 
    class_id INT AUTO_INCREMENT, 
    name VARCHAR(50) NOT NULL, 
    description VARCHAR(1000), 
    code VARCHAR(10) UNIQUE, 
    maximum_students INT DEFAULT 10, 
    PRIMARY KEY(class_id) 
); 
 
CREATE TABLE IF NOT EXISTS students( 
    student_id INT AUTO_INCREMENT, 
    first_name VARCHAR(30) NOT NULL, 
    last_name VARCHAR(50) NOT NULL, 
    birthdate DATE, 
    PRIMARY KEY (student_id) 
); 

CREATE TABLE IF NOT EXISTS academic_titles(
    academic_title_id INT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY(academic_title_id)
);

CREATE TABLE IF NOT EXISTS instructors(
    instructor_id INT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR (80) NOT NULL,
    last_name VARCHAR (80) NOT NULL,
    academic_title_id INT, 
    FOREIGN KEY (academic_title_id) REFERENCES academic_titles(academic_title_id),
    PRIMARY KEY (instructor_id)
);

CREATE TABLE IF NOT EXISTS terms(
    term_id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(80) NOT NULL,
    PRIMARY KEY (term_id)
);

CREATE TABLE IF NOT EXISTS class_sections(
    class_section_id INT AUTO_INCREMENT NOT NULL,
    class_id INT NOT NULL,
    FOREIGN KEY (class_id) REFERENCES classes(class_id),
    instructor_id INT NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id),
    term_id INT NOT NULL,
    FOREIGN KEY (term_id) REFERENCES terms(term_id),
    PRIMARY KEY (class_section_id)
);

CREATE TABLE IF NOT EXISTS grades(
    grade_id INT AUTO_INCREMENT NOT NULL,
    letter_grade CHAR(2) NOT NULL,
    PRIMARY KEY (grade_id)
);

CREATE TABLE IF NOT EXISTS class_registrations(
    class_registration_id INT AUTO_INCREMENT NOT NULL,
    class_section_id INT NOT NULL,
    FOREIGN KEY (class_section_id) REFERENCES class_sections(class_section_id),
    student_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    grade_id INT,
    FOREIGN KEY (grade_id) REFERENCES grades(grade_id),
    signup_timestamp DATETIME DEFAULT current_timestamp,
    PRIMARY KEY(class_registration_id),
    UNIQUE KEY(class_section_id, student_id)
);

DELIMITER $$ 
CREATE FUNCTION convert_to_grade_point(letter_grade char(2)) 
   RETURNS INT 
   DETERMINISTIC 
BEGIN 
    IF letter_grade = 'A' THEN
       return 4;
	ELSEIF letter_grade = 'B' THEN
       return 3;
	ELSEIF letter_grade = 'C' THEN
       return 2;
	ELSEIF letter_grade = 'D' THEN
       return 1;
	ELSEIF letter_grade = 'F' THEN
       return 0;
	ELSEIF letter_grade = NULL THEN
       return NULL;
	END IF;
END $$ 