package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Builder
public class CodeDTO {
    String studentNum;
    String studentName;
    String filePath;
    String code_year;
    String assignmentNum;
    SubjectDto subjectdto;

    public Code toEntity(){

        Code code = Code.builder()
                .filePath(filePath)
                .studentName(studentName)
                .studentNum(studentNum)
                .code_year(code_year)
                .assignmentNum(assignmentNum)
                .subject(subjectdto.toEntity())
                .build();

        return code;
    }
}
