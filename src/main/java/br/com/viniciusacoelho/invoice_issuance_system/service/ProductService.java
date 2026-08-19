package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(ProductDTO productDTO) {
        Product product = Product.builder()
                .code(productDTO.code())
                .name(productDTO.name())
                .description(productDTO.description())
                .quantity(productDTO.quantity())
                .balance(productDTO.balance())
                .build();
        return productRepository.save(product);
    }

    public List<Product> read() {
        if (isProduct()) {
            return productRepository.findAll();
        }
        return null;
    }

    public Product update(Long id, ProductUpdateDTO productUpdateDTO) {
        Product product = findById(id);
        product.setCode(productUpdateDTO.code());
        product.setName(productUpdateDTO.name());
        product.setDescription(productUpdateDTO.description());
        product.setQuantity(productUpdateDTO.quantity());
        product.setBalance(productUpdateDTO.balance());
        return productRepository.save(product);
    }

    public Product delete(Long id) {
        hasProduct(id);
        productRepository.deleteById(id);
        return findById(id);
    }

    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Product.class.getName()));
    }

    private void hasProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException(Product.class.getName());
        }
    }

    private boolean isProduct() {
        return productRepository.count() > 0;
    }

}
