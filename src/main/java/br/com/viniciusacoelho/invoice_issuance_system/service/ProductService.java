package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
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
                .code(createCode())
                .name(productDTO.name())
                .description(productDTO.description())
                .stock(productDTO.stock())
                .build();
        return productRepository.save(product);
    }

    public List<Product> read() {
        if (isProduct()) {
            return productRepository.findAll();
        }
        throw new NotFoundException("Produtos");
    }

    // TODO: Check why it is saving with null values.
    public Product update(Long id, ProductUpdateDTO productUpdateDTO) {
        Product product = findById(id);
        product.setName(productUpdateDTO.name());
        product.setDescription(productUpdateDTO.description());
        product.setStock(productUpdateDTO.stock());
        return productRepository.save(product);
    }

    public Product delete(Long id) {
        hasProduct(id);
        productRepository.deleteById(id);
        return null;
    }

    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto"));
    }

    private void hasProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Produto");
        }
    }

    private boolean isProduct() {
        return productRepository.count() > 0;
    }

    public void addStock(Long id, Integer quantity) {
        Product product = findById(id);
        if (isStockValid(product.getStock(), quantity)) {
            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
            return;
        }
        throw new BadRequestException("Estoque");
    }

    public void removeStock(Long id, Integer quantity) {
        Product product = findById(id);
        if (isStockValid(product.getStock(), quantity)) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return;
        }
        throw new BadRequestException("Estoque");
    }

    private static boolean isStockValid(Integer stock, Integer quantity) {
        return true; // TODO: Make some validation
    }

    // TODO: Make it more clean
    private String createCode() {
        long id = productRepository.count() + 1;
        if (id < 10) {
            return "00000" + id;
        } else if (id < 100) {
            return "0000" + id;
        } else if (id < 1000) {
            return "000" + id;
        } else if (id < 10000) {
            return "00" + id;
        } else if (id < 100000) {
            return "0" + id;
        } else {
            return "" + id;
        }
    }

}
