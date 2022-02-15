package minji.jplag.service;

import minji.jplag.domain.entity.Code;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.CodeDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.CodeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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


    public List<CodeDto> getFiles(){
        List<Code> codeList = codeRepository.findAll();
        List<CodeDto> dtoes = new ArrayList<>();
        for(int i =0;i<codeList.size();i++){
            SubjectDto subjectDto = SubjectDto.builder().subjectName(codeList.get(i).getSubjectName()).build();
            AssignmentDto assignmentDto = AssignmentDto.builder().subjectDto(subjectDto).assignmentName(codeList.get(i).getAssignment().getAssignmentName()).build();
            CodeDto dto = CodeDto.builder()
                    .assignmentDto(assignmentDto)
                    .code_year(codeList.get(i).getCode_year())
                    .subjectName(codeList.get(i).getSubjectName())
                    .studentName(codeList.get(i).getStudentName())
                    .studentNum(codeList.get(i).getStudentNum())
                    .filePath(codeList.get(i).getFilePath())
                    .id(codeList.get(i).getCode_id())
                    .build();
            dtoes.add(dto);
        }
        return dtoes;
    }


}
