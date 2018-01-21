package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardDao;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardDao standardDao;
	@Override//保存分类标准的方法
	public void saveStandard(Standard standard) {
		standardDao.save(standard);

	}
	//分页查询所有分类标准
	@Override
	public Page<Standard> pageStandard(Pageable pageable) {
		return standardDao.findAll(pageable);
	}
	//查询所有的分类标准
	@Override
	public List<Standard> findAll() {
		
		return standardDao.findAll();
	}
	
	

}
