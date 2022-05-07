package platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSnippetRepository extends CrudRepository<CodeSnippet, Long> {
    CodeSnippet findByuuid (String uuid);

}
