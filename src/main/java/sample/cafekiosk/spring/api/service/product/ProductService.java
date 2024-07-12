package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.reqeust.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

/*
* 읽기 전용 transaction
* CRUD 에서 CUD 동작 X / only Read
* JPA : CUD 스냅샷 저장, 변경감지 X ( 성능 향상 효과 )
*
* CQRS - Command(CUD) / Query 분리하자
* Query 작업이 압도적으로 많다
* 서로 책임을 분리해서 서로 연관이 없도록 하자
* Query로 부하가 생겨서 Command에 에러 또 반대
* 조회용 서비스, 커맨드용 서비스 분리
*
* */

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;

    // 동시성 이슈
    // 1. productNumber 에 유니크를 걸고 3회정도 재시도 ( 빈도성이 낮아서 크리티컬 하지 않을때 )
    // 2. UUID로 만들면 오히려 좋음 (빈도성이 높을때, 크리티컬 할때)
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products  = productRepository.findAllBySellingStatusIn( ProductSellingStatus.forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String latestProduct = productRepository.findLatestProductNumber();
        if (latestProduct == null) return "001";

        int latestProductNumberInt = Integer.parseInt(latestProduct);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }
}
