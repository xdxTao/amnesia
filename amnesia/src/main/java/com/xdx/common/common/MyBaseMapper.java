package com.xdx.common.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实现BaseMapper 对里面的一些方法进行重写以便更好的使用
 * @author 小道仙
 * @date 2020年2月26日
 */
public interface MyBaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>  {

    /**
     * 查询列表
     * @param t
     */
    default List<T> selectList(T t) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(t);
        return selectList(qw);
    }

    default List<T> selectList(T t,String sort,String column) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(t);
        if (sort != null){
            if (sort.equals("asc")){
                qw.orderByAsc(column);
            }else{
                qw.orderByDesc(column);
            }
        }
        return selectList(qw);
    }

    /**
     * 查询一条记录
     */
    default T selectOne(T t){
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(t);
        return selectOne(qw);
    }

    /**
     * 查询总数
     */
    default Integer selectCount(T t){
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(t);
        return selectCount(qw);
    }

    /**
     * 更新数据
     *
     * @param entity  要更新的数据
     * @param update   更新的条件
     */
    default int update(T entity,T update){
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(update);
        return update(entity,qw);
    }
}
