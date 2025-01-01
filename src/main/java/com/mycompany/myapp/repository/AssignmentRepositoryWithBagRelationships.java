package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Assignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AssignmentRepositoryWithBagRelationships {
    Optional<Assignment> fetchBagRelationships(Optional<Assignment> assignment);

    List<Assignment> fetchBagRelationships(List<Assignment> assignments);

    Page<Assignment> fetchBagRelationships(Page<Assignment> assignments);
}
