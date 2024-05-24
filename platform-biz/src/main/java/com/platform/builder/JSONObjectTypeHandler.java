/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.builder;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author cxd
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JSONObjectTypeHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject array, JdbcType jdbcType) throws SQLException {
        ps.setString(i, array.toJSONString());
    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(columnName));
    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(columnIndex));
    }

    @Override
    public JSONObject getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseObject(callableStatement.getString(columnIndex));
    }
}
