package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class SubjectDto {
    @Id
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;
    private List<Code> codes;


    public Subject toEntity(){
        Subject subject = Subject.builder()
                .filename(filename)
                .filePath(filePath)
                .codes(codes)
                .build();

        return subject;
    }
    @Builder
    public SubjectDto(String filename, String filePath, List<Code> codes) {
        this.filename = filename;
        this.filePath = filePath;
        this.codes = codes;
    }
}
