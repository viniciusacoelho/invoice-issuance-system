package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.ProductRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.request.ProductUpdateRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(ProductRequestDTO productRequestDTO) {
        Product product = Product.builder()
                .code(createCode())
                .name(productRequestDTO.name())
                .description(productRequestDTO.description())
                .price(productRequestDTO.price())
                .stock(productRequestDTO.stock())
                .category(productRequestDTO.category())
                .build();
        return productRepository.save(product);
    }

    public List<Product> read() {
        hasProducts();
        return productRepository.findAll();
    }

    // TODO: Check why it is saving with null values.
    public Product update(Long id, ProductUpdateRequestDTO productUpdateRequestDTO) {
        Product product = findById(id);
        product.setName(productUpdateRequestDTO.name());
        product.setDescription(productUpdateRequestDTO.description());
        product.setPrice(productUpdateRequestDTO.price());
        product.setStock(productUpdateRequestDTO.stock());
        product.setCategory(productUpdateRequestDTO.category());
        return productRepository.save(product);
    }

    public Product delete(Long id) {
        hasProductById(id);
        productRepository.deleteById(id);
        return null;
    }

    public List<Product> findByName(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        hasProducts(products);
        return products;
    }

    public List<Product> filterByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        hasProducts(products);
        return products;
    }

    public List<Product> filterNameByPriceAsc(String name) {
        List<Product> products = productRepository.findByNameContainingOrderByPriceAsc(name);
        hasProducts(products);
        return products;
    }

    public List<Product> filterNameByPriceDesc(String name) {
        List<Product> products = productRepository.findByNameContainingOrderByPriceDesc(name);
        hasProducts(products);
        return products;
    }

    public List<Product> filterNameByStockAsc(String name) {
        List<Product> products = productRepository.findByNameContainingOrderByStockAsc(name);
        hasProducts(products);
        return products;
    }

    public List<Product> filterNameByStockDesc(String name) {
        List<Product> products = productRepository.findByNameContainingOrderByStockDesc(name);
        hasProducts(products);
        return products;
    }

    public List<Product> filterCategoryByPriceAsc(String category) {
        List<Product> products = productRepository.findByCategoryContainingOrderByPriceAsc(category);
        hasProducts(products);
        return products;
    }

    public List<Product> filterCategoryByPriceDesc(String category) {
        List<Product> products = productRepository.findByCategoryContainingOrderByPriceDesc(category);
        hasProducts(products);
        return products;
    }

    public List<Product> filterCategoryByStockAsc(String category) {
        List<Product> products = productRepository.findByCategoryContainingOrderByStockAsc(category);
        hasProducts(products);
        return products;
    }

    public List<Product> filterCategoryByStockDesc(String category) {
        List<Product> products = productRepository.findByCategoryContainingOrderByStockDesc(category);
        hasProducts(products);
        return products;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto"));
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

    @Transactional
    public void removeStock(Long id, Integer quantity) {
        Product product = findByIdWithLock(id);
        if (isStockValid(product.getStock(), quantity)) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return;
        }
        throw new BadRequestException("Estoque");
    }

    public void hasProducts() {
        if (productRepository.count() == 0) {
            throw new NotFoundException("Produtos");
        }
    }

    private void hasProducts(List<Product> products) {
        if (products.isEmpty()) {
            throw new NotFoundException("Produto");
        }
    }

    private void hasProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Produto");
        }
    }

    private Product findByIdWithLock(Long id) {
        return productRepository.findByIdWithLock(id)
                .orElseThrow(() -> new NotFoundException("Produto"));
    }

    private static boolean isStockValid(Integer stock, Integer quantity) {
        return quantity > 0 && quantity <= stock;
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
