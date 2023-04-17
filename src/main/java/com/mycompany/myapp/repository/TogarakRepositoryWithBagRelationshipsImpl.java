package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Togarak;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TogarakRepositoryWithBagRelationshipsImpl implements TogarakRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Togarak> fetchBagRelationships(Optional<Togarak> togarak) {
        return togarak.map(this::fetchUstozs);
    }

    @Override
    public Page<Togarak> fetchBagRelationships(Page<Togarak> togaraks) {
        return new PageImpl<>(fetchBagRelationships(togaraks.getContent()), togaraks.getPageable(), togaraks.getTotalElements());
    }

    @Override
    public List<Togarak> fetchBagRelationships(List<Togarak> togaraks) {
        return Optional.of(togaraks).map(this::fetchUstozs).orElse(Collections.emptyList());
    }

    Togarak fetchUstozs(Togarak result) {
        return entityManager
            .createQuery("select togarak from Togarak togarak left join fetch togarak.ustozs where togarak is :togarak", Togarak.class)
            .setParameter("togarak", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Togarak> fetchUstozs(List<Togarak> togaraks) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, togaraks.size()).forEach(index -> order.put(togaraks.get(index).getId(), index));
        List<Togarak> result = entityManager
            .createQuery(
                "select distinct togarak from Togarak togarak left join fetch togarak.ustozs where togarak in :togaraks",
                Togarak.class
            )
            .setParameter("togaraks", togaraks)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
