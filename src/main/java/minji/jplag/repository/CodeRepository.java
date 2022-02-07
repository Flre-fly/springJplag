package minji.jplag.repository;

import minji.jplag.domain.entity.Code;
import minji.jplag.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {
}
