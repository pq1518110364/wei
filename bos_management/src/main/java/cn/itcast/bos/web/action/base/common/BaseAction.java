package cn.itcast.bos.web.action.base.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport  implements ModelDriven<T>{

	protected T model;
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	//构造器构建子类的action对象获取父类的泛型
	public BaseAction() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		// 获取类型第一个泛型参数
				ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
				Class<T> modelClass = (Class<T>) parameterizedType
						.getActualTypeArguments()[0];
				try {
					model = modelClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					System.out.println("模型构造失败...");
				}
	}

	
	
	// 接收分页查询参数
		protected int page;
		protected int rows;

		public void setPage(int page) {
			this.page = page;
		}

		public void setRows(int rows) {
			this.rows = rows;
		}
		
		// 将分页查询结果数据，压入值栈的方法
		protected void pushPageDataToValueStack(Page<T> pageData) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", pageData.getTotalElements());
			result.put("rows", pageData.getContent());

			ActionContext.getContext().getValueStack().push(result);
		}
}
