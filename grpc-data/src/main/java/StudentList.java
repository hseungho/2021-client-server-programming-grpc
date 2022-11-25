import java.io.*;
import java.util.ArrayList;

public class StudentList {
	protected ArrayList<Student> vStudent;
	private File studentFile;
	private String filePath;

	public StudentList(String sStudentFileName) throws IOException {
		this.filePath = sStudentFileName;
		InputStream is = getClass().getResourceAsStream(this.filePath);
		BufferedReader objStudentFile = new BufferedReader(new InputStreamReader(is));
		this.vStudent = new ArrayList<>();
		while (objStudentFile.ready()) {
			String stuInfo = objStudentFile.readLine();
			if (!stuInfo.equals("")) {
				this.vStudent.add(new Student(stuInfo));
			}
		}
		objStudentFile.close();
	}

	public ArrayList<Student> getAllStudentRecords() throws MyException.NullDataException {
		if(this.vStudent.size()==0) throw new MyException.NullDataException("~~~~~~~~Student data is null~~~~~~~~~");
		return this.vStudent;
	}
	
	public boolean addStudentRecord(String studentInfo) throws MyException.DuplicationDataException {
		String[] strStudentInfo = studentInfo.split(" ");
		String studentId = strStudentInfo[0];
		for(Student student:this.vStudent) 
			if(student.match(studentId)) throw new MyException.DuplicationDataException("~~~~~~~Student ID "+studentId+" is already exists!!!~~~~~");
		if(this.vStudent.add(new Student(studentInfo))) return true;
		else return false;
	}

	public boolean deleteStudentRecord(String studentId) {
		for (int i = 0; i < this.vStudent.size(); i++) {
			Student student = this.vStudent.get(i);
			if (student.match(studentId)) {
				if(this.vStudent.remove(student)) return true;
				else return false;
			}
		}
		return false;
	}
	
	public boolean isRegisteredStudent(String sSID) {
		for (int i = 0; i < this.vStudent.size(); i++) {
			Student objStudent = (Student) this.vStudent.get(i);
			if (objStudent.match(sSID)) {
				return true;
			}
		}
		return false;
	}

	public void saveData() throws IOException {
		if(!this.studentFile.exists()) {
			if(this.studentFile.createNewFile())
				System.out.println("Create New File !!!");
			else {
				System.out.println("Cannot Create New File !!!");
				return;
			}
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.studentFile, false));
		for(int i=0; i<this.vStudent.size(); i++) {
			writer.write(this.vStudent.get(i).toString()+"\n");
		}
		writer.flush();
		writer.close();
	}
	
}
