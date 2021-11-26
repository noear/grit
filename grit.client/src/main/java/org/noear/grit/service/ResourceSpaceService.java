package org.noear.grit.service;

import org.noear.grit.model.domain.ResourceSpace;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源空间服务
 *
 * @author noear
 * @since 1.0
 */
public interface ResourceSpaceService {

    /**
     * 获取资源空间
     *
     * @param resourceSpaceCode 资源空间代号
     */
    ResourceSpace getSpaceByCode(String resourceSpaceCode) throws SQLException;

    /**
     * 获取所有的资源空间列表
     */
    List<ResourceSpace> getSpaceList() throws SQLException;


    /**
     * 获取主体的资源空间列表
     *
     * @param subjectId 主体Id
     */
    List<ResourceSpace> getSpaceListByUser(long subjectId) throws SQLException;

    /**
     * 获取主体的第一个资源空间
     *
     * @param subjectId 主体Id
     */
    ResourceSpace getSpaceFristByUser(long subjectId) throws SQLException;
}
