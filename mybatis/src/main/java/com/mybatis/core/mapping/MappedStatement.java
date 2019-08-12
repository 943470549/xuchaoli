package com.mybatis.core.mapping;

/**
 * 对应map配置文件里面的属性,Mapper Method MetaData
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class MappedStatement {

    /**
     * package name,eg: com.mybatis.mapper.CourseMapper
     */
    private String namespace;

    /**
     * source id, eg: com.mybatis.mapper.CourseMapper.get
     */
    private String sourceId;

    /**
     * result map, eg: com.mybatis.entities.Course
     */
    private String resultMap;

    /**
     * prepareStatement sql
     */
    private String sql;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceid) {
        this.sourceId = sourceid;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
