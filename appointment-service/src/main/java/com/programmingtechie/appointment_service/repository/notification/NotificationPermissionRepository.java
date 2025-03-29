package com.programmingtechie.appointment_service.repository.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.notification.NotificationPermission;
import com.programmingtechie.appointment_service.enity.notification.NotificationReceiver;

@Repository
public interface NotificationPermissionRepository extends JpaRepository<NotificationPermission, String> {

    // Tìm các quyền của một NotificationReceiver theo receiver id
    List<NotificationPermission> findByNotificationReceiver_Id(String receiverId);

    // Tìm quyền của một NotificationReceiver theo quyền cụ thể
    List<NotificationPermission> findByPermission(String permission);

    // Tìm quyền của người nhận theo NotificationReceiver và quyền cụ thể
    NotificationPermission findByNotificationReceiverAndPermission(
            NotificationReceiver notificationReceiver, String permission);
}
