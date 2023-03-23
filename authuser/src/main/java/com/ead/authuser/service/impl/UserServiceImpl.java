package com.ead.authuser.service.impl;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.mappers.UserMapper;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CourseClient courseClient;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository userRepository, CourseClient courseClient, UserEventPublisher userEventPublisher, UserMapper mapper) {
        this.userRepository = userRepository;
        this.courseClient = courseClient;
        this.userEventPublisher = userEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return userRepository.existsByCpf(cpf);
    }

    @Transactional
    @Override
    public void saveUser(UserModel userModel) {
        var savedUserModel = save(userModel);
        var userEventDto = mapper.userModelToUserEventDto(savedUserModel);
        userEventPublisher.publishUserEvent(userEventDto, ActionType.CREATE);
    }

    @Transactional
    @Override
    public void deleteUser(UserModel userModel) {
        delete(userModel);
        var userEventDto = mapper.userModelToUserEventDto(userModel);
        userEventPublisher.publishUserEvent(userEventDto, ActionType.DELETE);
    }

    @Override
    public UserModel updateUser(UserModel userModel) {
        var savedUserModel = save(userModel);
        var userEventDto = mapper.userModelToUserEventDto(savedUserModel);
        userEventPublisher.publishUserEvent(userEventDto, ActionType.UPDATE);
        return savedUserModel;
    }


    @Override
    public UserModel updatePassword(UserModel userModel) {
        return save(userModel);
    }
}
