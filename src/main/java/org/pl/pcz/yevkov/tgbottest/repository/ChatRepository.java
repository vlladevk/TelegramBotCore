package org.pl.pcz.yevkov.tgbottest.repository;

import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long> {}
