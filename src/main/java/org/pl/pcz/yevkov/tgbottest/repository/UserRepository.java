package org.pl.pcz.yevkov.tgbottest.repository;

import org.pl.pcz.yevkov.tgbottest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
