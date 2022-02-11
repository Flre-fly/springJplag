package minji.jplag.service;

import minji.jplag.domain.entity.Assignment;
import minji.jplag.domain.entity.Subject;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.AssignmentRepository;
import minji.jplag.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {
    private SubjectRepository subjectRepository;
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
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
                .build();

        return dto;
    }


    public List<SubjectDto> getFiles(){
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDto> dtoes = new ArrayList<>();
        for(int i =0;i<subjects.size();i++){
            SubjectDto dto = SubjectDto.builder()
                    .subjectName(subjects.get(i).getSubjectName())
                    .build();
            dtoes.add(dto);
        }
        return dtoes;
    }
}
