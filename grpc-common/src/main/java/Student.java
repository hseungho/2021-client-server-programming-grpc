import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Data @AllArgsConstructor
public class Student implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String studentId;
    private String name;
    private String department;
    private List<String> completedCourseList;

    public Student(String inputString) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
    	this.studentId = stringTokenizer.nextToken();
    	this.name = stringTokenizer.nextToken();
    	this.name = this.name + " " + stringTokenizer.nextToken();
    	this.department = stringTokenizer.nextToken();
    	this.completedCourseList = new ArrayList<>();
    	while (stringTokenizer.hasMoreTokens()) {
    		this.completedCourseList.add(stringTokenizer.nextToken());
    	}
    }

    public boolean match(String studentId) {
        return this.studentId.equals(studentId);
    }

	@Override
    public String toString() {
        String stringReturn = this.studentId + " " + this.name + " " + this.department;
        for (int i = 0; i < this.completedCourseList.size(); i++) {
            stringReturn = stringReturn + " " + this.completedCourseList.get(i);
        }
        return stringReturn;
    }

    public static Student toEntity(ClientServer.Student dto) {
        return new Student(
                dto.getId(),
                dto.getName(),
                dto.getDepartment(),
                new ArrayList<>(dto.getCompletedCourseListList())
        );
    }

}
