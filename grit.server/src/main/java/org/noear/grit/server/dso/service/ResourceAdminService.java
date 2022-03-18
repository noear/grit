package org.noear.grit.server.dso.service;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.data.ResourceLinkedDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源管理服务
 *
 * @author noear
 * @since 1.0
 */
public interface ResourceAdminService {

    /**
     * 添加资源
     *
     * @param resource 资源
     */
    long addResource(ResourceDo resource) throws SQLException;

    /**
     * 同步资源
     *
     * @param resource 资源
     * */
    boolean synResourceByGuid(ResourceDo resource) throws SQLException;

    /**
     * 更新资源
     *
     * @param resourceId 资源Id
     * @param resource   资源
     */
    boolean updResourceById(long resourceId, ResourceDo resource) throws SQLException;

    /**
     * 删除资源
     *
     * @param resourceId 资源Id
     */
    boolean delResourceById(long resourceId) throws SQLException;


    /**
     * 批量删除资源
     */
    boolean delResourceByIds(String ids) throws SQLException;


    /**
     * 批量禁用资源
     */
    boolean desResourceByIds(String ids, boolean disabled) throws SQLException;

    /**
     * 获取资源
     *
     * @param resourceId 资源Id
     */
    Resource getResourceById(long resourceId) throws SQLException;

    /**
     * 获取资源
     *
     * @param guid guid
     */
    Resource getResourceByGuid(String guid) throws SQLException;

    /**
     * 获取管理用的资源空间列表
     */
    List<ResourceSpace> getSpaceList() throws SQLException;

    /**
     * 获取管理用的空间内所有资源
     *
     * @param resourceId 空间资源Id
     */
    List<Resource> getResourceListBySpace(long resourceId) throws SQLException;


    /**
     * 获取管理用的空间内所有资源分组
     *
     * @param resourceId 空间资源Id
     */
    List<ResourceGroup> getResourceGroupListBySpace(long resourceId) throws SQLException;

    /**
     * 获取管理用的空间内所有资源分组
     *
     * @param resourceId 空间资源Id
     * @param ids        资源Ids
     */
    List<ResourceGroup> getResourceGroupListBySpaceAndIds(long resourceId, String ids) throws SQLException;

    /**
     * 获取管理用的空间内所有资源实体
     *
     * @param resourceId 空间资源Id
     */
    List<ResourceEntity> getResourceEntityListBySpace(long resourceId) throws SQLException;


    /**
     * 获取管理用的下级资源表表
     *
     * @param resourceId 资源Id
     */
    List<Resource> getSubResourceListByPid(long resourceId) throws SQLException;


    /**
     * 获取管理用的下级资源表表
     *
     * @param resourceId 资源Id
     */
    List<Resource> getSubResourceListByPidAndIds(long resourceId, String ids) throws SQLException;


    ////////////////

    /**
     * 添加资源关联
     *
     * @param resourceId  资源Id
     * @param subjectId   主体Id
     * @param subjectType 主体类型
     */
    long addResourceLink(long resourceId, long subjectId, int subjectType) throws SQLException;

    /**
     * 删除资源关联
     *
     * @param linkIds 资源连接Ids
     */
    void delResourceLink(long... linkIds) throws SQLException;

    /**
     * 添加资源关联
     *
     * @param subjectId   主体Id
     * @param subjectType 主体类型
     * @param resourceIds 资源Ids
     */
    void addResourceLinkBySubject(long subjectId, int subjectType, List<Long> resourceIds) throws SQLException;

    /**
     * 删除资源关联
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     */
    void delResourceLinkBySubjectBySpace(long subjectId, long resourceSpaceId) throws SQLException;

    /**
     * 获取资源关联（仅自己的）
     *
     * @param subjectId 主体Id
     */
    List<ResourceLinkedDo> getResourceLinkListBySubjectSlf(long subjectId) throws SQLException;


    /**
     * 获取资源关联（自己的 + 继承的）
     *
     * @param subjectId 主体Id
     */
    List<ResourceLinkedDo> getResourceLinkListBySubjectAll(long subjectId) throws SQLException;
}
