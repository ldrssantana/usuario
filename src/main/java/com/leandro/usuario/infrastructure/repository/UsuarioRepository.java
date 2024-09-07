package com.leandro.usuario.infrastructure.repository;


import com.leandro.usuario.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        boolean existsByEmail(String email);

        //Option Classe do Java Util, ele serve para evitar retorno de dados nulos.
        Optional<Usuario> findByEmail(String email);

        @Transactional // Para nao causar nenhum erro na hora de deletar
        void deleteByEmail(String email);
}
