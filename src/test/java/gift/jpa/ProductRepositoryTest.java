package gift.jpa;

import gift.product.Product;
import gift.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void save(){
        Product expected = new Product("상품1",10000L, "상품1.jpg");
        Product actual = productRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }
    @Test
    void findByID(){
        Product expected = new Product("상품1",10000L, "상품1.jpg");
        productRepository.save(expected);

        Product actual = productRepository.findById(expected.getId()).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    @Transactional
    void update(){
        Product expected = new Product("상품1",10000L, "상품1.jpg");
        productRepository.save(expected);
        Optional<Product> product = productRepository.findById(expected.getId());
        product.ifPresent(product1 -> {
                product1.setName("상품2");
                product1.setPrice(20000L);
                product1.setName("상품2.jpg");
        }
        );
        Product actual = productRepository.findById(expected.getId()).orElseThrow();

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }
    @Test
    void deleteByID(){
        Product product = new Product("상품1", 10000L, "상품1.jpg");
        productRepository.save(product);
        productRepository.deleteById(product.getId());
        Optional<Product> actual = productRepository.findById(product.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}