package gr.hua.ds_group_13;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter,String> {
    Optional<Letter> findLetterByProfLName(String ProfLName);
    @Query("select l from Letter l where l.AppID = ?1")
    Optional<Letter> findLetterByAppID (Application AppID);
}
