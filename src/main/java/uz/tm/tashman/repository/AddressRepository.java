package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}