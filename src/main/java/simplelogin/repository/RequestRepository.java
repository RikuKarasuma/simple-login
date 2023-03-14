/**********************************
*              @2023              *
**********************************/
package simplelogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simplelogin.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
