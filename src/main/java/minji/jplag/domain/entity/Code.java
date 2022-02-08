package minji.jplag.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {
    @Id//얘는 pk이다 라는걸 알려주는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id", nullable = true)
    private Long code_id;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String studentNum;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String code_year;

    @Column(nullable = false)
    private String assignmentNum;


}
