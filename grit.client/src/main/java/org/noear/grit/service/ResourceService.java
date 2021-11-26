package org.noear.grit.service;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;

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
     * 添加资源
     *
     * @param resource 资源
     */
    long addResource(ResourceDo resource) throws SQLException;

    /**
     * 更新资源
     *
     * @param resourceId 资源Id
     * @param resource   资源
     */
    boolean updResource(long resourceId, ResourceDo resource) throws SQLException;


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
    boolean hasResourceCode(long resourceSpaceId, String resourceCode) throws SQLException;

    default boolean hasResourceCode(String resourceCode) throws SQLException {
        return hasResourceCode(GritClient.global().getCurrentSpaceId(), resourceCode);
    }

    /**
     * 极测资源路径是否存在
     *
     * @param resourceUri 资源路径
     * @return 是否存在
     */
    boolean hasResourceUri(long resourceSpaceId, String resourceUri) throws SQLException;

    default boolean hasResourceUri(String resourceUri) throws SQLException {
        return hasResourceUri(GritClient.global().getCurrentSpaceId(), resourceUri);
    }

    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    List<Resource> getSubResourceListById(long resourceId) throws SQLException;

    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    List<ResourceEntity> getSubResourceEngityListById(long resourceId) throws SQLException;
}
