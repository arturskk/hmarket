package com.housing.market;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long>, JpaSpecificationExecutor<Market>, MarketRepositoryCustom {
    static Specification<Market> search(QueryParamsForm form) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            Predicate predicate = cb.isTrue(cb.literal(true));
            if (form.getSize() != null) {
                predicates.add(cb.between(root.get("area"), form.getSize().rangeMin, form.getSize().rangeMax));
            }
            if (form.getRooms() != null) {
                predicates.add(cb.equal(root.get("rooms"), form.getRooms()));
            }
            if (form.getRegionId() != null) {
                predicates.add(cb.equal(root.get("regionId"), form.getRegionId()));
            }
            if (form.getDateSince() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), form.getDateSince()));
            }
            if (form.getDateUntil() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), form.getDateUntil()));
            }
            if (form.getTypes() != null && !form.getTypes().isEmpty()) {
                predicates.add(root.get("type").in(form.getTypes()));
            }
            return predicate;
        };
    }
}
