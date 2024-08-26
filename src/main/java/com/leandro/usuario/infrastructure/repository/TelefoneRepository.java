package com.leandro.usuario.infrastructure.repository;

import com.leandro.aprendendospring.infrasctruture.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
