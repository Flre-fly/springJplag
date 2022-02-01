package minji.jplag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minji.jplag.domain.entity.File;

@Getter
@Setter
public class FileDto {
    //dto 란 데이터 전송(이동) 객체라는 의미를 가지고 있다. ... DB의 데이터를 Service나 Controller 등으로 보낼 때 사용하는 객체
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    public File toEntity() {
        File build = File.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public FileDto(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }


}
