package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.User;
import com.fizalise.dailyfitness.exception.UserAlreadyExistsException;
import com.fizalise.dailyfitness.exception.UserNotFoundException;
import com.fizalise.dailyfitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "Сервис пользователей")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Page<User> findAllUsers(Integer page) {
        return userRepository.findAll(getPageRequest(page));
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
    public UserDetailsService getUserDetailsService() {
        return this::findByEmail;
    }
    @Transactional
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        log.info("Пользователь {} сохранен в базу данных", savedUser);
        return savedUser;
    }
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с такой почтой уже существует");
        }
        return saveUser(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id.toString());
        }
        userRepository.deleteById(id);
        log.info("Удален пользователь с id: {}", id);
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, UserRepository.PAGE_SIZE, Sort.by(Sort.Direction.ASC, "name"));
    }
}
