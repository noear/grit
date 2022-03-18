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
     * @param jsond 数据
     */
    boolean importSchema(String jsond) throws Exception;

    /**
     * 导出资源架构
     *
     * @param resourceSpaceId 资源空间Id
     * @return jsond
     */
    String exportSchema(long resourceSpaceId) throws Exception;
}
