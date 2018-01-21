package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {
	public void saveStandard(Standard standard);
	public Page<Standard> pageStandard(Pageable pageable);
	public List<Standard> findAll();
}
