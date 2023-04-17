package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ustoz;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ustoz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UstozRepository extends JpaRepository<Ustoz, Long> {}
