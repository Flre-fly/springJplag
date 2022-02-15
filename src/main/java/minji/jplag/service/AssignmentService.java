package minji.jplag.service;

import minji.jplag.domain.entity.Assignment;
import minji.jplag.domain.entity.Code;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.CodeDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentService {
    private AssignmentRepository assignmentRepository;
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public Long saveFile(AssignmentDto assignmentDto) {
        return assignmentRepository.save(assignmentDto.toEntity()).getAssignment_id();
    }

    @Transactional
    public AssignmentDto getAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id).get();

        AssignmentDto dto = AssignmentDto.builder()
                .assignmentName(assignment.getAssignmentName())
                .subjectDto(SubjectDto.builder().subjectName(assignment.getSubject().getSubjectName()).build())
                .build();

        return dto;
    }


    public List<AssignmentDto> getFiles(){
        List<Assignment> assignments = assignmentRepository.findAll();
        List<AssignmentDto> dtoes = new ArrayList<>();
        for(int i =0;i<assignments.size();i++){
            AssignmentDto dto = AssignmentDto.builder()
                    .assignmentName(assignments.get(i).getAssignmentName())
                    .subjectDto(SubjectDto.builder().subjectName(assignments.get(i).getSubject().getSubjectName())
                            .id(assignments.get(i).getSubject().getSubject_id()).build())
                    .id(assignments.get(i).getAssignment_id())
                    .build();
            dtoes.add(dto);
        }
        return dtoes;
    }
}
