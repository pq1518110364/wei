package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.bos.domain.base.Standard;

public interface StandardDao extends JpaRepository<Standard, Integer> {
	@Query
	public List<Standard> queryName2(String name);
	@Query(value="update Standard set name=?2 where id =?1")
	@Modifying
	public void updateName(Integer id , String name);
}
