package com.deepoo.web.jdbc;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

@WebServlet("/studentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtils studentDbUtils;
	
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			
			studentDbUtils = new StudentDbUtils(dataSource);
			
		} catch (Exception e) {
			throw new ServletException();
		}
	}




	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String theCommand = request.getParameter("command");
			
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			switch (theCommand) {
			case "LIST"	:
				listStudent(request, response);
				break;
			case "ADD"	:
				addStudent(request, response);
				break;
			case "LOAD" :
				loadStudent(request, response);
				break;
			case "UPDATE" :
				updateStudent(request, response);
				break;
			case "DELETE" :
				deleteStudent(request, response);
				break;
			default:
				listStudent(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId = request.getParameter("studentId");
		
		studentDbUtils.deleteStudent(theStudentId);
			
		listStudent(request, response);
	}




	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student student = new Student(id, firstName, lastName, email);
		
	    studentDbUtils.updateStudent(student);
		
		listStudent(request, response);
		
	}




	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId = request.getParameter("studentId");
		
		Student theStudent = studentDbUtils.getStudent(theStudentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher requestDispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		
		requestDispatcher.forward(request, response);
		
	}




	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student student = new Student(firstName, lastName, email);
		
		studentDbUtils.addStudent(student);
		
		listStudent(request, response);
	}




	private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Student> students = studentDbUtils.getStudents();
		
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher requestDispatcher = 
				request.getRequestDispatcher("/list-students.jsp");
		
		requestDispatcher.forward(request, response);
	}

}
