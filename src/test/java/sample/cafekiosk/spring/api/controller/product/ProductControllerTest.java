package sample.cafekiosk.spring.api.controller.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.reqeust.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest requset = ProductCreateRequest.builder()
                                                           .type(ProductType.HANDMADE)
                                                           .sellingStatus(
                                                               ProductSellingStatus.SELLING)
                                                           .name("아메리카노")
                                                           .price(4000)
                                                           .build();
        // when // then
        mockMvc.perform(
                   post("/api/v1/products/new")
                       .content(objectMapper.writeValueAsString(requset))
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isOk());

    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수 값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest requset = ProductCreateRequest.builder()
                                                           .sellingStatus(
                                                               ProductSellingStatus.SELLING)
                                                           .name("아메리카노")
                                                           .price(4000)
                                                           .build();
        // when // then
        mockMvc.perform(
                   post("/api/v1/products/new")
                       .content(objectMapper.writeValueAsString(requset))
                       .contentType(MediaType.APPLICATION_JSON)
               ).andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        ;

    }

    @DisplayName("신규 상품을 등록할 때 상품의 판매상태는 필수 값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest requset = ProductCreateRequest.builder()
                                                           .type(ProductType.HANDMADE)
                                                           .name("아메리카노")
                                                           .price(4000)
                                                           .build();
        // when // then
        mockMvc.perform(
                   post("/api/v1/products/new")
                       .content(objectMapper.writeValueAsString(requset))
                       .contentType(MediaType.APPLICATION_JSON)
               ).andDo(print())
               .andExpect(status().isBadRequest());

    }

    @DisplayName("신규 상품을 등록할 때 이름은 필수 값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest requset = ProductCreateRequest.builder()
                                                           .type(ProductType.HANDMADE)
                                                           .sellingStatus(
                                                               ProductSellingStatus.SELLING)
                                                           .price(4000)
                                                           .build();
        // when // then
        mockMvc.perform(
                   post("/api/v1/products/new")
                       .content(objectMapper.writeValueAsString(requset))
                       .contentType(MediaType.APPLICATION_JSON)
               ).andDo(print())
               .andExpect(status().isBadRequest());

    }

    @DisplayName("신규 상품을 등록할 때 가격은 양수이다.")
    @Test
    void createProductWithNegativePrice() throws Exception {
        // given
        ProductCreateRequest requset = ProductCreateRequest.builder()
                                                           .type(ProductType.HANDMADE)
                                                           .sellingStatus(
                                                               ProductSellingStatus.SELLING)
                                                           .name("아메리카노")
                                                           .price(-4000)
                                                           .build();
        // when // then
        mockMvc.perform(
                   post("/api/v1/products/new")
                       .content(objectMapper.writeValueAsString(requset))
                       .contentType(MediaType.APPLICATION_JSON)
               ).andDo(print())
               .andExpect(status().isBadRequest());

    }

}