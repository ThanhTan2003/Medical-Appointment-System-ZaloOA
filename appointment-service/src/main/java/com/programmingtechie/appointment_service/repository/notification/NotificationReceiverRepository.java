package com.programmingtechie.appointment_service.repository.notification;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.notification.NotificationReceiver;

@Repository
public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver, String> {

    // Tìm NotificationReceiver theo UID
    Optional<NotificationReceiver> findByUid(String uid);

    // Tìm danh sách người nhận thông báo theo quyền với phân trang
    Page<NotificationReceiver> findByNotificationPermissions_Permission(String permission, Pageable pageable);
}
