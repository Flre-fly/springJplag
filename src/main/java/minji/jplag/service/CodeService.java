package minji.jplag.service;

import minji.jplag.domain.entity.Code;
import minji.jplag.dto.CodeDTO;
import minji.jplag.repository.CodeRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class CodeService {
    private CodeRepository codeRepository;
    EntityManager em;
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Transactional
    public Long saveFile(CodeDTO codeDto) {
        //System.out.println(codeDto.toEntity().getCode_year() + "dddddddddddd");
        //em.persist(codeDto.toEntity());
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
