package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder bcryptEncoder;
    private final ConversionService conversionService;

    @Override
    public Page<UserDTO> findByRole(RoleEnum roleEnum, Pageable pageable) {
        return userRepository.findAllByRole(roleEnum, pageable).map(this::mapToUserDTO);
    }

    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAllByRoleNot(RoleEnum.CUSTOMER, pageable).map(this::mapToUserDTO);
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        var mongoUser = userRepository.findByEmail(userDTO.getEmail());
        if (mongoUser.isPresent()) {
            throw new RuntimeException("User exists");
        }
        var user = conversionService.convert(userDTO, UserDocument.class);
        user.setCreatedDate(new Date());
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO updatePassword(UserDTO userDTO) {
        var user = userRepository.findById(userDTO.getId()).orElseThrow();
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO findById(String id) {
        var user = userRepository.findById(id);
        return user.map(userDocument -> conversionService.convert(userDocument, UserDTO.class)).orElse(null);
    }

    protected UserDTO mapToUserDTO(UserDocument user) {
        var userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getAddresses().size() > 0) {
            var address = user.getAddresses().stream().filter(AddressDocument::getIsPrincipal).findFirst();
            if (address.isPresent()) {
                userDTO.setFullAddress(String.format("%s %s, %s",
                        address.get().getPlace().getAddress(),
                        address.get().getAddress(),
                        address.get().getPlace().getCountry()
                ));
            }
        }
        return userDTO;
    }
}
