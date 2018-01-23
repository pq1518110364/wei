package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {

	@Autowired
	private AreaService areaService;

	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	@Action(value = "area_batchImport")
	public String area_batchImport() throws Exception {
		List<Area> list = new ArrayList<>();
		// 加载excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 获取第一个sheet对象
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		// 遍历得到每一行数据 sheet相当于整个文本对象 遍历得到每一行数据 在得到每一个单元格
		for (Row row : sheet) {
			// 跳过第一行表头数据
			if (row.getRowNum() == 0) {
				continue;
			}
			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 城市简拼
			String province = area.getProvince().substring(0, area.getProvince().length() - 1);
			String city = area.getCity().substring(0, area.getCity().length() - 1);
			String district = area.getDistrict().substring(0, area.getDistrict().length() - 1);
			String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
			StringBuffer buffer = new StringBuffer();
			for (String haed : headArray) {
				buffer.append(haed);
			}
			String code = buffer.toString();
			area.setShortcode(code);
			String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(cityCode);
			list.add(area);

		}
		areaService.save(list);
		return NONE;
	}

	// 编写条件分页的代码
	@Action(value = "Area_Page", results = { @Result(name = "success", type = "json") })
	public String pageArea() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> listpredicate = new ArrayList<>();
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p1 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
					listpredicate.add(p1);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p2 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					listpredicate.add(p2);
				}
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p3 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					listpredicate.add(p3);
				}
				
				return cb.and(listpredicate.toArray(new Predicate[0]));
			}
		};
				// 调用业务层完成查询
				Page<Area> pageData = areaService.findPageData(specification, pageable);
				// 压入值栈
				pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
