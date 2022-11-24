import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Student implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String studentId;
    private String name;
    private String department;
    protected ArrayList<String> completedCoursesList;

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getCompletedCourses() {
        return this.completedCoursesList;
    }

    public Student(String inputString) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
    	this.studentId = stringTokenizer.nextToken();
    	this.name = stringTokenizer.nextToken();
    	this.name = this.name + " " + stringTokenizer.nextToken();
    	this.department = stringTokenizer.nextToken();
    	this.completedCoursesList = new ArrayList<String>();
    	while (stringTokenizer.hasMoreTokens()) {
    		this.completedCoursesList.add(stringTokenizer.nextToken());
    	}
    }

    public boolean match(String studentId) {
        return this.studentId.equals(studentId);
    }

	@Override
    public String toString() {
        String stringReturn = this.studentId + " " + this.name + " " + this.department;
        for (int i = 0; i < this.completedCoursesList.size(); i++) {
            stringReturn = stringReturn + " " + this.completedCoursesList.get(i).toString();
        }
        return stringReturn;
    }

}
