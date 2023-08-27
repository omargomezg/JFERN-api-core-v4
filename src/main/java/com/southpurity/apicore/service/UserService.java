package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.dto.UserFilter;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    Page<UserDTO> findByRole(RoleEnum roleEnum, Pageable pageable);

    Page<UserDocument> findAllUsers(Pageable pageable, UserFilter filter);

    UserDocument update(UserDocument userDocument);

    UserDocument updateCodeForPwdRecovery(UserDocument userDocument, String code);

    UserDocument updatePwdWithCode(UserDocument userDocument);

    UserDTO create(UserDTO userDTO);

    UserDTO updatePassword(UserDTO userDTO);

    Optional<UserDocument> findById(String id);
}
