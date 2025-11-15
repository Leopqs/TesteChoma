package com.teste.spring.teste;


import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.exception.*;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteRepository repo;

    @InjectMocks
    ClienteService service;

    @Test
    void criar_deveLancarSeEmailJaExiste() {
        Cliente c = new Cliente();
        c.setNome("João");
        c.setEmail("j@ex.com");
        when(repo.existsByEmail("j@ex.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(c))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email já cadastrado");
        verify(repo, never()).save(any());
    }

    @Test
    void atualizar_deveAtualizarCamposBasicos() {
        Cliente antigo = new Cliente();
        antigo.setId(1L);
        antigo.setNome("Antigo");
        antigo.setEmail("a@ex.com");
        antigo.setTelefone("11");

        when(repo.findById(1L)).thenReturn(Optional.of(antigo));
        when(repo.findByEmail("novo@ex.com")).thenReturn(Optional.of(antigo)); // mesmo cliente
        when(repo.existsByEmail("novo@ex.com")).thenReturn(true);
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente dados = new Cliente();
        dados.setNome("Novo");
        dados.setEmail("novo@ex.com");
        dados.setTelefone("22");

        Cliente atualizado = service.atualizar(1L, dados);

        assertThat(atualizado.getNome()).isEqualTo("Novo");
        assertThat(atualizado.getEmail()).isEqualTo("novo@ex.com");
        assertThat(atualizado.getTelefone()).isEqualTo("22");
    }

    ///////////////
    ///
    @Test
    void criar_deveSalvarClienteComSucessoQuandoEmailNaoExiste() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("Maria");
        cliente.setEmail("maria@ex.com");
        cliente.setTelefone("1199999-9999");

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("Maria");
        clienteSalvo.setEmail("maria@ex.com");
        clienteSalvo.setTelefone("1199999-9999");

        when(repo.existsByEmail("maria@ex.com")).thenReturn(false);
        when(repo.save(cliente)).thenReturn(clienteSalvo);

        // Act
        Cliente resultado = service.criar(cliente);

        // Assert
        assertThat(resultado).isEqualTo(clienteSalvo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repo).existsByEmail("maria@ex.com");
        verify(repo).save(cliente);
    }

    @Test
    void atualizar_deveLancarBusinessExceptionQuandoEmailDuplicadoEmOutroCliente() {
        // Arrange
        Long idClienteAtual = 1L;
        Long idOutroCliente = 2L;

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(idClienteAtual);
        clienteExistente.setNome("Cliente Atual");
        clienteExistente.setEmail("atual@ex.com");

        Cliente outroCliente = new Cliente();
        outroCliente.setId(idOutroCliente);
        outroCliente.setNome("Outro Cliente");
        outroCliente.setEmail("novo@ex.com"); // Email que será usado na atualização

        Cliente dadosAtualizacao = new Cliente();
        dadosAtualizacao.setNome("Cliente Atualizado");
        dadosAtualizacao.setEmail("novo@ex.com"); // Email que já pertence a outro cliente

        when(repo.findById(idClienteAtual)).thenReturn(Optional.of(clienteExistente));
        when(repo.existsByEmail("novo@ex.com")).thenReturn(true);
        when(repo.findByEmail("novo@ex.com")).thenReturn(Optional.of(outroCliente));

        // Act & Assert
        assertThatThrownBy(() -> service.atualizar(idClienteAtual, dadosAtualizacao))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email já cadastrado para outro cliente");

        verify(repo, never()).save(any(Cliente.class));
    }

    @Test
    void atualizar_devePermitirAtualizacaoQuandoEmailPertenceAoMesmoCliente() {
        // Arrange
        Long idCliente = 1L;

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(idCliente);
        clienteExistente.setNome("Cliente Antigo");
        clienteExistente.setEmail("cliente@ex.com");
        clienteExistente.setTelefone("1111-1111");

        Cliente dadosAtualizacao = new Cliente();
        dadosAtualizacao.setNome("Cliente Atualizado");
        dadosAtualizacao.setEmail("cliente@ex.com"); // Mesmo email
        dadosAtualizacao.setTelefone("2222-2222");

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(idCliente);
        clienteAtualizado.setNome("Cliente Atualizado");
        clienteAtualizado.setEmail("cliente@ex.com");
        clienteAtualizado.setTelefone("2222-2222");

        when(repo.findById(idCliente)).thenReturn(Optional.of(clienteExistente));
        when(repo.existsByEmail("cliente@ex.com")).thenReturn(true);
        when(repo.findByEmail("cliente@ex.com")).thenReturn(Optional.of(clienteExistente));
        when(repo.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // Act
        Cliente resultado = service.atualizar(idCliente, dadosAtualizacao);

        // Assert
        assertThat(resultado.getNome()).isEqualTo("Cliente Atualizado");
        assertThat(resultado.getEmail()).isEqualTo("cliente@ex.com");
        assertThat(resultado.getTelefone()).isEqualTo("2222-2222");
        verify(repo).save(clienteExistente);
    }

    @Test
    void excluir_deveExcluirClienteComSucessoQuandoEncontrado() {
        // Arrange
        Long id = 1L;
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("Cliente para Excluir");
        clienteExistente.setEmail("excluir@ex.com");

        when(repo.findById(id)).thenReturn(Optional.of(clienteExistente));
        doNothing().when(repo).delete(clienteExistente);

        // Act
        service.excluir(id);

        // Assert
        verify(repo).findById(id);
        verify(repo).delete(clienteExistente);
    }

    @Test
    void excluir_deveLancarNotFoundExceptionQuandoClienteNaoExiste() {
        // Arrange
        Long id = 999L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.excluir(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cliente não encontrado");

        verify(repo, never()).delete(any());
    }

    @Test
    void atualizar_deveSalvarComSucessoQuandoEmailNaoExisteEmOutroCliente() {
        // Arrange
        Long id = 1L;

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("Nome Antigo");
        clienteExistente.setEmail("antigo@ex.com");

        Cliente dadosAtualizacao = new Cliente();
        dadosAtualizacao.setNome("Nome Novo");
        dadosAtualizacao.setEmail("novo@ex.com"); // Email que não existe

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(id);
        clienteAtualizado.setNome("Nome Novo");
        clienteAtualizado.setEmail("novo@ex.com");

        when(repo.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(repo.existsByEmail("novo@ex.com")).thenReturn(false);
        when(repo.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // Act
        Cliente resultado = service.atualizar(id, dadosAtualizacao);

        // Assert
        assertThat(resultado.getNome()).isEqualTo("Nome Novo");
        assertThat(resultado.getEmail()).isEqualTo("novo@ex.com");
        verify(repo).save(clienteExistente);
    }

    @Test
    void cliente_gettersESettersCriadoEm_devemFuncionarCorretamente() {
        // Arrange
        Cliente cliente = new Cliente();
        LocalDateTime dataHora = LocalDateTime.of(2023, 10, 15, 14, 30, 0);

        // Act
        cliente.setCriadoEm(dataHora);

        // Assert
        assertThat(cliente.getCriadoEm()).isEqualTo(dataHora);
    }

    @Test
    void clienteMapa_toDTO_e_toEntity_devemFuncionarCorretamente() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("1199999-9999");
        LocalDateTime dataHora = LocalDateTime.now();
        cliente.setCriadoEm(dataHora);

        // Act - Teste toDTO
        ClienteDto dto = ClienteMapa.toDTO(cliente);

        // Assert - Verifica toDTO
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("João Silva");
        assertThat(dto.getEmail()).isEqualTo("joao@email.com");
        assertThat(dto.getTelefone()).isEqualTo("1199999-9999");

        // Act - Teste toEntity
        Cliente clienteConvertido = ClienteMapa.toEntity(dto);

        // Assert - Verifica toEntity
        assertThat(clienteConvertido.getId()).isEqualTo(1L);
        assertThat(clienteConvertido.getNome()).isEqualTo("João Silva");
        assertThat(clienteConvertido.getEmail()).isEqualTo("joao@email.com");
        assertThat(clienteConvertido.getTelefone()).isEqualTo("1199999-9999");

        // Assert - Verifica criadoEm
        assertThat(cliente.getCriadoEm()).isEqualTo(dataHora);
    }
}
