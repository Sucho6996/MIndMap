package com.SuchoMIndMap.MaindTreeUser.repo;


import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Usuaria,String> {
    Usuaria findAllByphNo(String phNo);

    Usuaria findByphNo(String username);
}
