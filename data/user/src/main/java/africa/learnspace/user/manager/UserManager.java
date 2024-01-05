package africa.learnspace.user.manager;

import africa.learnspace.user.model.User;
import africa.learnspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;

//    public UserManager(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}