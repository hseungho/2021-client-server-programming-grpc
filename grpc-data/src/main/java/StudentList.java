import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudentList {
	protected ArrayList<Student> vStudent;
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

	private void saveData() throws IOException {
		URL path = getClass().getResource(this.filePath);
		File file;
		try {
			file = Paths.get(path.toURI()).toFile();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		if(!file.exists()) {
			if(file.createNewFile())
				System.out.println("Create New File !!!");
			else {
				System.out.println("Cannot Create New File !!!");
				return;
			}
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		for(int i=0; i<this.vStudent.size(); i++) {
			writer.write(this.vStudent.get(i).toString()+"\n");
		}
		writer.flush();
		writer.close();
	}

	public ArrayList<Student> getAllStudentRecords() throws MyException.NullDataException {
		if(this.vStudent.size()==0) throw new MyException.NullDataException("~~~~~~~~Student data is null~~~~~~~~~");
		return this.vStudent;
	}

	public List<String> getAllStudentId() {
		return this.vStudent.stream().map(Student::getStudentId).toList();
	}
	
	public boolean addStudentRecord(ClientServer.Student studentDto) throws IOException {
		if(this.vStudent.add(Student.createStudent(studentDto))) {
			this.saveData();
			return true;
		}
		return false;
	}

	public boolean deleteStudentRecord(String studentId) throws IOException {
		for (int i = 0; i < this.vStudent.size(); i++) {
			Student student = this.vStudent.get(i);
			if (student.match(studentId)) {
				if(this.vStudent.remove(student)) {
					this.saveData();
					return true;
				}
				else return false;
			}
		}
		return false;
	}

}
