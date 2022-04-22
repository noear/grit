package org.noear.grit.service;

import org.noear.snack.ONode;

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
     * @param data 数据
     */
    boolean importSchema(String data) throws Exception;

    /**
     * 导出资源架构
     *
     * @param resourceSpaceId 资源空间Id
     * @return jsond or json
     */
    String exportSchema(long resourceSpaceId, String fmt) throws Exception;
}
