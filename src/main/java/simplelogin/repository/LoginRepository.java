/**********************************
*              @2023              *
**********************************/
package simplelogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simplelogin.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}
