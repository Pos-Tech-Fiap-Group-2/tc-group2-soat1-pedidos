package com.techchallenge.pedidos.adapter.mapper.business;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;
import com.techchallenge.pedidos.drivers.db.entities.ItemPedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.PedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class PedidoBusinessMapperTest {

	@InjectMocks
	private PedidoBusinessMapper api;
	
	@Mock
	private ModelMapper mapper;
	
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
	private Cliente createCliente(Long id, Long cpf, String email, String nome) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(cpf);
		cliente.setEmail(email);
		cliente.setId(id);
		cliente.setNome(nome);
		
		return cliente;
	}
	
	private Categoria createCategoria(Long id, String nome) {
		Categoria categoria = new Categoria();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private Produto createProduto(String descricao, Long id, String imagem, String nome, BigDecimal preco) {
		Produto produto = new Produto();
		
		produto.setCategoria(createCategoria(1L, "Lanche"));
		produto.setDescricao(descricao);
		produto.setId(id);
		produto.setImagem(imagem);
		produto.setNome(nome);
		produto.setPreco(preco);
		
		return produto;
	}
	
    private Pedido createPedido(Long id, Cliente cliente) {
    	return createPedido(id, cliente, StatusPedido.RECEBIDO);
    }
    
    private Pedido createPedido(Long id, Cliente cliente, StatusPedido status) {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(cliente);
    	pedido.setId(id);
    	pedido.setItens(createItensPedido(pedido));
    	pedido.setStatus(status);
    	
    	return pedido;
    }
    
	private ItemPedido createItemPedido(Long id, Pedido pedido, BigDecimal precoTotal, Produto produto, Integer quantidade) {
		ItemPedido itemPedido = new ItemPedido();
		
		itemPedido.setId(id);
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(precoTotal);
		itemPedido.setProduto(produto);
		itemPedido.setQuantidade(quantidade);
		
		return itemPedido;
	}
	
	@SuppressWarnings("serial")
	private List<ItemPedido> createItensPedido(Pedido pedido) {
		
		return new ArrayList<ItemPedido>() {{
			this.add(createItemPedido(1L, pedido, new BigDecimal("25.99"), createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99")), 2));
			this.add(createItemPedido(1L, pedido, new BigDecimal("20.99"), createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger duplo", new BigDecimal("20.99")), 1));
		}};
	}
	
	private List<ItemPedidoEntity> createItemPedidoEntities(List<ItemPedido> itens) {
		return itens.stream().map(i -> createItemPedidoEntity(i)).collect(Collectors.toList());
	}
	
	private ItemPedidoEntity createItemPedidoEntity(ItemPedido itemPedido) {
		PedidoEntity pedidoEntity = new PedidoEntity();
		
		pedidoEntity.setCliente(createClienteEntity(itemPedido.getPedido().getCliente()));
		pedidoEntity.setDataCancelamento(itemPedido.getPedido().getDataCancelamento());
		pedidoEntity.setDataFinalizacao(itemPedido.getPedido().getDataFinalizacao());
		pedidoEntity.setDataSolicitacao(itemPedido.getPedido().getDataSolicitacao());
		pedidoEntity.setId(itemPedido.getPedido().getId());
		pedidoEntity.setId(itemPedido.getPedido().getId());
		pedidoEntity.setPaymentId(itemPedido.getPedido().getPaymentId());
		pedidoEntity.setStatus(itemPedido.getPedido().getStatus());
		pedidoEntity.setValor(itemPedido.getPedido().getValor());
		
		ItemPedidoEntity entity = new ItemPedidoEntity();
		
		entity.setId(itemPedido.getId());
		entity.setPedido(pedidoEntity);
		entity.setPrecoTotal(itemPedido.getPrecoTotal());
		entity.setProduto(createProdutoEntity(itemPedido.getProduto()));
		entity.setQuantidade(itemPedido.getQuantidade());
		
		return entity;
	}
	
	private ClienteEntity createClienteEntity(Cliente cliente) {
		ClienteEntity entity = new ClienteEntity();
		
		entity.setCpf(cliente.getCpf());
		entity.setEmail(cliente.getEmail());
		entity.setId(cliente.getId());
		entity.setNome(cliente.getNome());
		
		return entity;
	}
	
	private ProdutoEntity createProdutoEntity(Produto produto) {
		ProdutoEntity entity = new ProdutoEntity();
		
		entity.setCategoria(createCategoriaEntity(produto.getCategoria()));
		entity.setDescricao(produto.getDescricao());
		entity.setId(produto.getId());
		entity.setImagem(produto.getImagem());
		entity.setNome(produto.getNome());
		entity.setPreco(produto.getPreco());
		
		return entity;
	}
	
	private CategoriaEntity createCategoriaEntity(Categoria categoria) {
		CategoriaEntity entity = new CategoriaEntity();
		
		entity.setId(categoria.getId());
		entity.setNome(categoria.getNome());
		
		return entity;
	}
	
	private PedidoEntity createPedidoEntity(Pedido pedido) {
		PedidoEntity entity = new PedidoEntity();
		
		entity.setCliente(createClienteEntity(pedido.getCliente()));
		entity.setDataCancelamento(pedido.getDataCancelamento());
		entity.setDataFinalizacao(pedido.getDataFinalizacao());
		entity.setDataSolicitacao(pedido.getDataSolicitacao());
		entity.setId(pedido.getId());
		entity.setItens(createItemPedidoEntities(pedido.getItens()));
		entity.setPaymentId(pedido.getPaymentId());
		entity.setStatus(pedido.getStatus());
		entity.setValor(pedido.getValor());
		
		return entity;
	}
    
    @Test
	public void toModel() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
    	PedidoEntity entity = createPedidoEntity(pedido);
    	
    	when(mapper.map(entity, Pedido.class)).thenReturn(pedido);
    	assertEquals(api.toModel(entity), pedido);
	}
	
    @Test
	public void toCollectionModel() {
    	List<PedidoEntity> entities = new ArrayList<>();
    	
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
    	PedidoEntity entity = createPedidoEntity(pedido);
    	
    	entities.add(entity);
    	
    	when(mapper.map(entity, Pedido.class)).thenReturn(pedido);
    	assertTrue(api.toCollectionModel(entities).size() > 0);
	}
}
