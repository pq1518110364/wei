package cn.itheima.bos.test.repositorytest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class RepositoryTest {

	@Autowired
	private StandardDao standardDao;
	@Test
	public void test() {
		System.out.println(standardDao.count());
	}
	@Test
	public void test2() {
		System.out.println(standardDao.findAll());
	}
	@Test
	public void test3() {
		System.out.println(standardDao.queryName2("20-30kg"));
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test4() {
		standardDao.updateName(1, "10-20kg");
	}
}