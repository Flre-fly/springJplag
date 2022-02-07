package minji.jplag.service;

import minji.jplag.domain.entity.Subject;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SubjectService {
    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public Long saveFile(SubjectDto subjectDto) {
        System.out.println(subjectRepository.save(subjectDto.toEntity()) + "ASDADADA");
        return subjectRepository.save(subjectDto.toEntity()).getSubject_id();
    }

    @Transactional
    public SubjectDto getSubject(Long id) {
        Subject subject = subjectRepository.findById(id).get();

        SubjectDto subjectDto = SubjectDto.builder()
                .filename(subject.getFilename())
                .filePath(subject.getFilePath())
                .build();
        return subjectDto;
    }

    public List<Subject> getFiles(){
        return subjectRepository.findAll();
    }
}
