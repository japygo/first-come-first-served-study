package com.example.api.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@SpringBootTest
class KafkaApplyServiceTest {

    @Autowired
    private KafkaApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @BeforeEach
    void setUp() {
        couponCountRepository.flushAll();
    }

    @Test
    void 여러명응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            try {
                executorService.execute(() -> {
                    applyService.apply(userId);
                });
            } finally {
                latch.countDown();
            }
        }

        latch.await();

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);
    }

}
