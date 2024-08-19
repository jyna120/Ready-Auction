package com.readyauction.app.cash.repository;

import com.readyauction.app.cash.entity.Payment;
import com.readyauction.app.cash.entity.PaymentCategory;
import com.readyauction.app.cash.entity.PaymentStatus;
import com.readyauction.app.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMemberIdAndProductIdAndCategory(Long memberId, Long productId, PaymentCategory category);

    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.status = 'ROLLBACK' WHERE p.productId = :productId AND p.category = :category AND p.status = :status")
    int updateStatusToRollbackByProductIdAndCategory(@Param("productId") Long productId, @Param("category") PaymentCategory category, @Param("status") PaymentStatus status);

    Optional<List<Payment>> findByProductIdAndMemberIdAndCategoryAndStatusOrStatus(Long productId,Long memberId,PaymentCategory category, PaymentStatus status, PaymentStatus status2);
    Optional<List<Payment>> findByProductIdAndMemberIdAndCategoryNotAndStatusOrStatus(Long productId,Long memberId,PaymentCategory category, PaymentStatus status, PaymentStatus status2);
    Optional<List<Payment>> findByProductIdAndStatus(Long productId, PaymentStatus status);

    // 송신 계좌 또는 수신 계좌와 연관된 모든 결제 내역을 조회
    @Query("SELECT p FROM Payment p WHERE p.senderAccount.id = :accountId OR p.receiverAccount.id = :accountId ORDER BY p.date DESC")
    List<Payment> findAllByAccountIdOrderByDateDesc(@Param("accountId") Long accountId);
 }
