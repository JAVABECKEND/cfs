package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vazifa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vazifa entity.
 */
@Repository
public interface VazifaRepository extends JpaRepository<Vazifa, Long> {
    default Optional<Vazifa> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vazifa> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vazifa> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vazifa from Vazifa vazifa left join fetch vazifa.ustoz left join fetch vazifa.fan",
        countQuery = "select count(distinct vazifa) from Vazifa vazifa"
    )
    Page<Vazifa> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vazifa from Vazifa vazifa left join fetch vazifa.ustoz left join fetch vazifa.fan")
    List<Vazifa> findAllWithToOneRelationships();

    @Query("select vazifa from Vazifa vazifa left join fetch vazifa.ustoz left join fetch vazifa.fan where vazifa.id =:id")
    Optional<Vazifa> findOneWithToOneRelationships(@Param("id") Long id);
}
