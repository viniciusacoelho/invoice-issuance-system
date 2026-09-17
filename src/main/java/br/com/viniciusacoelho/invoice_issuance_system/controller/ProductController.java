package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.create(productDTO));
    }

    @GetMapping
    public ResponseEntity<List<Product>> read() {
        return ResponseEntity.ok(productService.read());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        return ResponseEntity.ok(productService.update(id, productUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.delete(id));
    }

    @GetMapping("/filter/name={name}")
    public ResponseEntity<List<Product>> filterByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/filter/category={category}")
    public ResponseEntity<List<Product>> filterByCategory(@PathVariable("category") String category) {
        return ResponseEntity.ok(productService.filterByCategory(category));
    }

    @GetMapping("/filter/name={name}/priceAsc")
    public ResponseEntity<List<Product>> filterNameByPriceAsc(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.filterNameByPriceAsc(name));
    }

    @GetMapping("/filter/name={name}/priceDesc")
    public ResponseEntity<List<Product>> filterNameByPriceDesc(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.filterNameByPriceDesc(name));
    }

    @GetMapping("/filter/name={name}/stockAsc")
    public ResponseEntity<List<Product>> filterNameByStockAsc(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.filterNameByStockAsc(name));
    }

    @GetMapping("/filter/name={name}/stockDesc")
    public ResponseEntity<List<Product>> filterNameByStockDesc(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.filterNameByStockDesc(name));
    }

    @GetMapping("/filter/category={category}/priceAsc")
    public ResponseEntity<List<Product>> filterCategoryByPriceAsc(@PathVariable("category") String category) {
        return ResponseEntity.ok(productService.filterCategoryByPriceAsc(category));
    }

    @GetMapping("/filter/category={category}/priceDesc")
    public ResponseEntity<List<Product>> filterCategoryByPriceDesc(@PathVariable("category") String category) {
        return ResponseEntity.ok(productService.filterCategoryByPriceDesc(category));
    }

    @GetMapping("/filter/category={category}/stockAsc")
    public ResponseEntity<List<Product>> filterCategoryByStockAsc(@PathVariable("category") String category) {
        return ResponseEntity.ok(productService.filterCategoryByStockAsc(category));
    }

    @GetMapping("/filter/category={category}/stockDesc")
    public ResponseEntity<List<Product>> filterCategoryByStockDesc(@PathVariable("category") String category) {
        return ResponseEntity.ok(productService.filterCategoryByStockDesc(category));
    }

}
