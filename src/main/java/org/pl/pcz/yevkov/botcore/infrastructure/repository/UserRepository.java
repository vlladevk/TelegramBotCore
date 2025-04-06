package org.pl.pcz.yevkov.botcore.infrastructure.repository;

import org.pl.pcz.yevkov.botcore.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
