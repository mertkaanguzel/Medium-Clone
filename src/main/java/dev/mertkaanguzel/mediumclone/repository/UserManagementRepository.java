package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.model.UserAccount;
import org.springframework.data.repository.Repository;


public interface UserManagementRepository extends Repository<UserAccount, Long> {
    //public UserAccount findByUsername(String username);
    public UserAccount findByEmail(String email);
}
