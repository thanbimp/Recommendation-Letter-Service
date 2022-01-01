package gr.hua.ds_group_13;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter,String> {
    Optional<Letter> findLetterByProfLName(String ProfLName);
}
