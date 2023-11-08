package dev.mertkaanguzel.mediumclone.repository;


import dev.mertkaanguzel.mediumclone.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount getUserByUsername(String username);
}
