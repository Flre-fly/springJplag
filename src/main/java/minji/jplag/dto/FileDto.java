package minji.jplag.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    //dto 란 데이터 전송(이동) 객체라는 의미를 가지고 있다. ... DB의 데이터를 Service나 Controller 등으로 보낼 때 사용하는 객체
    private String studentId;
    private String fileName;

    public FileDto(){

    }
    public FileDto(String studentId, String fileName){
        this.studentId = studentId;
        this.fileName = fileName;
    }


}
