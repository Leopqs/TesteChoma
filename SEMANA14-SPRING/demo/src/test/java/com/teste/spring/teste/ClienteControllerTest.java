package com.teste.spring.teste;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.spring.teste.controller.ClienteController;
import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.exception.BusinessException;
import com.teste.spring.teste.exception.NotFoundException;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ClienteRepository repo;

    @MockitoBean
    ClienteService service;


    // GET /api/clientes (listar)
    @Test
    void listar_deveRetornarPaginaDeClientes() throws Exception {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Ana");
        cliente1.setEmail("ana@ex.com");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Bruno");
        cliente2.setEmail("bruno@ex.com");

        Page<Cliente> paginaSimulada = new PageImpl<>(
                List.of(cliente1, cliente2),
                PageRequest.of(0, 10, Sort.by("id").ascending()),
                2
        );

        when(repo.findAll(any(Pageable.class))).thenReturn(paginaSimulada);

        mvc.perform(get("/api/clientes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("Ana"))
                .andExpect(jsonPath("$.content[1].email").value("bruno@ex.com"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }


    // POST /api/clientes (criar)
    @Test
    void criar_deveRetornar201ECorpoDoCliente() throws Exception {
        ClienteDto dto = new ClienteDto(null, "Carlos", "carlos@ex.com", "9999-0000");

        Cliente salvo = new Cliente();
        salvo.setId(10L);
        salvo.setNome("Carlos");
        salvo.setEmail("carlos@ex.com");
        salvo.setTelefone("9999-0000");

        when(service.criar(any(Cliente.class))).thenReturn(salvo);

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@ex.com"));
    }


    @Test
    void criar_deveRetornar422QuandoEmailDuplicado() throws Exception {
        ClienteDto dto = new ClienteDto(null, "Carlos", "carlos@ex.com", "9999-0000");

        when(service.criar(any(Cliente.class)))
                .thenThrow(new BusinessException("Email já cadastrado"));

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Email já cadastrado"));
    }

    // GET /api/clientes/{id}
    @Test
    void buscar_deveRetornarClienteQuandoExiste() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(5L);
        cliente.setNome("Joana");
        cliente.setEmail("joana@ex.com");

        when(service.buscar(5L)).thenReturn(cliente);

        mvc.perform(get("/api/clientes/{id}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nome").value("Joana"))
                .andExpect(jsonPath("$.email").value("joana@ex.com"));
    }


    @Test
    void buscar_deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(service.buscar(99L))
                .thenThrow(new NotFoundException("Cliente não encontrado"));

        mvc.perform(get("/api/clientes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));
    }

    // PUT /api/clientes/{id}
    @Test
    void atualizar_deveRetornarClienteAtualizado() throws Exception {
        Long id = 3L;

        ClienteDto dto = new ClienteDto(null, "Nome Atualizado", "novo@ex.com", "5555-0000");

        Cliente atualizado = new Cliente();
        atualizado.setId(id);
        atualizado.setNome("Nome Atualizado");
        atualizado.setEmail("novo@ex.com");
        atualizado.setTelefone("5555-0000");

        when(service.atualizar(eq(id), any(Cliente.class))).thenReturn(atualizado);

        mvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.email").value("novo@ex.com"));
    }

    @Test
    void atualizar_deveRetornar422QuandoEmailDuplicado() throws Exception {
        Long id = 3L;
        ClienteDto dto = new ClienteDto(null, "Nome", "email@ex.com", "123");

        when(service.atualizar(eq(id), any(Cliente.class)))
                .thenThrow(new BusinessException("Email já cadastrado para outro cliente"));

        mvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Email já cadastrado para outro cliente"));
    }

    // DELETE /api/clientes/{id}
    @Test
    void excluir_deveRetornar204QuandoSucesso() throws Exception {
        Long id = 7L;
        doNothing().when(service).excluir(id);

        mvc.perform(delete("/api/clientes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveRetornar404QuandoNaoEncontrado() throws Exception {
        Long id = 8L;
        doThrow(new NotFoundException("Cliente não encontrado"))
                .when(service).excluir(id);

        mvc.perform(delete("/api/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));
    }

    @Test
    void clienteMapa_copyToEntity_deveCopiarDadosCorretamente() {
        // Arrange
        ClienteDto dto = new ClienteDto(null, "Novo Nome", "novo@email.com", "1177777-7777");
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNome("Nome Antigo");
        clienteExistente.setEmail("antigo@email.com");
        clienteExistente.setTelefone("1166666-6666");
        LocalDateTime dataOriginal = LocalDateTime.now();
        clienteExistente.setCriadoEm(dataOriginal);

        // Act
        ClienteMapa.copyToEntity(dto, clienteExistente);

        // Assert
        assertThat(clienteExistente.getId()).isEqualTo(1L); // ID não muda
        assertThat(clienteExistente.getNome()).isEqualTo("Novo Nome");
        assertThat(clienteExistente.getEmail()).isEqualTo("novo@email.com");
        assertThat(clienteExistente.getTelefone()).isEqualTo("1177777-7777");
        assertThat(clienteExistente.getCriadoEm()).isEqualTo(dataOriginal); // criadoEm não muda
    }
}
