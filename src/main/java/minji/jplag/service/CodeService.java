package minji.jplag.service;

import minji.jplag.domain.entity.Code;
import minji.jplag.dto.CodeDto;
import minji.jplag.repository.CodeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CodeService {
    private CodeRepository codeRepository;
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Transactional
    public Long saveFile(CodeDto codeDto) {
        return codeRepository.save(codeDto.toEntity()).getCode_id();
    }

    @Transactional
    public CodeDto getCode(Long id) {
        Code code = codeRepository.findById(id).get();

        CodeDto codeDTO = CodeDto.builder()
                .code_year(code.getCode_year())
                .filePath(code.getFilePath())
                .studentNum(code.getStudentNum())
                .studentName(code.getStudentName())
                .build();

        return codeDTO;
    }


    public List<Code> getFiles(){
        return codeRepository.findAll();
    }


}
