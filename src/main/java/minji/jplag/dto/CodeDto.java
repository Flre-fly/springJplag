package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;

@Getter
@Setter
@Builder
public class CodeDto {
    private Long id;
    private String studentNum;
    private String studentName;
    private String filePath;
    private String code_year;
    private String subjectName;
    private AssignmentDto assignmentDto;
    public Code toEntity(){
        Code code = Code.builder()
                .filePath(filePath)
                .studentName(studentName)
                .studentNum(studentNum)
                .code_year(code_year)
                .subjectName(subjectName)
                .assignment(assignmentDto.toEntity())
                .build();

        return code;
    }
}
