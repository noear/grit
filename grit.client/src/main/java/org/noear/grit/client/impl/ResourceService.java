package org.noear.grit.client.impl;

import org.noear.grit.client.model.Resource;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源服务
 *
 * @author noear
 * @since 1.0
 */
public interface ResourceService {
    /**
     * 资源获取
     *
     * @param resourceId 资源Id
     * */
    Resource getResourceById(long resourceId) throws SQLException;

    /**
     * 资源获取
     *
     * @param resourceCode 资源代号
     * */
    Resource getResourceByCode(String resourceCode) throws SQLException;

    /**
     * 资源获取
     *
     * @param resourcePath 资源路径
     * */
    Resource getResourceByPath(String resourcePath) throws SQLException;

    /**
     * 资源代号是否存在
     *
     * @param resourceCode 资源代号
     * @return 是否存在
     */
    boolean hasResourceCode(String resourceCode) throws SQLException;

    /**
     * 资源路径是否存在
     *
     * @param path 链接路径
     * @return 是否存在
     */
    boolean hasResourcePath(String path) throws SQLException;

    /**
     * 资源列表根据组获取
     *
     * @param groupCode 分组代号
     */
    List<Resource> getResourceListByGroup(String groupCode) throws SQLException;


    /**
     * 资源列表根据组获取
     *
     * @param groupId 分组Id
     */
    List<Resource> getResourceListByGroup(long groupId) throws SQLException;



    /**
     * 资源列表根据用户获取
     *
     * @param userId 用户Id
     */
    List<Resource> getResourceListByUser(long userId) throws SQLException;



    /**
     * 资源列表根据用户和组获取
     *
     * @param userId    用户Id
     * @param groupCode 分组代号
     * @return 分组列表
     */
    List<Resource> getResourceListByUserAndGroup(long userId, String groupCode) throws SQLException;


    /**
     * 资源列表根据用户和组获取
     *
     * @param userId  用户Id
     * @param groupId 分组Id
     * @return 分组列表
     */
    List<Resource> getResourceListByUserAndGroup(long userId, long groupId) throws SQLException;
}
