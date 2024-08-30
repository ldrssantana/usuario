package com.leandro.usuario.business;

import com.leandro.usuario.business.converter.UsuarioConverter;
import com.leandro.usuario.business.dto.EnderecoDTO;
import com.leandro.usuario.business.dto.TelefoneDTO;
import com.leandro.usuario.business.dto.UsuarioDTO;
import com.leandro.usuario.infrastructure.entity.Endereco;
import com.leandro.usuario.infrastructure.entity.Telefone;
import com.leandro.usuario.infrastructure.entity.Usuario;
import com.leandro.usuario.infrastructure.exceptions.ConflictException;
import com.leandro.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.leandro.usuario.infrastructure.repository.EnderecoRepository;
import com.leandro.usuario.infrastructure.repository.TelefoneRepository;
import com.leandro.usuario.infrastructure.repository.UsuarioRepository;
import com.leandro.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

     private final UsuarioRepository usuarioRepository;
     private final UsuarioConverter usuarioConverter;
     private final PasswordEncoder passwordEncoder;
     private final JwtUtil jwtUtil;
     private final EnderecoRepository enderecoRepository;
     private final TelefoneRepository telefoneRepository;

     public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
          emailExiste(usuarioDTO.getEmail());
          usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
          Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
          return usuarioConverter.paraUsuarioDTO(
                  usuarioRepository.save(usuario));
     }

     public void emailExiste(String email) {
          try {
               boolean existe = verificaEmailExistente(email);
               if (existe) {
                    throw new ConflictException("Email já cadastrado" + email);
               }
          } catch (ConflictException e) {
               throw new ConflictException("Email já cadastrado " + e.getCause());
          }
     }

     public boolean verificaEmailExistente(String email) {
          return usuarioRepository.existsByEmail(email);
     }

     public UsuarioDTO buscarUsuarioPorEmail(String email) {
          try {
               return usuarioConverter.paraUsuarioDTO(
                       usuarioRepository.findByEmail(email).orElseThrow(
                               () -> new ResourceNotFoundException("Email não encontrado" + email))); //esse orElseThrow para nao quebrar o codigo caso nao exista.

          } catch (ResourceNotFoundException e) {
               throw new ResourceNotFoundException("Email não encontrado" + email);
          }


     }

     public void deletaUsuarioPorEmail(String email) {
          usuarioRepository.deleteByEmail(email);
     }

     public UsuarioDTO atualizaDadosUsuario(String token , UsuarioDTO dto){
         //Aqui buscamso o email do usuario atraves do token (tirar obrigatoriedade do emial)
          String email = jwtUtil.extrairEmailToken(token.substring(7));

          //Criptografia de senha
          dto.setSenha(dto.getSenha() !=null ? passwordEncoder.encode(dto.getSenha()) : null);

          //Busca os dados do usuario no banco de dados
          Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                  new ResourceNotFoundException("Email não localizado" + email));

          //mesclou  os dados que recebemos na requisição DTO com os dados de banco de dados
          Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

          //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para UsuarioDTO.
          return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

     }

     public EnderecoDTO atualizaEndereco(Long idEndereco , EnderecoDTO enderecoDTO){
          Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                  new ResourceNotFoundException("Id não encontrado" + idEndereco));
          Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO ,entity);

          return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

     }

     public TelefoneDTO atualizaTelefone(Long idTelefone , TelefoneDTO dto){
          Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                  new ResourceNotFoundException("Id do telefone não encontrado" + idTelefone));
          Telefone telefone = usuarioConverter.updateTelefone(dto, entity);

          return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));


     }
}
