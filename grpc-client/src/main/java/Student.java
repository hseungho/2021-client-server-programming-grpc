import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String studentId;
    protected String name;
    protected String department;
    protected ArrayList<String> completedCoursesList;

    public Student(String studentId, String name, String department, List<String> completedCourseList) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.completedCoursesList = new ArrayList<>(completedCourseList);
    }

    public boolean match(String studentId) {
        return this.studentId.equals(studentId);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getCompletedCourses() {
        return this.completedCoursesList;
    }

	@Override
    public String toString() {
        String stringReturn = this.studentId + " " + this.name + " " + this.department;
        for (int i = 0; i < this.completedCoursesList.size(); i++) {
            stringReturn = stringReturn + " " + this.completedCoursesList.get(i).toString();
        }
        return stringReturn;
    }

    public static Student toEntity(ClientServer.Student dto) {
        return new Student(
                dto.getId(),
                dto.getName(),
                dto.getDepartment(),
                dto.getCompletedCourseListList()
        );
    }

}
