package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FanRepository extends JpaRepository<Fan, Long> {}
