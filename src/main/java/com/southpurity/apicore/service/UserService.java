package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDTO> findByRole(RoleEnum roleEnum, Pageable pageable);

    Page<UserDTO> findAllUsers(Pageable pageable);

    UserDTO create(UserDTO userDTO);

    UserDTO findById(String id);
}
