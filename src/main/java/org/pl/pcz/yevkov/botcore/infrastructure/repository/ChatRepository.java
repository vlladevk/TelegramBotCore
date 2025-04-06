package org.pl.pcz.yevkov.botcore.infrastructure.repository;

import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long> {}
