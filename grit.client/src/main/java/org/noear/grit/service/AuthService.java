package org.noear.grit.service;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectGroup;

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
     * 登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    Subject login(String loginName, String loginPassword) throws SQLException;



    /**
     * 检测主体是否有Uri
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param uri             路径（例：/user/add）
     */
    boolean subjectHasUri(long subjectId, long resourceSpaceId, String uri) throws SQLException;

    /**
     * 检测主体是否有Uri
     *
     * @param subjectId       主体Id
     * @param uri             路径（例：/user/add）
     */
    default boolean subjectHasUri(long subjectId,  String uri) throws SQLException{
        return subjectHasUri(subjectId, GritClient.getCurrentSpaceId(), uri);
    }

    /**
     * 检测主体是否有权限
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param permission      权限（例：user:add）
     */
    boolean subjectHasPermission(long subjectId, long resourceSpaceId, String permission) throws SQLException;

    /**
     * 检测主体是否有权限
     *
     * @param subjectId       主体Id
     * @param permission      权限（例：user:add）
     */
    default boolean subjectHasPermission(long subjectId, String permission) throws SQLException{
        return subjectHasPermission(subjectId, GritClient.getCurrentSpaceId(), permission);
    }

    /**
     * 检测是否有角色
     *
     * @param subjectId
     * @param role      角色（例：water-admin）
     */
    boolean subjectHasRole(long subjectId, String role) throws SQLException;


    /**
     * 获取主体的路径列表
     */
    List<ResourceEntity> getSubjectUriListByGroup(long subjectId, long resourceGroupId) throws SQLException;

    /**
     * 获取主体的路径列表
     */
    List<ResourceEntity> getSubjectUriListBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的路径列表
     */
    default List<ResourceEntity> getSubjectUriListBySpace(long subjectId) throws SQLException{
        return getSubjectUriListBySpace(subjectId, GritClient.getCurrentSpaceId());
    }

    /**
     * 获取主体的第一个路径
     */
    ResourceEntity getSubjectUriFristBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的第一次路径
     */
    default ResourceEntity getSubjectUriFristBySpace(long subjectId) throws SQLException{
        return getSubjectUriFristBySpace(subjectId, GritClient.getCurrentSpaceId());
    }

    /**
     * 获取主体的分组下的第一个路径
     */
    ResourceEntity getSubjectUriFristByGroup(long subjectId, long resourceGroupId) throws SQLException;



    /**
     * 获取主体的路径分组列表
     */
    List<ResourceGroup> getSubjectUriGroupListBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的路径分组列表
     */
    default List<ResourceGroup> getSubjectUriGroupListBySpace(long subjectId) throws SQLException{
        return getSubjectUriGroupListBySpace(subjectId, GritClient.getCurrentSpaceId());
    }



    /**
     * 获取主体的权限列表
     */
    List<ResourceEntity> getSubjectPermissionList(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取主体的权限列表
     */
    default List<ResourceEntity> getSubjectPermissionList(long subjectId) throws SQLException {
        return getSubjectPermissionList(subjectId, GritClient.getCurrentSpaceId());
    }


    /**
     * 获取主体的角色列表
     */
    List<SubjectGroup> getSubjectRoleList(long subjectId) throws SQLException;
}
