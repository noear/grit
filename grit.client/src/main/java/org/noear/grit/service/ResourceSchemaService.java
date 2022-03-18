package org.noear.grit.service;

import java.sql.SQLException;

/**
 * 资源架构服务
 *
 * @author noear
 * @since 1.0
 */
public interface ResourceSchemaService {
    /**
     * 导入资源架构
     *
     * @param json 数据
     */
    boolean importSchema(String json) throws SQLException;

    /**
     * 导出资源架构
     *
     * @param resourceSpaceId 资源空间Id
     * @return json
     */
    String exportSchema(long resourceSpaceId) throws SQLException;
}
