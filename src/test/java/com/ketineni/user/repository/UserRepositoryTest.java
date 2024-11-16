package com.ketineni.user.repository;

import com.ketineni.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // given
        User user = new User(null, "John Doe", "john@example.com");
        User savedUser = userRepository.save(user);

        // when
        Optional<User> found = userRepository.findById(savedUser.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(user.getName());
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findAll_WhenUsersExist_ShouldReturnAllUsers() {
        // given
        User user1 = new User(null, "John Doe", "john@example.com");
        User user2 = new User(null, "Jane Doe", "jane@example.com");
        userRepository.save(user1);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName)
                        .containsExactlyInAnyOrder("John Doe", "Jane Doe");
    }

    @Test
    void save_ShouldPersistUser() {
        // given
        User user = new User(null, "John Doe", "john@example.com");

        // when
        User saved = userRepository.save(user);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(user.getName());
        assertThat(saved.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        // given
        User user = userRepository.save(new User(null, "John Doe", "john@example.com"));

        // when
        userRepository.deleteById(user.getId());

        // then
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}