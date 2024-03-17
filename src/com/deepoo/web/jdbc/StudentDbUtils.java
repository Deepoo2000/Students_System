package com.deepoo.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



public class StudentDbUtils {
	
	private DataSource dataSource;

	public StudentDbUtils(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<Student>();
		
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResultSet = null;
		
		try {
			
			myConnection = dataSource.getConnection();
			
			String sql = "select * from student order by last_name";
			myStatement = myConnection.createStatement();
			
			myResultSet = myStatement.executeQuery(sql);
			
			while(myResultSet.next()) {
				
				int id = myResultSet.getInt("id");
				String firstName = myResultSet.getString("first_name");
				String lastName = myResultSet.getString("last_name");
				String email = myResultSet.getString("email");
				
				Student tempStudent = new Student(id, firstName, lastName, email); 
				
				students.add(tempStudent);
				
			}

			return students;
			
		} finally {
			close(myConnection, myStatement, myResultSet);
		} 
		
	}

	private void close(Connection myConnection, Statement myStatement, ResultSet myResultSet) {
		
		try {
			
			if(myConnection != null) {
				myConnection.close();
			}
			
			if(myStatement != null) {
				myStatement.close();
			}
			
			if(myResultSet != null) {
				myResultSet.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student student) throws SQLException {
		Connection myConnection = null;
		
		PreparedStatement myStatement = null;
		
		try {
			myConnection = dataSource.getConnection();
			
			String sql = "insert into student "
					     + "(first_name, last_name, email) "
					     + "values(?, ?, ?)";
			myStatement = myConnection.prepareStatement(sql);
			
			myStatement.setString(1, student.getFirstName());
			myStatement.setString(2, student.getLastName());
			myStatement.setString(3, student.getEmail());
			
			myStatement.execute();
			
			
		} finally {
			close(myConnection, myStatement, null);
		} 
		
		
	}


	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		ResultSet myResultSet = null;
		int studentId;
		
		try {
			
			studentId = Integer.parseInt(theStudentId);
			myConnection = dataSource.getConnection();
			
			String sql = "select * from student where id=?";
			
			myStatement = myConnection.prepareStatement(sql);
			
			((PreparedStatement) myStatement).setInt(1, studentId);
			
			myResultSet = myStatement.executeQuery();
			
			
			if(myResultSet != null) {
				while(myResultSet.next()) {
					String firstName = myResultSet.getString("first_name");
					String lastName = myResultSet.getString("last_name");
					String email = myResultSet.getString("email");
					
					theStudent = new Student(studentId, firstName, lastName, email); 
					
				}
			}
			else {
				throw new Exception("couldn't find student id: " + studentId);
			}
			
			
			return theStudent;
			
		} finally {
			close(myConnection, myStatement, myResultSet);
		}
		
	}

	public void updateStudent(Student student) throws SQLException {
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		try {
			myConnection = dataSource.getConnection();
			
			String sql = "update student "
				    + "set first_name=?, last_name=?, email=? "
				    + "where id=?";
			
			myStatement = myConnection.prepareStatement(sql);
			
			myStatement.setString(1, student.getFirstName());
			myStatement.setString(2, student.getLastName());
			myStatement.setString(3, student.getEmail());
			myStatement.setInt(4, student.getId());	
			
			myStatement.execute();
		} finally {
			close(myConnection, myStatement, null);
		}
	}

	public void deleteStudent(String theStudentId) throws SQLException {
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		try {
			
			int studentId = Integer.parseInt(theStudentId);
			
			myConnection = dataSource.getConnection();
			
			String sql = "delete from student where id=?";
			
			myStatement = myConnection.prepareStatement(sql);
			
			myStatement.setInt(1, studentId);	
			
			myStatement.execute();
		} finally {
			close(myConnection, myStatement, null);
		}
		
	}

}
