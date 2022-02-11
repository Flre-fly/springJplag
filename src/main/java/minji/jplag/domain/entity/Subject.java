package minji.jplag.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_id", nullable = true)
    private Long subject_id;

    @Column(nullable = false)
    private String subjectName;
}
