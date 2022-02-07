package minji.jplag.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject {
    @Id
    @Column(name="subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subject_id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;






}
