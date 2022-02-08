package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;

@Getter
@Setter
@Builder
public class CodeDTO {
    private String studentNum;
    private String studentName;
    private String filePath;
    private String code_year;
    private String assignmentNum;
    private String subjectName;

    public Code toEntity(){
        Code code = Code.builder()
                .filePath(filePath)
                .studentName(studentName)
                .studentNum(studentNum)
                .code_year(code_year)
                .assignmentNum(assignmentNum)
                .subjectName(subjectName)
                .build();

        return code;
    }
}
