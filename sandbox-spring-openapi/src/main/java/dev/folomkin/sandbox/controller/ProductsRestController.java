package dev.folomkin.sandbox.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue/products")
public class ProductsRestController {

    public record ProductV1Presentation(String id, String title,
                                        String details) {
    }


    @GetMapping
    public ResponseEntity<List<ProductV1Presentation>> getProducts() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.eselpo.catalogue.products.v1+json"))
                .body(List.of(
                        new ProductV1Presentation("a396a088-172c-11ee-aa6f-4f6009552211",
                                "Молоко, 3,2%, 1 литр",
                                "Молоко с жирностью 3,2% в упаковке 1 литр"),
                        new ProductV1Presentation("a396a088-172c-11ee-aa6f-4f6009552212",
                                "Кефир, 3,2%, 0,5 литра",
                                "Кефир с жирностью 3,2% в упаковке 0,5 литра")
                ));
    }
}