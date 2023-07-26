package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDTO> findByRole(RoleEnum roleEnum, Pageable pageable);

    Page<UserDTO> findAllUsers(Pageable pageable);

    UserDocument update(UserDocument userDocument);

    UserDocument updateCodeForPwdRecovery(UserDocument userDocument, String code);

    UserDocument updatePwdWithCode(UserDocument userDocument);

    UserDTO create(UserDTO userDTO);

    UserDTO updatePassword(UserDTO userDTO);

    UserDTO findById(String id);
}
