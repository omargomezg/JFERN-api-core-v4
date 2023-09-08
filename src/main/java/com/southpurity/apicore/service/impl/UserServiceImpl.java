package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.dto.UserFilter;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.PasswordReset;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final PlaceRepository placeRepository;
    private final PasswordEncoder bcryptEncoder;
    private final ConversionService conversionService;

    @Override
    public Page<UserDTO> findByRole(RoleEnum roleEnum, Pageable pageable) {
        return userRepository.findAllByRole(roleEnum, pageable).map(this::mapToUserDTO);
    }

    @Override
    public Page<UserDocument> findAllUsers(Pageable pageable, UserFilter filter) {
        Query query = new Query().with(pageable);
        if (filter.getPlaceId() != null) {
            query.addCriteria(Criteria.where("addresses.place.id").is(filter.getPlaceId()));
        }
        if (filter.getRole() != null) {
            query.addCriteria(Criteria.where("role").in(filter.getRole()));
        }
        query.with(Sort.by(Sort.Direction.DESC, "updatedDate"));
        var users = mongoTemplate.find(query, UserDocument.class);
        users.forEach(user -> {
            if (user.getRole().equals(RoleEnum.CUSTOMER) && user.getPlaceId() != null) {
                placeRepository.findById(user.getPlaceId()).ifPresent(user::setPlace);
            }
        });
        return PageableExecutionUtils.getPage(
                users,
                pageable,
                () -> mongoTemplate.count(query, UserDocument.class));
    }

    @Override
    public UserDocument update(UserDocument user) {
        var userDocument = userRepository.findByEmail(user.getEmail()).orElseThrow();
        user.setId(userDocument.getId());
        return userRepository.save(user);
    }

    @Override
    public UserDocument updateCodeForPwdRecovery(UserDocument userDocument, String code) {
        var user = userRepository.findByEmail(userDocument.getEmail()).orElseThrow();
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setCode(code);
        passwordReset.setRequestedAt(Instant.now());
        user.setPasswordReset(passwordReset);
        return userRepository.save(user);
    }

    @Override
    public UserDocument updatePwdWithCode(UserDocument userDocument) {
        var user = userRepository.findByEmail(userDocument.getEmail()).orElseThrow();
        if (!user.getPasswordReset().isValid(userDocument.getPasswordReset().getCode())) {
            throw new RuntimeException("Expired code");
        }
        user.setPassword(bcryptEncoder.encode(userDocument.getPassword()));
        return userRepository.save(user);
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
    public Optional<UserDocument> findById(String id) {
        var user = userRepository.findById(id);
        if (user.isPresent()){
            if (user.get().getRole().equals(RoleEnum.CUSTOMER) && user.get().getPlaceId() != null) {
                placeRepository.findById(user.get().getPlaceId()).ifPresent(user.get()::setPlace);
            }
        }return user;
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
