package com.mybatis.mapper;

import com.mybatis.core.annotation.Mapper;
import com.mybatis.core.annotation.Param;
import com.mybatis.core.annotation.Select;
import com.mybatis.entities.Course;

/**
 * mapper
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
@Mapper
public interface CourseMapper {

//    /**
//     * 添加课程
//     *
//     * @param id   主键
//     * @param name 名称
//     * @return 插入条数
//     */
//    @Insert("insert into course(id, name) values(?,?)")
//    int save(@Param("id") int id, @Param("name") String name);

    /**
     * 获取数据
     *
     * @param id 主键
     * @return 查询结果
     */
    @Select("select id, name from course where id = ?")
    Course getByAnnotation(@Param("id") int id);

    /**
     * 获取数据
     *
     * @param id 主键
     * @return 查询结果
     */
    Course getByXml(@Param("id") int id);
}
