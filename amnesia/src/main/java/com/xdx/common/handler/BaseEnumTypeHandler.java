package com.xdx.common.handler;

import com.xdx.common.enums.IBaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 1、枚举处理器，不配置会出现  IllegalArgumentException: No enum constant ....  异常
 * 2、yml 文件也需要配置
 *  mybatis-plus:
 *      type-handlers-package:com.xdx97.framework.handler          # 处理器配置
 *
 * @author 小道仙
 * @date 2020年2月20日
 */
public class BaseEnumTypeHandler<T extends Enum<?> & IBaseEnum> extends BaseTypeHandler<IBaseEnum> {

	private Class<T> type;

	private final T[] enums;
    public BaseEnumTypeHandler() {
        enums = null;
    }

		/**
         * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
         *
         * @param type
         *            配置文件中设置的转换类
         */
	public BaseEnumTypeHandler(Class<T> type) {
		if (type == null){
			throw new IllegalArgumentException("【BaseEnumTypeHandler】Type argument cannot be null");
		}
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null){
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
		}

	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// 根据数据库存储类型决定获取类型
		String i = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位Enum子类
			return locateEnumStatus(i);
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型
		int i = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位子类
			return locateEnumStatus(i);
		}
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型
		int i = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位EnumStatus子类
			return locateEnumStatus(i);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, IBaseEnum parameter, JdbcType jdbcType)
			throws SQLException {
		JdbcType type = null;
		if (parameter.getCode() instanceof Integer){
            type = JdbcType.TINYINT;
        }
		if (parameter.getCode() instanceof String){
            type = JdbcType.VARCHAR;
        }
		// baseTypeHandler已经帮我们做了parameter的null判断
		ps.setObject(i, parameter.getCode(), type.TYPE_CODE);

	}

	/**
	 * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
	 * 
	 * @param code
	 *            数据库中存储的自定义code属性
	 * @return code对应的枚举类
	 */
	private T locateEnumStatus(Object code) {
		for (T status : enums) {
			if (status.getCode().toString().trim().equals(code.toString().trim())) {
				return status;
			}
		}
		throw new IllegalArgumentException("未知的枚举类型：" + code + ",请核对" + type.getSimpleName());
	}

}
