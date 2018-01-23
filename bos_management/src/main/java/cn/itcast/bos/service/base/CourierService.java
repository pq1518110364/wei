package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {
	public void saveCourier(Courier courier);

	public Page<Courier> findCourierPage(Specification<Courier> specification,Pageable pageable);

	public void delete_courier(String ids);

	public void restore_courier(String ids);
}
