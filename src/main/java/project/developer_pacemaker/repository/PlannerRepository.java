package project.developer_pacemaker.repository;

        import org.springframework.data.domain.Sort;
        import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.developer_pacemaker.entity.PlannerEntity;

        import java.time.LocalDate;
        import java.util.List;
        import java.util.Optional;

public interface PlannerRepository extends JpaRepository<PlannerEntity, Long> {
    List<PlannerEntity> findByUser_uSeqAndIsDeleted(Long uSeq, boolean b, Sort sort);

    Optional<PlannerEntity> findBypSeqAndIsDeletedFalse(long pSeq);

    Optional<PlannerEntity> findByUser_uSeqAndIsDeletedAndRegistered(Long uSeq, boolean b, LocalDate parse);

    // List<PlannerEntity> findByUser_uSeqAndIsDeletedAndRegisteredBetween(Long uSeq, boolean b, LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT p.registered, COUNT(t.tSeq)
        FROM PlannerEntity p
        LEFT JOIN p.todoEntities t
            ON t.isCompleted = true
            AND t.isDeleted = false
        WHERE p.user.uSeq = :uSeq
        AND p.isDeleted = false
        AND p.registered BETWEEN :startDate AND :endDate
        GROUP BY p.registered
    """)
    List<Object[]> findCompletedTodoCountByDate(
            @Param("uSeq") Long uSeq,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}