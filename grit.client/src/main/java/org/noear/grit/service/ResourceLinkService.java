package org.noear.grit.service;

import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.domain.Subject;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源关联服务
 *
 * @author noear
 * @since 1.0
 */
public interface ResourceLinkService {

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
     * 检测是否存在连接
     *
     * @param resourceId 资源Id
     * @param subjectId  主体Id
     */
    boolean hasResourceLink(long resourceId, long subjectId) throws SQLException;

    /**
     * 获取资源授权过的所有主体
     *
     * @param resourceId 资源Id
     * @return 主体列表
     */
    List<Subject> getSubjectListByResource(long resourceId) throws SQLException;

    /**
     * 获取主体所有的授权资源列表
     *
     * @param subjectId  主体Id
     * @param isVisibled 是否可见（null表示全部）
     * @return 资源列表
     */
    List<ResourceEntity> getResourceEntityListBySubject(long subjectId, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体在某一个资源组下的授权资源列表
     *
     * @param subjectId       主体Id
     * @param resourceGroupId 资源组Id
     * @return 资源列表
     */
    List<ResourceEntity> getResourceEntityListBySubjectAndGroup(long subjectId, long resourceGroupId, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体在某一个资源空间下的授权资源列表
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @return 资源列表
     */
    List<ResourceEntity> getResourceEntityListBySubjectAndSpace(long subjectId, long resourceSpaceId, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体在某一个资源组下的授权资源列表
     *
     * @param subjectIds      主体Ids
     * @param resourceGroupId 资源组Id
     * @return 资源列表
     */
    List<ResourceEntity> getResourceEntityListBySubjectsAndGroup(List<Long> subjectIds, long resourceGroupId, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体在某一个资源空间下的授权资源列表
     *
     * @param subjectIds      主体Ids
     * @param resourceSpaceId 资源空间Id
     * @return 资源列表
     */
    List<ResourceEntity> getResourceEntityListBySubjectsAndSpace(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体的授权资源分组列表
     *
     * @param subjectIds 主体Ids
     * @return 资源列表
     */
    List<ResourceGroup> getResourceGroupListBySubjects(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException;

    /**
     * 获取主体的授权资源空间列表
     *
     * @param subjectIds 主体Ids
     * @return 资源列表
     */
    List<ResourceSpace> getResourceSpaceListBySubjects(List<Long> subjectIds, Boolean isVisibled) throws SQLException;


    /**
     * 获取主体在某一个资源空间下的第一个授权资源
     *
     * @param subjectIds      主体Ids
     * @param resourceSpaceId 资源空间Id
     */
    ResourceEntity getResourceEntityFristBySubjectsAndSpace(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException;



    /**
     * 获取主体在某一个资源分组下的第一个授权资源
     *
     * @param subjectIds      主体Ids
     * @param resourceGroupId 资源空间Id
     */
    ResourceEntity getResourceEntityFristBySubjectsAndGroup(List<Long> subjectIds, long resourceGroupId, Boolean isVisibled) throws SQLException;

}
