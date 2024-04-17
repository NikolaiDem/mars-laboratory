package ru.ase.mars.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.ase.mars.entity.Report;
import ru.ase.mars.enums.Statuses;

@Repository
@AllArgsConstructor
public class CustomReportRepository {

    private EntityManager em;

    public List<Report> findBy(Integer authorId,
                               String state,
                               String orderSort,
                               String fieldSort,
                               PageRequest pageRequest) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Report> cq = cb.createQuery(Report.class);

        Root<Report> book = cq.from(Report.class);
        List<Predicate> predicates = new ArrayList<>();

        if (authorId == null && state == null && fieldSort == null) {
            predicates.add(cb.notEqual(book.get("state"), Statuses.SEND.name()));
            cq.orderBy(cb.desc(book.get("lastUpdated")));
        }
        if (authorId != null) {
            predicates.add(cb.equal(book.get("author").get("id"), authorId));
        }
        if (state != null) {
            predicates.add(cb.equal(book.get("state"), state));
        }
        if (fieldSort != null) {
            if ("asc".equalsIgnoreCase(orderSort)) {
                cq.orderBy(cb.asc(book.get(fieldSort)));
            } else {
                cq.orderBy(cb.desc(book.get(fieldSort)));
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Report> query = em.createQuery(cq);
        query.setFirstResult(Math.toIntExact(pageRequest.getOffset()));
        query.setMaxResults(pageRequest.getPageSize());

        return query.getResultList();
    }
}
