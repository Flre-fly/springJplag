package minji.jplag.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {
    @Id
    @Column(name = "code_id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    String studentNum;

    @Column(nullable = false)
    String studentName;

    @Column(nullable = false)
    String filePath;

    @Column(nullable = false)
    String code_year;

    @Column(nullable = false)
    String assignmentNum;

    @Builder
    public Code(String studentNum, String studentName,String year, String assignmentNum,  String filePath) {
        this.studentName = studentName;
        this.studentNum = studentNum;
        this.assignmentNum = assignmentNum;
        this.code_year = year;
        this.filePath = filePath;
    }

}
