package org.noear.grit.service;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceSpace;

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
     */
    Resource getResourceById(long resourceId) throws SQLException;

    /**
     * 资源获取
     *
     * @param resourceCode 资源代号
     */
    Resource getResourceByCode(long resourceSpaceId, String resourceCode) throws SQLException;

    default Resource getResourceByCode(String resourceCode) throws SQLException {
        return getResourceByCode(GritClient.global().getCurrentSpaceId(), resourceCode);
    }


    /**
     * 资源获取
     *
     * @param resourceUri 资源路径
     */
    Resource getResourceByUri(long resourceSpaceId, String resourceUri) throws SQLException;

    default Resource getResourceByUri(String resourceUri) throws SQLException {
        return getResourceByUri(GritClient.global().getCurrentSpaceId(), resourceUri);
    }

    /**
     * 检测资源代号是否存在
     *
     * @param resourceCode 资源代号
     * @return 是否存在
     */
    boolean hasResourceByCode(long resourceSpaceId, String resourceCode) throws SQLException;

    default boolean hasResourceByCode(String resourceCode) throws SQLException {
        return hasResourceByCode(GritClient.global().getCurrentSpaceId(), resourceCode);
    }

    /**
     * 极测资源路径是否存在
     *
     * @param resourceUri 资源路径
     * @return 是否存在
     */
    boolean hasResourceByUri(long resourceSpaceId, String resourceUri) throws SQLException;

    default boolean hasResourceByUri(String resourceUri) throws SQLException {
        return hasResourceByUri(GritClient.global().getCurrentSpaceId(), resourceUri);
    }

    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    List<Resource> getSubResourceListByPid(long resourceId) throws SQLException;

    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    List<ResourceEntity> getSubResourceEngityListByPid(long resourceId) throws SQLException;


    ////////////////////

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

}
