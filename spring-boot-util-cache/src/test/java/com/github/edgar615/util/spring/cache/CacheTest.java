package com.github.edgar615.util.spring.cache;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

  @Autowired
  private CacheService cacheService;

  @Autowired
  private CacheManager cacheManager;

  @After
  public void tearDown() {
    cacheManager.getCache("caffeineCache1").clear();
    cacheManager.getCache("caffeineCache2").clear();
    cacheManager.getCache("l2Cache").clear();
    cacheService.clearStat();
  }

  @Test
  public void testCache1() throws InterruptedException {
    cacheService.getCache1(1);
    cacheService.getCache1(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache1(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache1(1);
    cacheService.getCache1(2);

    int count = cacheService.count("getCache1");
    Assert.assertEquals(3, count);
  }

  @Test
  public void testCache2() throws InterruptedException {
    cacheService.getCache2(1);
    cacheService.getCache2(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache2(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache2(1);
    cacheService.getCache2(2);

    int count = cacheService.count("getCache2");
    Assert.assertEquals(2, count);
  }

  @Test
  public void testL2Cache() throws InterruptedException {
    cacheService.l2Cache(1);
    cacheService.l2Cache(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.l2Cache(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.l2Cache(1);
    cacheService.l2Cache(2);

    int count = cacheService.count("l2Cache");
    Assert.assertEquals(2, count);
  }

  @Test
  public void testEvictCache() throws InterruptedException {
    cacheService.getCache1(1);
    cacheService.getCache1(1);
    cacheService.getCache1(2);
    cacheService.getCache1(2);
    cacheService.evictCache1(1);
    TimeUnit.SECONDS.sleep(1);
    cacheService.getCache1(1);
    cacheService.getCache1(2);

    int count = cacheService.count("getCache1");
    Assert.assertEquals(3, count);
  }

  @Test
  public void testClearCache() throws InterruptedException {
    cacheService.getCache1(1);
    cacheService.getCache1(1);
    cacheService.getCache1(2);
    cacheService.getCache1(2);
    cacheService.clearCache1();
    TimeUnit.SECONDS.sleep(1);
    cacheService.getCache1(1);
    cacheService.getCache1(2);

    int count = cacheService.count("getCache1");
    Assert.assertEquals(4, count);
  }

  @Test
  public void testEvictL2Cache() throws InterruptedException {
    L2Cache l2Cache = (L2Cache) cacheManager.getCache("l2Cache");
    int l1Missed = l2Cache.l1Missed();
    int l2Missed = l2Cache.l2Missed();
    cacheService.l2Cache(1);
    cacheService.l2Cache(1);
    cacheService.l2Cache(2);
    cacheService.l2Cache(2);
    cacheService.evictL2Cache(1);
    TimeUnit.SECONDS.sleep(1);
    cacheService.l2Cache(1);
    cacheService.l2Cache(2);

    int count = cacheService.count("l2Cache");
    Assert.assertEquals(2, count);

    Assert.assertEquals(3, l2Cache.l1Missed() - l1Missed);
    Assert.assertEquals(2, l2Cache.l2Missed() - l2Missed);
  }

  @Test
  public void testClearL2Cache() throws InterruptedException {
    L2Cache l2Cache = (L2Cache) cacheManager.getCache("l2Cache");
    int l1Missed = l2Cache.l1Missed();
    int l2Missed = l2Cache.l2Missed();
    cacheService.l2Cache(1);
    cacheService.l2Cache(1);
    cacheService.l2Cache(2);
    cacheService.l2Cache(2);
    cacheService.clearCache1();
    TimeUnit.SECONDS.sleep(1);
    cacheService.l2Cache(1);
    cacheService.l2Cache(2);

    int count = cacheService.count("l2Cache");
    Assert.assertEquals(2, count);
    Assert.assertEquals(4, l2Cache.l1Missed() - l1Missed);
    Assert.assertEquals(2, l2Cache.l2Missed() - l2Missed);
  }

  @Test
  public void testNoOpCache() throws InterruptedException {
    cacheService.noOp(1);
    cacheService.noOp(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.noOp(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.noOp(1);
    cacheService.noOp(2);

    int count = cacheService.count("noOpCache");
    Assert.assertEquals(5, count);
  }
}
