package com.ohgiraffers.springdatajpa.chap05.service; // chap05 Service 패키지

import com.ohgiraffers.springdatajpa.chap02.section01.repository.ProductRepository; // Chap02 Repository 재사용
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation; // ✅ Isolation Enum 임포트
import org.springframework.transaction.annotation.Transactional;

/*
 * =====================================================================================
 * 🚦 Section 03: Service에서 트랜잭션 격리 수준 설정하기
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. 트랜잭션 격리 수준 (Isolation Level)이란?
 * - 여러 트랜잭션이 '동시에' 실행될 때,
 * 서로의 작업 내용에 '얼마나 노출'될지를 결정하는 '안전 장치' 레벨입니다.
 *
 * 2. 왜 필요한가?
 * - '동시성(Concurrency)'과 '데이터 일관성(Consistency)'은 '트레이드오프(Trade-off)' 관계입니다.
 * - [낮은 격리 수준]: 동시성은 높지만(빠름), 데이터 일관성이 깨질 수 있습니다. (읽기 오류 발생)
 * - [높은 격리 수준]: 데이터 일관성은 높지만(안전), 동시성이 낮아집니다. (느림, 락 발생)
 *
 * 3. Spring에서 설정 방법
 * - @Transactional 어노테이션의 'isolation' 속성을 사용합니다.
 * - 예: @Transactional(isolation = Isolation.READ_COMMITTED)
 *
 * 4. 격리 수준의 종류 (Spring Enum 기준)
 * - Isolation.READ_UNCOMMITTED (Level 0): 커밋되지 않은 데이터도 읽음 (매우 위험)
 * - Isolation.READ_COMMITTED (Level 1): '커밋된' 데이터만 읽음 (대부분의 DB 기본값)
 * - Isolation.REPEATABLE_READ (Level 2): 트랜잭션 내내 '반복 읽기' 보장 (MySQL 기본값)
 * - Isolation.SERIALIZABLE (Level 3): 트랜잭션을 '순차적'으로 실행 (가장 안전, 가장 느림)
 *
 * 5. Isolation.DEFAULT
 * - "나는 Spring에게 맡기지 않고, 연결된 'DB의 기본 설정'을 그대로 따르겠다"는 의미입니다.
 * - (참고) Oracle, PostgreSQL 등: READ_COMMITTED
 * - (참고) MySQL(InnoDB) : REPEATABLE_READ
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. ❓ 왜 '격리 수준'이 필요한가? (동시성 문제의 발생)
 *
 * 트랜잭션은 'ACID' 원칙을 따라야 합니다. 그중 'I'가 Isolation(고립성)입니다.
 * 만약 격리성이 완벽하지 않다면(낮다면), 여러 트랜잭션이 동시에 실행될 때
 * 다음과 같은 3가지 '읽기 이상 현상(Read Anomaly)'이 발생합니다.
 *
 * - (1) Dirty Read (더티 리드) - "취소된 주문을 읽다"
 * - [문제] 트랜잭션 A가 데이터를 수정(예: 재고 100 -> 90)하고 아직 '커밋(Commit)하지 않았는데',
 * 트랜잭션 B가 이 '90'이라는 임시 데이터를 읽어가는 현상입니다.
 * - [위험] 만약 A가 '롤백(Rollback)'하면, B는 '존재하지 않는' 90이라는 데이터를 기반으로
 * 로직을 처리하게 되어 데이터가 심각하게 꼬입니다. (가장 위험한 현상)
 *
 * - (2) Non-Repeatable Read (반복 불가능한 읽기) - "읽는 순간 데이터가 바뀌다"
 * - [문제] 트랜잭션 A가 '1번 상품'의 재고(100)를 읽었습니다.
 * 그 직후, 트랜잭션 B가 '1번 상품'의 재고를 90으로 '수정하고 커밋'했습니다.
 * 잠시 후 A가 '다시' '1번 상품'의 재고를 읽었더니, 이번에는 '90'이 조회됩니다.
 * - [위험] 하나의 트랜잭션('A') 내에서 '같은 데이터(row)'를 읽었는데도
 * 결과가 달라져, '일관성'이 깨지게 됩니다. (예: 조회와 동시에 수정 로직이 도는 경우)
 *
 * - (3) Phantom Read (팬텀 리드) - "없던 데이터가 생겨나다"
 * - [문제] 트랜잭션 A가 "재고가 100개 이상인 상품"을 조회했더니 5건이 나왔습니다.
 * 그 직후, 트랜잭션 B가 "재고 110개짜리 새 상품"을 '추가(INSERT)하고 커밋'했습니다.
 * 잠시 후 A가 '같은 조건'("재고 100개 이상")으로 다시 조회했더니,
 * 이번에는 6건이 조회됩니다.
 * - [위험] A 입장에서는 '유령(Phantom)'처럼 없던 데이터가 생겨난 것입니다.
 * (Non-Repeatable이 'UPDATE' 문제라면, Phantom은 'INSERT/DELETE' 문제입니다.)
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 🚦 4가지 표준 격리 수준 (Anomalies 해결책)
 *
 * 격리 수준은 위 3가지 문제를 '어디까지 막아줄 것인가'에 대한 '정책'입니다.
 *
 *
 * | 격리 수준 | Dirty Read | Non-Repeatable | Phantom Read | 동시성 |
 * | :--- | :---: | :---: | :---: | :---: |
 * | `READ_UNCOMMITTED` | O (발생) | O (발생) | O (발생) | 최상 |
 * | `READ_COMMITTED` | X (방지) | O (발생) | O (발생) | 상 |
 * | `REPEATABLE_READ` | X (방지) | X (방지) | O (발생) | 중 |
 * | `SERIALIZABLE` | X (방지) | X (방지) | X (방지) | 최하 |
 *
 * - (Level 0) `READ_UNCOMMITTED`
 * - '커밋되지 않은' 데이터도 읽습니다. Dirty Read를 허용합니다.
 * - 정합성이 전혀 보장되지 않아, 실제 운영 시스템에서는 '절대' 사용하지 않습니다.
 *
 * - (Level 1) `READ_COMMITTED` (가장 널리 사용되는 표준)
 * - '커밋된' 데이터만 읽습니다. (Dirty Read 방지)
 * - 'Read' 작업이 'Write' 작업을 막지 않습니다.
 * - Oracle, PostgreSQL, MS-SQL의 '기본(Default)' 격리 수준입니다.
 *
 * - (Level 2) `REPEATABLE_READ`
 * - '트랜잭션이 시작된 시점'의 데이터 버전을 '스냅샷'으로 만들어,
 * 트랜잭션이 끝날 때까지 해당 스냅샷만 읽습니다. (Non-Repeatable Read 방지)
 * - 따라서 트랜잭션 내내 '반복 읽기'가 보장됩니다.
 * - MySQL(InnoDB)의 '기본(Default)' 격리 수준입니다.
 *
 * - (Level 3) `SERIALIZABLE`
 * - 가장 강력한 수준. 'Read' 작업에도 'Lock(공유 락)'을 겁니다.
 * - 다른 트랜잭션은 해당 데이터를 읽거나/쓰거나/추가할 수 없게 됩니다.
 * - 마치 모든 트랜잭션이 '순서대로(Serial)' 실행되는 것처럼 동작합니다.
 * - Phantom Read까지 방지하지만, '데드락(Deadlock)' 가능성이 커지고
 * 동시성(성능)이 최악이므로, 꼭 필요한 곳(예: 금융 통계)이 아니면 사용하지 않습니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. 🚀 Spring 적용 가이드: "언제 격리 수준을 변경하는가?"
 *
 * - [원칙] '99%의 경우'는 `DEFAULT` (즉, DB 기본값)를 사용합니다.
 * `READ_COMMITTED`나 `REPEATABLE_READ` 수준이면 대부분의 웹 애플리케이션 로직은
 * 안전하게 처리할 수 있습니다.
 *
 * - [변경 시점]
 * '특정' 비즈니스 로직이 '특정' 동시성 문제에 민감할 때, '해당 서비스 메서드'에만
 * 격리 수준을 '상향' 조절합니다.
 *
 * - (예시) "오늘의 매출 통계를 내는 `getDailySalesReport()` 메서드"
 * - 이 메서드는 실행 시간이 5초 정도 걸린다고 가정합니다.
 * - 만약 기본값이 `READ_COMMITTED`라면, 5초 동안 통계를 집계하는 와중에
 * '새로운 주문(INSERT)'이 커밋되면 'Phantom Read'가 발생하여 통계가 정확하지 않게 됩니다.
 *
 * - [해결] 이 메서드에만 격리 수준을 상향합니다.
 *
 * @Transactional(
 * isolation = Isolation.SERIALIZABLE, // 또는 REPEATABLE_READ
 * readOnly = true
 * )
 * public ReportDto getDailySalesReport() {
 * // ... (팬텀 리드가 방지된 상태로 통계 집계)
 * }
 *
 * - [주의] 격리 수준을 `SERIALIZABLE`로 올리면 성능 저하(락 경합)가 발생하므로,
 * '꼭 필요한 메서드'에만 '최소한'으로 적용해야 합니다.
 */
@Service
public class IsolationTestService {

    private final ProductRepository productRepository;

    @Autowired
    public IsolationTestService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- 격리 수준 설정 예시 ---

    /**
     * 📌 Isolation.DEFAULT: DB 기본 격리 수준 사용 (가장 일반적)
     * - 대부분의 경우 이 설정으로 충분합니다.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public Product findProductDefault(Integer productId) {
        System.out.println("\nService(Isolation) - findProductDefault 호출 (Isolation=DEFAULT)");
        // DB의 기본 격리 수준(e.g., MySQL의 REPEATABLE_READ)으로 조회 수행
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));
    }

    /**
     * 📌 Isolation.READ_COMMITTED: Dirty Read 방지
     * - 다른 트랜잭션이 커밋한 데이터만 읽습니다.
     * - Oracle, SQL Server 등의 기본 격리 수준입니다.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Product findProductReadCommitted(Integer productId) {
        System.out.println("\nService(Isolation) - findProductReadCommitted 호출 (Isolation=READ_COMMITTED)");
        // 커밋된 데이터만 읽도록 보장 (Dirty Read 방지)
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));
    }

    /**
     * 📌 Isolation.REPEATABLE_READ: 반복 읽기 시 동일 결과 보장 (Non-Repeatable Read 방지)
     * - 한 트랜잭션 내에서 같은 데이터를 여러 번 읽어도 항상 같은 결과가 나옵니다.
     * - MySQL InnoDB의 기본 격리 수준입니다.
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Product findProductRepeatableRead(Integer productId) {
        System.out.println("\nService(Isolation) - findProductRepeatableRead 호출 (Isolation=REPEATABLE_READ)");
        // 트랜잭션 시작 시점의 데이터를 기준으로 읽기 (Non-Repeatable Read 방지)
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));
    }

    /**
     * 📌 Isolation.SERIALIZABLE: 가장 높은 격리 수준 (Phantom Read 방지)
     * - 트랜잭션을 순차적으로 실행하는 것처럼 동작시켜 동시성 문제를 원천 차단합니다.
     * - 동시 처리 성능이 크게 저하될 수 있으므로 매우 신중하게 사용해야 합니다! ⚠️
     * - 예: 중요한 재고 차감 로직, 금융 거래 처리 등
     */
    @Transactional(isolation = Isolation.SERIALIZABLE) // 데이터 변경 가능성 있으므로 readOnly=false
    public void processOrderWithSerializable(Integer productId, int quantityToDecrease) {
        System.out.println("\nService(Isolation) - processOrderWithSerializable 호출 (Isolation=SERIALIZABLE)");
        // SERIALIZABLE 수준에서 상품 조회 및 재고 변경 (가정)
        // 이 트랜잭션 동안 다른 트랜잭션은 해당 상품 데이터에 접근(읽기/쓰기)이 제한될 수 있음
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));

        System.out.println("  - 조회된 상품: " + product.getProductName());
        // (가정) 재고 차감 로직 ...
        System.out.println("  - (가정) 재고 차감 로직 수행...");
        // product.decreaseStock(quantityToDecrease); // 엔티티에 재고 관련 필드/메서드 있다고 가정
        // productRepository.save(product); // 변경 감지 또는 save 호출

        System.out.println("Service(Isolation) - processOrderWithSerializable 완료 (커밋 예정)");
    }

    // 💡 참고: Isolation.READ_UNCOMMITTED는 거의 사용되지 않으므로 예제에서 제외합니다.
}