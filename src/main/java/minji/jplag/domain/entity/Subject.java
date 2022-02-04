package minji.jplag.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject {
    @Id
    @Column(name = "subject_id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @OneToMany
    @JoinColumn(name = "code_id")
    private List<Code> codes;


    @Builder
    public Subject(String filename, String filePath, List<Code> codes) {
        this.filename = filename;
        this.filePath = filePath;
        this.codes = codes;
    }

}
