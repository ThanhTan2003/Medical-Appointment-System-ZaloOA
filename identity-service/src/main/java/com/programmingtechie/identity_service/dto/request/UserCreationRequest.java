package com.programmingtechie.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String userName;

    String password;

    String accountName;

    String status;

    String doctorId;

    String roleId;
}
