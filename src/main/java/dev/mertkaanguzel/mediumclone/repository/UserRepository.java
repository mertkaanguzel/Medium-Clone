package dev.mertkaanguzel.mediumclone.repository;


import dev.mertkaanguzel.mediumclone.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> getUserByUsername(String username);
    public Optional<UserAccount> findByEmail(String email);
}
