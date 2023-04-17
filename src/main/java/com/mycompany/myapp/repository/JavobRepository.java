package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Javob;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Javob entity.
 */
@Repository
public interface JavobRepository extends JpaRepository<Javob, Long> {
    default Optional<Javob> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Javob> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Javob> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct javob from Javob javob left join fetch javob.ustoz",
        countQuery = "select count(distinct javob) from Javob javob"
    )
    Page<Javob> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct javob from Javob javob left join fetch javob.ustoz")
    List<Javob> findAllWithToOneRelationships();

    @Query("select javob from Javob javob left join fetch javob.ustoz where javob.id =:id")
    Optional<Javob> findOneWithToOneRelationships(@Param("id") Long id);
}
