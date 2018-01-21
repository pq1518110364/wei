package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

//保存收派标准的demo_save
@Controller
@Scope("prototype")
@Actions
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
	private Standard standard = new Standard();
	@Autowired
	private StandardService standardService;

	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String saveStandard() {
		standardService.saveStandard(standard);
		return SUCCESS;
	}

	// 接收页面参数 属性驱动接收
	private Integer page;
	private Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	@Action(value = "StandardPageAction", results = { @Result(name = "success", type = "json") })
	public String pageStandard() {
		// 对象封装
		Pageable pageable = new PageRequest(page - 1, rows);
		//方法调用
		Page<Standard> pageStandard = standardService.pageStandard(pageable);
		//数据处理
		Map<String,Object> map = new HashMap<>(); 
		map.put("total", pageStandard.getNumberOfElements());
		map.put("rows", pageStandard.getContent());
		//数据返回
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	//查询所有的分类标准
	@Action(value="standard_findAll",results= {@Result(name="success",type="json"),@Result(name="none",type="redirect",location="./page/base/standard.html")})
	public String findAll() {
		//获取所有对象
		List<Standard> list=standardService.findAll();
		if(list.size()==0) {
			return NONE;
		}else{
			ActionContext.getContext().getValueStack().push(list);
			return SUCCESS;
		}
	}
}
