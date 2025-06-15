package com.klymb.quiz_service.entity.specfications;

import com.klymb.quiz_service.entity.QuestionBank;
import com.klymb.quiz_service.entity.UserGroup;
import com.klymb.quiz_service.entity.enums.QuestionBankStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class CustomSpecifications {
    public static Specification<QuestionBank> questionBankSpecification(
            String tenantId,
            String title,
            QuestionBankStatus status,
            String categoryValue
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tenantId != null && !tenantId.isBlank()) {
                predicates.add(cb.equal(root.get("tenantId"), tenantId));
            }

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (categoryValue != null && !categoryValue.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("category").get("value")),
                        "%" + categoryValue.toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

 public static Specification<UserGroup> userGroupSpecification(
            String tenantId,
            String title,
            Boolean isEnabled
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tenantId != null && !tenantId.isBlank()) {
                predicates.add(cb.equal(root.get("tenantId"), tenantId));
            }

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (isEnabled != null) {
                predicates.add(cb.equal(root.get("isEnabled"), isEnabled));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

