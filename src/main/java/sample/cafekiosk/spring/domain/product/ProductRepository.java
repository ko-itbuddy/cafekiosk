package sample.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long > {

    /**
     * select *
     * from product
     * where selling_status in ('SELLING', 'HOLD')
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> productSellingStatuses);


    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query("select p.productNumber from Product  p order by p.id desc limit 1")
    String findLatestProductNumber();
}
