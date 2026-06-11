package com.example.EmpManagement.Repository;

import com.example.EmpManagement.Model.PfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PfAccountRepo extends JpaRepository<PfAccount, Long> {

}
