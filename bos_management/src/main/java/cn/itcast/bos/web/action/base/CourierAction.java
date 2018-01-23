package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;

/**
 * 快递员 的action crud以及条件分页
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	private Courier courier = new Courier();

	@Override
	public Courier getModel() {

		return courier;
	}

	// 保存courier
	@Autowired
	private CourierService courierService;

	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String saveCourier() {
		courierService.saveCourier(courier);
		return SUCCESS;
	}

	private Integer page;
	private Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	// 简单的分页查询courier
	@Action(value = "courier_page", results = { @Result(name = "success", type = "json") })
	public String courierPage() {
		// 数据获取
		Pageable pageable = new PageRequest(page - 1, rows);
		// 条件获取及筛选 对象的分装
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// predicate作为条件对象存到集合
				List<Predicate> list = new ArrayList<Predicate>();
				// 对于简单表单条件判断
				/*if (StringUtils.isNotBlank(courier.getCourierNum())) {
					// 条件表达式的编写,建立 精确条件判断
					Predicate pl = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(pl);
				}*/
				// 模糊条件判断
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p2 = cb.like(root.get("courierNum").as(String.class),
							"%" + courier.getCourierNum() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p3 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p3);
				}
				/*//精确查询
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p4 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p4);
				}*/

				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p5 = cb.like(root.get("type").as(String.class), "%" + courier.getType() + "%");
					list.add(p5);
				}
				
				
				//多表条件查询
				Join<Courier, Standard> jion= root.join("standard", JoinType.INNER);
				//判断
				if (courier.getStandard() != null
						&& StringUtils.isNotBlank(courier.getStandard()
								.getName())) {
					Predicate p6 = cb.like(
							jion.get("name").as(String.class), "%"
									+ courier.getStandard().getName() + "%");
					list.add(p6);
				}
			System.out.println(list);
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Page<Courier> pageResult = courierService.findCourierPage(specification,pageable);
		// 数据筛选
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageResult.getTotalElements());
		map.put("rows", pageResult.getContent());
		System.out.println(map);
		// 数据返回
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	//批量删除的
	@Action(value = "courier_delete", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String delete_courier() {
		courierService.delete_courier(ids);
		return SUCCESS;
	}
	
	//批量还原的
		@Action(value = "courier_restore", results = {
				@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
		public String restore_courier() {
			courierService.restore_courier(ids);
			return SUCCESS;
		}
	
}
