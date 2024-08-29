package com.example.crud.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getProducts() {
        return this.productRepository.findAll();
    };

    public ResponseEntity<Object> newProduct(Product product) throws IllegalAccessException {
        Optional<Product> res = this.productRepository.findProductByName(product.getName());
        HashMap<String, Object> datos = new HashMap<>();
        if(res.isPresent() && product.getId() == null) {
            datos.put("error", true);
            datos.put("message", "Ya exite un producto con ese nombre");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        datos.put("message", "Se guardó con exitó");
        // VAMOS A ACTUALIZAR
        if (product.getId() != null) {
            datos.put("message", "Se actualizo con exitó");
        }
        productRepository.save(product);
        datos.put("data", product);
        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Object> removeProduct(Long id) throws IllegalAccessException {
        Optional<Product> existingProduct = this.productRepository.findById(id);
        HashMap<String, Object> datos = new HashMap<>();
        if(existingProduct.isEmpty()) {
            datos.put("error", true);
            datos.put("message", "No se encontro producto por ese id");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        productRepository.deleteById(id);
        datos.put("success", true);
        datos.put("message", "Se ha eliminado con éxito");
        return new ResponseEntity<>(
                datos,
                HttpStatus.ACCEPTED
        );
    }

}
