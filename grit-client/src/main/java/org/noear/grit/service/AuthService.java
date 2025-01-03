package org.noear.grit.service;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 签权服务
 *
 * @author noear
 * @since 1.0
 */
public interface AuthService {
    /**
     * 主体登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    Subject login(String loginName, String loginPassword) throws Exception;

    /**
     * 主体登录
     *
     * @param loginToken 登录令牌
     */
    Subject loginByToken(String loginToken) throws Exception;


    /**
     * 检测主体是否有路径授予
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param uri             路径（例：/user/add）
     */
    boolean hasUriBySpace(long subjectId, long resourceSpaceId, String uri) throws SQLException;

    /**
     * 检测主体是否有路径授予
     *
     * @param subjectId 主体Id
     * @param uri       路径（例：/user/add）
     */
    default boolean hasUri(long subjectId, String uri) throws SQLException {
        return hasUriBySpace(subjectId, GritClient.global().getCurrentSpaceId(), uri);
    }

    /**
     * 检测主体是否有权限授予
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param permission      权限（例：user:add）
     */
    boolean hasPermissionBySpace(long subjectId, long resourceSpaceId, String permission) throws SQLException;

    /**
     * 检测主体是否有权限授予
     *
     * @param subjectId  主体Id
     * @param permission 权限（例：user:add）
     */
    default boolean hasPermission(long subjectId, String permission) throws SQLException {
        return hasPermissionBySpace(subjectId, GritClient.global().getCurrentSpaceId(), permission);
    }

    /**
     * 检测是否有角色关联
     *
     * @param subjectId
     * @param role      角色（例：water-admin）
     */
    boolean hasRole(long subjectId, String role) throws SQLException;


    /**
     * 获取主体的授与资源列表（包括，可见与不可见）
     *
     * @param subjectId       主体Id
     * @param resourceGroupId 资源组Id
     */
    List<ResourceEntity> getResListByGroup(long subjectId, long resourceGroupId) throws SQLException;

    /**
     * 获取主体的授与资源列表（包括，可见与不可见）
     *
     * @param subjectId         主体Id
     * @param resourceGroupCode 资源组Code
     */
    List<ResourceEntity> getResListByGroupCode(long subjectId, String resourceGroupCode) throws SQLException;


    /**
     * 获取主体的授与路径资源列表
     *
     * @param subjectId       主体Id
     * @param resourceGroupId 资源组Id
     */
    List<ResourceEntity> getUriListByGroup(long subjectId, long resourceGroupId) throws SQLException;

    /**
     * 获取主体的授与路径资源列表
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     */
    List<ResourceEntity> getUriListBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的授与路径资源列表
     *
     * @param subjectId 主体Id
     */
    default List<ResourceEntity> getUriList(long subjectId) throws SQLException {
        return getUriListBySpace(subjectId, GritClient.global().getCurrentSpaceId());
    }

    /**
     * 获取主体的首个授与路径资源
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     */
    ResourceEntity getUriFristBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的首个授与路径资源
     *
     * @param subjectId
     */
    default ResourceEntity getUriFrist(long subjectId) throws SQLException {
        return getUriFristBySpace(subjectId, GritClient.global().getCurrentSpaceId());
    }

    /**
     * 获取主体在某分组下的首个授与路径资源
     *
     * @param subjectId       主体Id
     * @param resourceGroupId 资源组Id
     */
    ResourceEntity getUriFristByGroup(long subjectId, long resourceGroupId) throws SQLException;


    /**
     * 获取主体的授与路径资源分组列表
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     */
    List<ResourceGroup> getUriGroupListBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的授与路径资源分组列表
     *
     * @param subjectId 主体Id
     */
    default List<ResourceGroup> getUriGroupList(long subjectId) throws SQLException {
        return getUriGroupListBySpace(subjectId, GritClient.global().getCurrentSpaceId());
    }


    /**
     * 获取主体的授与权限资源列表
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     */
    List<ResourceEntity> getPermissionListBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的授与权限资源列表
     *
     * @param subjectId 主体Id
     */
    default List<ResourceEntity> getPermissionList(long subjectId) throws SQLException {
        return getPermissionListBySpace(subjectId, GritClient.global().getCurrentSpaceId());
    }


    /**
     * 获取主体关联的角色列表
     *
     * @param subjectId 主体Id
     */
    List<SubjectGroup> getRoleList(long subjectId) throws SQLException;


    /**
     * 获取主体关联的资源空间列表
     *
     * @param subjectId 主体Id
     */
    List<ResourceSpace> getSpaceList(long subjectId) throws SQLException;

    /**
     * 获取主体关联的首个资源空间
     *
     * @param subjectId 主体Id
     */
    ResourceSpace getSpaceFrist(long subjectId) throws SQLException;
}