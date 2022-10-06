/* Put your final project reporting queries here */
USE cs_hu_310_final_project;

-- Calculate the GPA for student given a student_id (use student_id=1) 
select students.first_name, students.last_name, 
count(class_registrations.student_id) as number_of_classes, 
sum(convert_to_grade_point(grades.letter_grade)) as total_grade_points_earned,
avg(convert_to_grade_point(grades.letter_grade)) as GPA
from students
left join class_registrations on students.student_id = class_registrations.student_id
left join grades on class_registrations.grade_id = grades.grade_id
where students.student_id = 1
group by class_registrations.student_id;

-- Calculate the GPA for each student (across all classes and all terms) 
select students.first_name, students.last_name, 
count(class_registrations.grade_id) as number_of_classes, 
sum(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as total_grade_points_earned,
avg(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as GPA
from students
left join class_registrations on students.student_id = class_registrations.student_id
left join grades on class_registrations.grade_id = grades.grade_id
where students.student_id = class_registrations.student_id
group by class_registrations.student_id;

-- Calculate the avg GPA for each class 
select classes.code, classes.name, count(class_registrations.grade_id) as number_of_grades,
sum(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as total_grade_points,
avg(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as 'AVG GPA'
from class_sections
left join class_registrations on class_sections.class_section_id = class_registrations.class_section_id
left join classes on classes.class_id = class_sections.class_id
left join grades on class_registrations.grade_id = grades.grade_id
group by class_sections.class_id;

-- Calculate the avg GPA for each class and term 
select classes.code, classes.name, terms.name as term, 
count(class_registrations.grade_id) as number_of_grades,
sum(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as total_grade_points,
avg(convert_to_grade_point(ifnull(grades.letter_grade,'N'))) as 'AVG GPA'
from class_sections
left join classes on classes.class_id = class_sections.class_id
left join terms on terms.term_id = class_sections.term_id
left join class_registrations on class_sections.class_section_id = class_registrations.class_section_id
left join grades on class_registrations.grade_id = grades.grade_id
group by class_sections.class_section_id
order by terms.term_id;

-- List all the classes being taught by an instructor (use instructor_id=1) 
select instructors.first_name, instructors.last_name, academic_titles.title, 
classes.code, classes.name as class_name, terms.name as term
from instructors
left join academic_titles on instructors.academic_title_id = academic_titles.academic_title_id
left join class_sections on class_sections.instructor_id = instructors.instructor_id
left join classes on classes.class_id = class_sections.class_id
left join terms on terms.term_id = class_sections.term_id
where instructors.instructor_id = 1;

-- List all classes with terms & instructor 
select classes.code, classes.name, terms.name as term, instructors.first_name, instructors.last_name
from instructors
left join class_sections on class_sections.instructor_id = instructors.instructor_id
left join classes on classes.class_id = class_sections.class_id
left join terms on terms.term_id = class_sections.term_id
order by class_sections.class_section_id;

-- Calculate the remaining space left in a class 
select classes.code, classes.name, terms.name as term,
count(class_registrations.class_section_id) as enrolled_students, 
(classes.maximum_students - count(class_registrations.class_section_id)) as space_remaining
from class_sections
left join classes on classes.class_id = class_sections.class_id
left join terms on terms.term_id = class_sections.term_id
left join class_registrations on class_sections.class_section_id = class_registrations.class_section_id
group by class_sections.class_section_id
HAVING count(class_registrations.class_section_id) > 0;