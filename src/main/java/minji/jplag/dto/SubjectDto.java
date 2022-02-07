package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class SubjectDto {
    //dto는 id가 필요 없음
    private String filename;
    private String filePath;


    public Subject toEntity(){
        Subject subject = Subject.builder()
                .filename(filename)
                .filePath(filePath)
                .build();

        return subject;
    }
}
