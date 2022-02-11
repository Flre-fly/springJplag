package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Assignment;
import minji.jplag.domain.entity.Code;

@Getter
@Setter
@Builder
public class AssignmentDto {
    private Long id;
    private String assignmentName;
    private SubjectDto subjectDto;
    public Assignment toEntity(){
        Assignment assignment = Assignment.builder().assignmentName(assignmentName)
                .assignment_id(id)
                .subject(subjectDto.toEntity()).build();
        return assignment;
    }

}
