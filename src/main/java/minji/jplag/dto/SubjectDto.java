package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;

@Getter
@Setter
@Builder
public class SubjectDto {
    private Long id;
    private String subjectName;

    public Subject toEntity(){
            Subject subject = Subject.builder().subjectName(subjectName)
                    .build();
        return subject;
    }
}
