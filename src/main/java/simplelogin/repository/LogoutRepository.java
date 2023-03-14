/**********************************
*              @2023              *
**********************************/
package simplelogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simplelogin.entity.Logout;

@Repository
public interface LogoutRepository extends JpaRepository<Logout, Long> {
}
