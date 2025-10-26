package com.ohgiraffers.springdatajpa.chap01.section02.controller;

import com.ohgiraffers.springdatajpa.chap01.section02.service.ProductService;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/*
 * =====================================================================================
 * 🎮 Section 02: Controller Layer 구현
 * =====================================================================================
 */
/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. Controller Layer (표현 계층)
 * - 애플리케이션의 '관문'으로, 사용자의 HTTP 요청을 '최초로' 수신하는 진입점입니다.
 * - @GetMapping, @PostMapping 등의 '매핑'을 통해 특정 URL 요청을 처리할 메서드를 연결합니다.
 *
 * 2. 핵심 역할: 조율 및 위임
 * - (1) 요청 처리: 요청 파라미터(@RequestParam), 경로 변수(@PathVariable),
 * 요청 본문(@RequestBody) 등을 자바 객체로 변환(Parsing/Binding)합니다.
 * - (2) 서비스 호출: 실제 비즈니스 로직을 처리하는 'Service Layer'의 적절한 메서드를 호출합니다.
 * - (3) 응답 반환: 서비스의 처리 결과를 클라이언트에게 HTTP 응답(주로 JSON)으로
 * 변환(Serialization)하여 반환합니다.
 *
 * 3. 관심사의 분리 (SoC)
 * - 컨트롤러는 '비즈니스 로직'이나 '데이터 접근 로직'을 절대 직접 수행하지 않습니다.
 * - 컨트롤러는 '조율자(Orchestrator)'이며, 모든 실제 작업은 'Service'에게 위임합니다.
 *
 * 4. @Controller vs @RestController
 * - @Controller: (전통적) 주로 View(HTML 페이지)를 반환할 때 사용.
 * 메서드 반환 값은 '뷰 이름(String)'이 됩니다.
 * - @RestController: (현대 API) `@Controller` + `@ResponseBody`.
 * 주로 데이터(JSON/XML) 자체를 반환하는 'REST API' 서버에 사용됩니다.
 * 메서드 반환 값(객체)이 '응답 본문(Body)'에 직렬화되어 들어갑니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. 사용자의 HTTP 요청을 받는 레이어 (애플리케이션의 '관문')
 * - [아키텍처] 3계층 아키텍처(Presentation - Business - Data)에서
 * 'Presentation Layer(표현 계층)'를 담당합니다.
 *
 * - [비유] 컨트롤러는 레스토랑의 '매니저(Maître d')'와 같습니다.
 * 1. (요청 수신) 고객(Client)이 "1번 테이블, 스테이크 2개 주세요" (HTTP POST /orders)라고 요청합니다.
 * 2. (요청 번역) 매니저(Controller)는 이 요청을 주방(Service)이 알아들을 수 있는 '주문서(DTO)'로 변환합니다. (`@RequestBody OrderDto dto`)
 * 3. (위임) 매니저(Controller)는 직접 요리(비즈니스 로직)하지 않습니다.
 * 주방장(Service)에게 주문서를 전달합니다. (`orderService.placeOrder(dto)`)
 * 4. (응답) 요리가 완료되면(Service가 객체를 반환하면), 매니저(Controller)는
 * "주문이 완료되었습니다"(HTTP 200 OK + JSON 응답)라고 고객에게 최종 응답합니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 요청 파라미터 처리, 응답 데이터 형식 변환
 *
 * 컨트롤러의 핵심 기술적 책임은 '데이터 변환'입니다.
 *
 * - (1) 요청 데이터 변환 (HTTP Request -> Java Object)
 * 클라이언트가 보낸 원시(Raw) 데이터를 자바 메서드가 사용할 수 있는 객체로 변환합니다.
 * - @PathVariable: URL 경로의 일부를 변수로 받습니다. (예: /users/10 -> `Long id`)
 * - @RequestParam: URL 쿼리 파라미터를 변수로 받습니다. (예: /users?name=kim -> `String name`)
 * - @RequestBody: HTTP Body에 담긴 JSON/XML 데이터를 '자바 객체(DTO)'로 자동 변환(역직렬화)합니다.
 *
 * - (2) 응답 데이터 변환 (Java Object -> HTTP Response)
 * 서비스가 반환한 '자바 객체'를 클라이언트(브라우저, 모바일 앱)가 이해할 수 있는
 * 'JSON' 형태의 문자열로 변환(직렬화)하여 HTTP 응답 본문에 실어 보냅니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. 비즈니스 로직을 Service 계층에 위임 (관심사의 분리)
 * 이것은 컨트롤러 설계의 *가장 중요한 원칙*입니다.
 *
 * - [잘못된 예 (Bad)] 컨트롤러 메서드 내부에 `if`문을 사용한 로직 처리나
 * `repository.save()` 같은 코드가 직접 포함된 경우입니다.
 *
 * void createUser(@RequestBody UserDto dto) {
 * if (dto.getAge() < 19) { // (X) 이건 비즈니스 로직
 * throw new Exception("미성년자 가입 불가");
 * }
 * User user = new User(dto.getName(), ...);
 * userRepository.save(user); // (X) 이건 데이터 접근 로직
 * }
 *
 * - [올바른 예 (Good)] 컨트롤러는 요청을 변환하고 서비스에 위임할 뿐, '아무것도' 하지 않습니다.
 *
 * ResponseEntity<User> createUser(@RequestBody UserDto dto) {
 * // 비즈니스 로직(나이 검증)과 데이터 저장(save)은 모두 서비스가 담당
 * User savedUser = userService.join(dto);
 * return ResponseEntity.ok(savedUser); // 반환 값 변환만 처리
 * }
 *
 * - [이유]
 * - (1) 재사용성: `userService.join()` 로직은 나중에 관리자(Admin) 컨트롤러에서도 재사용할 수 있습니다.
 * - (2) 테스트 용이성: 비즈니스 로직(Service)과 HTTP 처리(Controller)를 분리하여
 * 각각 독립적으로 테스트할 수 있습니다.
 * - (3) 유지보수성: "미성년자 기준이 19세에서 20세로 변경"될 때,
 * Service 레이어만 수정하면 되므로 변경 지점이 명확해집니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. @Controller vs @RestController (매우 중요한 차이)
 *
 * - (1) @Controller (전통적인 방식, Server-Side Rendering)
 * - 목적: HTML '페이지'를 반환하기 위해 사용합니다. (JSP, Thymeleaf)
 * - 동작: 메서드가 "home"이라는 `String`을 반환하면,
 * 스프링의 'ViewResolver'가 `templates/home.html` 같은 뷰 파일을 찾아
 * HTML로 '렌더링'한 후, 완성된 HTML '페이지'를 HTTP 응답으로 보냅니다.
 *
 * - (2) @RestController (현대적인 API 방식, Client-Side Rendering)
 * - 목적: JSON/XML 같은 '데이터'를 반환하기 위해 사용합니다. (React/Vue/Mobile App 연동)
 * - 구성: `@Controller` + `@ResponseBody`의 조합입니다.
 * - `@ResponseBody`의 역할:
 * "메서드 반환 값을 뷰(View)로 해석하지 말고,
 * HTTP 응답 본문(Body)에 '직접' 써넣어라."
 * - 동작: 메서드가 `User` 객체를 반환하면,
 * 스프링은 이 객체를 'JSON 문자열'로 자동 변환(직렬화)하여
 * HTTP 응답 본문에 담아 클라이언트에게 전송합니다.
 * 클라이언트(예: React)는 이 JSON 데이터를 받아 화면을 '직접' 그립니다.
 */
@RestController // (또는 @RestController) Spring MVC 컨트롤러
@RequestMapping("/chap01/section02/")
public class ProductController {

    private final ProductService productService; // Service 계층 의존

    // 생성자 주입 (@Autowired 생략 가능)
    @Autowired
    public ProductController(ProductService productService) {
        System.out.println("ProductController 생성: ProductService 주입됨");
        this.productService = productService;
    }

    // GET /products/cheap?maxPrice=10000 요청 처리 핸들러
    @GetMapping("/products/cheap")
    @ResponseBody // 결과를 JSON으로 반환
    public List<String> findCheapProductNames(@RequestParam("maxPrice") Integer maxPrice) {
        System.out.println("\nController - findCheapProductNames 요청 수신: maxPrice = " + maxPrice);
        // 1. Service 호출하여 비즈니스 로직 수행 요청 (Service는 가격 이하 Product 목록 반환)
        List<Product> cheapProducts = productService.findProductsCheaperThan(maxPrice);

        // 2. 결과 가공 (응답 데이터 형태로 변환 - 상품명 목록)
        List<String> productNames = cheapProducts.stream()
                .map(Product::getProductName)
                .collect(Collectors.toList());
        System.out.println("Controller - 응답할 상품명 목록: " + productNames);

        // 3. 결과 반환 (@ResponseBody가 JSON으로 변환)
        return productNames;
    }

    // GET /products/{productId} 요청 처리 핸들러
    @GetMapping("/products/{productId}") // URL 경로의 일부를 변수({productId})로 사용
    @ResponseBody
    public Product findProductById(@PathVariable("productId") Integer productId) { // @PathVariable로 경로 변수 값 받기
        System.out.println("\nController - findProductById 요청 수신: productId = " + productId);
        // 1. Service 호출
        Product product = productService.findProductById(productId);
        System.out.println("Controller - 응답할 상품 정보: " + product);
        // 2. 결과 반환 (Product 객체가 JSON으로 변환됨)
        return product;
    }
}