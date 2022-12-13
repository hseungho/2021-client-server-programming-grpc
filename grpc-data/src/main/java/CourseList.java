import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CourseList {
	private ArrayList<Course> vCourse;
	private String filePath;
	
	public CourseList(String cCourseFileName) throws IOException {
		this.filePath = cCourseFileName;
		InputStream is = getClass().getResourceAsStream(this.filePath);
		BufferedReader objCourseFile = new BufferedReader(new InputStreamReader(is));
		this.vCourse = new ArrayList<>();
		while(objCourseFile.ready()) {
			String corInfo = objCourseFile.readLine();
			if(!corInfo.equals(""))
				this.vCourse.add(new Course(corInfo));
		}
		objCourseFile.close();
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
		for(int i=0; i<this.vCourse.size(); i++) {
			writer.write(this.vCourse.get(i).toString()+"\n");
		}
		writer.flush();
		writer.close();
	}
	
	public ArrayList<Course> getAllCourseRecords() throws MyException.NullDataException{
		if(this.vCourse.size()==0) throw new MyException.NullDataException("~~~~~~~~Course data is null~~~~~~~~~");
		return this.vCourse;
	}

	public List<String> getAllCourseId() {
		return this.vCourse.stream().map(Course::getCourseId).toList();
	}

	public boolean addCourseRecord(ClientServer.Course courseDto) throws IOException {
		if(this.vCourse.add(Course.createCourse(courseDto))) {
			this.saveData();
			return true;
		}
		return false;
	}

	public boolean deleteCourseRecord(String courseId) throws IOException {
		for(Course course : this.vCourse) {
			if(course.match(courseId)) {
				if(this.vCourse.remove(course)) {
					this.saveData();
					return true;
				}
				else return false;
			}
		}
		return false;
	}

}
