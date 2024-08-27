package com.leandro.usuario.business;


import com.leandro.usuario.business.converter.UsuarioConverter;
import com.leandro.usuario.business.dto.UsuarioDTO;
import com.leandro.usuario.infrastructure.entity.Usuario;
import com.leandro.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

     private final UsuarioRepository usuarioRepository;
     private final UsuarioConverter usuarioConverter;

     public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
          Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
          return usuarioConverter.paraUsuarioDTO(
                  usuarioRepository.save(usuario));
     }

}
