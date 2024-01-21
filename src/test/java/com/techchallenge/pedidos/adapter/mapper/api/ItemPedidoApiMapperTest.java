package com.techchallenge.pedidos.adapter.mapper.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ItemPedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ItemPedidoInput;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemPedidoApiMapperTest {

	@InjectMocks
	private ItemPedidoApiMapper api;
	
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
	
	private ItemPedidoModel createItemPedidoModel(ItemPedido itemPedido) {
		ItemPedidoModel model = new ItemPedidoModel();
		
		model.setPrecoTotal(itemPedido.getPrecoTotal());
		model.setProduto(createProdutoModel(itemPedido.getProduto()));
		model.setQuantidade(itemPedido.getQuantidade());
		
		return model;
	}
	
	private ProdutoModel createProdutoModel(Produto produto) {
		ProdutoModel model = new ProdutoModel();
		
		model.setCategoria(createCategoriaResumoModel(produto.getCategoria()));
		model.setDescricao(produto.getDescricao());
		model.setId(produto.getId());
		model.setImagem(produto.getImagem());
		model.setNome(produto.getNome());
		model.setPreco(produto.getPreco());
	
		return model;
	}
	
	private CategoriaResumoModel createCategoriaResumoModel(Categoria categoria) {
		CategoriaResumoModel model = new CategoriaResumoModel();
		
		model.setNome(categoria.getNome());
		
		return model;
	}
	
	private ItemPedidoInput createItemPedidoInput(ItemPedido itemPedido) {
		ItemPedidoInput input = new ItemPedidoInput();
		
		input.setProdutoId(itemPedido.getProduto().getId());
		input.setQuantidade(itemPedido.getQuantidade());
		
		return input;
	}
    
    @Test
    public void toDomainObject() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoInput input = createItemPedidoInput(itemPedido);
    	
    	when(mapper.map(input, ItemPedido.class)).thenReturn(itemPedido);
        assertEquals(api.toDomainObject(input), itemPedido);
    }

    @Test
    public void toModel() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoModel model = createItemPedidoModel(itemPedido);
    	
    	when(mapper.map(itemPedido, ItemPedidoModel.class)).thenReturn(model);
    	assertEquals(api.toModel(itemPedido), model);
    }

    @Test
    public void toCollectionModel() {
    	List<ItemPedido> itens = new ArrayList<>();
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
		ItemPedidoModel model = createItemPedidoModel(itemPedido);
		
		itens.add(itemPedido);
		
		when(mapper.map(itemPedido, ItemPedidoModel.class)).thenReturn(model);
		
		assertTrue(api.toCollectionModel(itens).size() > 0);
    }
}