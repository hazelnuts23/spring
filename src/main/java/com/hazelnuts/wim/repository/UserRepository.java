package com.hazelnuts.wim.repository;

import org.springframework.data.repository.CrudRepository;
import com.hazelnuts.wim.models.UserProfile;
public interface UserRepository extends CrudRepository<UserProfile, Long> {

}