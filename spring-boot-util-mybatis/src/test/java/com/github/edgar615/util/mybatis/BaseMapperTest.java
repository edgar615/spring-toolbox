package com.github.edgar615.util.mybatis;

import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.db.Pagination;
import com.github.edgar615.util.search.Example;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleMybatisApplication.class)
public class BaseMapperTest {

  @Autowired
  private SysUserMapper sysUserMapper;

  @Test
  @Transactional
//  @Rollback(false)
  public void testInsert() {
    sysUserMapper.insert(new SysUser());
  }

  @Test
  @Transactional
  public void testInsertAndGeneratedKey() {
    SysUser sysUser = new SysUser();
    sysUser.setUsername(Randoms.randomAlphabetAndNum(10));
    sysUserMapper.insertAndGeneratedKey(sysUser);
    Assert.assertNotNull(sysUser.getSysUserId());
    System.out.println(sysUser.getSysUserId());
  }

  @Test
  @Transactional
  public void testDeleteById() {
    int result = sysUserMapper.deleteById(337l);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testFindById() {
    SysUser sysUser = sysUserMapper.findById(337l);
    System.out.println(sysUser);
    Assert.assertNotNull(sysUser);
    Assert.assertEquals(337l, sysUser.getSysUserId(), 0);

    sysUserMapper.findById(337l);
    System.out.println(sysUser);
  }

  @Test
  @Transactional
  public void testFindCustomFieldById() {
    SysUser sysUser = sysUserMapper.findCustomFieldById(337l, Lists.newArrayList());
    System.out.println(sysUser);
    Assert.assertNotNull(sysUser);
    Assert.assertEquals(337l, sysUser.getSysUserId(), 0);
    Assert.assertNotNull(sysUser.getCompanyCode());
    sysUser = sysUserMapper.findCustomFieldById(337l, Lists.newArrayList("sysUserId", "companyId"));
    System.out.println(sysUser);
    Assert.assertNull(sysUser.getCompanyCode());
  }

  @Test
  @Transactional
  public void testFindByExample() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .addField("sysUserId")
        .addField("addOn")
        .between("addOn", 1, 2).in("sysUserId", Lists.newArrayList(337l))
        .orderBy("sysUserId")
        .desc("addOn")
        .withDistinct();
    List<SysUser> sysUsers = sysUserMapper.findByExample(example);
    System.out.println(sysUsers);
  }

  @Test
  @Transactional
  public void testFindByExampleWithLimit() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    List<SysUser> sysUsers = sysUserMapper.findByExampleWithLimit(example, 0, 1);
    System.out.println(sysUsers);
  }

  @Test
  @Transactional
  public void testCountByExample() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    Integer result = sysUserMapper.countByExample(example);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testPagination() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    Pagination<SysUser> result = sysUserMapper.pagination(example, 1, 10);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testDeleteByExample() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    Integer result = sysUserMapper.deleteByExample(example);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testUpdateById() {
    SysUser sysUser = new SysUser();
    sysUser.setUsername("haha");
    Integer result = sysUserMapper.updateById(sysUser, 337l);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testUpdateByIdWithNull() {
    SysUser sysUser = new SysUser();
    sysUser.setUsername("haha");
    Map<String, Number> addOrSub = new HashMap<>();
    addOrSub.put("addOn", 1);
    addOrSub.put("state", -1);
    List<String> nullFields = new ArrayList<>();
    nullFields.add("companyCode");
    nullFields.add("companyId");
    Integer result = sysUserMapper.updateByIdWithNull(sysUser, addOrSub, nullFields, 337l);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testUpdateByExample() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    SysUser sysUser = new SysUser();
    sysUser.setUsername("haha");
    Integer result = sysUserMapper.updateByExample(sysUser, example);
    System.out.println(result);
  }

  @Test
  @Transactional
  public void testUpdateByExampleWithNull() {
    Example example = Example.create().equalsTo("sysUserId", 337l).startsWith("username", "1")
        .orderBy("sysUserId")
        .desc("addOn");
    SysUser sysUser = new SysUser();
    sysUser.setUsername("haha");
    Map<String, Number> addOrSub = new HashMap<>();
    addOrSub.put("addOn", 1);
    addOrSub.put("state", -1);
    List<String> nullFields = new ArrayList<>();
    nullFields.add("companyCode");
    nullFields.add("companyId");
    Integer result = sysUserMapper.updateByExampleWithNull(sysUser, addOrSub, nullFields, example);
    System.out.println(result);
  }
}
