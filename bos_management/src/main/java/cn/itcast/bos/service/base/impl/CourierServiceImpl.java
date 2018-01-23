package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierDao courierDao;

	@Override
	public void saveCourier(Courier courier) {
		courierDao.save(courier);
	}

	@Override
	public Page<Courier> findCourierPage(Specification<Courier> specification, Pageable pageable) {

		return courierDao.findAll(specification, pageable);
	}

	@Override
	public void delete_courier(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			courierDao.updateCourier(Integer.parseInt(id));
		}
	}

	@Override
	public void restore_courier(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			courierDao.restoreCourier(Integer.parseInt(id));
		}
	}

}
