package org.example.expert.domain.manager.dto.request;

import jakarta.validation.constraints.NotNull;

public record ManagerSaveRequestDto(@NotNull Long managerUserId) {

}
