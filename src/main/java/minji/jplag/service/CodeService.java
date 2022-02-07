package minji.jplag.service;

import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;
import minji.jplag.dto.CodeDTO;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.CodeRepository;
import minji.jplag.repository.SubjectRepository;
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
    public Long saveFile(CodeDTO codeDto) {
        return codeRepository.save(codeDto.toEntity()).getCode_id();
    }

    @Transactional
    public CodeDTO getCode(Long id) {
        Code code = codeRepository.findById(id).get();

        CodeDTO codeDTO = CodeDTO.builder()
                .code_year(code.getCode_year())
                .filePath(code.getFilePath())
                .assignmentNum(code.getAssignmentNum())
                .studentNum(code.getStudentNum())
                .studentName(code.getStudentName())
                .build();

        return codeDTO;
    }

    public List<Code> getFiles(){
        return codeRepository.findAll();
    }
}
