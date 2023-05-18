package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {

    Page<UserDocument> findAllByRole(RoleEnum roleEnum, Pageable pageable);

    Page<UserDocument> findAllByRoleNot(RoleEnum roleEnum, Pageable pageable);

    Optional<UserDocument> findByEmail(String email);

    List<UserDocument> findAllByRole(RoleEnum roleEnum);
}
