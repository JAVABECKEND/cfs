package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Togarak;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TogarakRepositoryWithBagRelationships {
    Optional<Togarak> fetchBagRelationships(Optional<Togarak> togarak);

    List<Togarak> fetchBagRelationships(List<Togarak> togaraks);

    Page<Togarak> fetchBagRelationships(Page<Togarak> togaraks);
}
