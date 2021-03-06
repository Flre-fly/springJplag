package minji.jplag.service;

import minji.jplag.domain.entity.Assignment;
import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.CodeDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.AssignmentRepository;
import minji.jplag.repository.CodeRepository;
import minji.jplag.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {
    private SubjectRepository subjectRepository;
    private AssignmentService assignmentService;
    private AssignmentRepository assignmentRepository;
    private CodeService codeService;
    private CodeRepository codeRepository;
    @Autowired
    public SubjectService(SubjectRepository subjectRepository,
                          AssignmentService assignmentService,
                          AssignmentRepository assignmentRepository,
                          CodeService codeService,
                          CodeRepository codeRepository
                          ) {
        this.subjectRepository = subjectRepository;
        this.assignmentService = assignmentService;
        this.codeService = codeService;
        this.codeRepository = codeRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public Long saveFile(SubjectDto subjectDto) {
        return subjectRepository.save(subjectDto.toEntity()).getSubject_id();
    }

    @Transactional
    public SubjectDto subjectDto(Long id) {
        Subject subject = subjectRepository.findById(id).get();

        SubjectDto dto = SubjectDto.builder()
                .subjectName(subject.getSubjectName())
                .id(id)
                .build();

        return dto;
    }

    @Transactional
    public void delete(Long id){
        //subject > assignment > code
        SubjectDto subjectDto = subjectDto(id);


        List<AssignmentDto> assignmentDtoList = assignmentService.getFiles();
        AssignmentDto deleteAssignmentDto;


        //code??? ?????? ????????? assignment??? ?????????

        //?????? assignment??? ?????????
        for(int i=0;i<assignmentDtoList.size();i++){
            //????????????????????? subject??? ????????? ????????? ???????????????
            if(assignmentDtoList.get(i).getSubjectDto().getSubjectName().equals(subjectDto.getSubjectName())){
                deleteAssignmentDto = assignmentDtoList.get(i);

                //?????? code??? ?????????
                List<CodeDto> codeDtoList = codeService.getFiles();
                for(int k=0;k<codeDtoList.size();k++){
                    //??????code??? ????????? subject????????? ?????? code??? ???????????????
                    if(codeDtoList.get(k).getAssignmentDto().getSubjectDto().getSubjectName().equals(subjectDto.getSubjectName())){

                        codeRepository.deleteById(codeDtoList.get(k).getId());
                    }
                }
                //code??? ??? ????????? ??? code??? ????????????????????????????????? ??????????????????
                assignmentRepository.deleteById(deleteAssignmentDto.getId());

                }
        }
        subjectRepository.deleteById(subjectDto.getId());

    }


    public List<SubjectDto> getFiles(){
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDto> dtoes = new ArrayList<>();
        for(int i =0;i<subjects.size();i++){
            SubjectDto dto = SubjectDto.builder()
                    .subjectName(subjects.get(i).getSubjectName())
                    .id(subjects.get(i).getSubject_id())
                    .build();
            dtoes.add(dto);
        }
        return dtoes;
    }
}
