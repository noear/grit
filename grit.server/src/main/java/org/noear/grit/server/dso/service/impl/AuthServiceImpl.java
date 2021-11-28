package org.noear.grit.server.dso.service.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.*;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.AuthService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 签权服务实现
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/v1/AuthService")
@Remoting
public class AuthServiceImpl implements AuthService {
    /**
     * 主体登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    @Override
    public Subject login(String loginName, String loginPassword) throws SQLException {
        return GritClient.global().subject().getSubjectByLoginNameAndPassword(loginName, loginPassword);
    }

    /**
     * 检测主体是否有Uri
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param uri             路径（例：/user/add）
     */
    @Override
    public boolean hasUri(long subjectId, long resourceSpaceId, String uri) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.global().resource().getResourceByUri(resourceSpaceId, uri);

        if (resource.resource_id == null) {
            return false;
        }

        return GritClient.global().resourceLink().hasResourceLink(resource.resource_id, subjectId);
    }

    /**
     * 检测主体是否有权限
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param permission      权限（例：user:add）
     */
    @Override
    public boolean hasPermission(long subjectId, long resourceSpaceId, String permission) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.global().resource().getResourceByCode(resourceSpaceId, permission);

        if (resource.resource_id == null) {
            return false;
        }

        return GritClient.global().resourceLink().hasResourceLink(resource.resource_id, subjectId);
    }

    /**
     * 检测是否有角色
     *
     * @param subjectId
     * @param role      角色（例：water-admin）
     */
    @Override
    public boolean hasRole(long subjectId, String role) throws SQLException {

        Subject subjectGroup = GritClient.global().subject().getSubjectByCode(role);

        return GritClient.global().subjectLink().hasSubjectLink(subjectId, subjectGroup.subject_id);
    }

    /**
     * 获取主体的授与路径列表
     *
     * @param subjectId 主体Id
     * @param resourceGroupId 资源组Id
     */
    @Override
    public List<ResourceEntity> getUriListByGroup(long subjectId, long resourceGroupId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityListBySubjectsAndGroup(subjectIds, resourceGroupId, true);
    }

    /**
     * 获取主体的授与路径列表
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public List<ResourceEntity> getUriListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityListBySubjectsAndSpace(subjectIds, resourceSpaceId, true);
    }

    /**
     * 获取主体的首个授与路径
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public ResourceEntity getUriFristBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityFristBySubjectsAndSpace(subjectIds, resourceSpaceId, true);
    }

    /**
     * 获取主体在某分组下的首个授与路径
     *
     * @param subjectId 主体Id
     * @param resourceGroupId 资源组Id
     */
    @Override
    public ResourceEntity getUriFristByGroup(long subjectId, long resourceGroupId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityFristBySubjectsAndGroup(subjectIds, resourceGroupId, true);
    }

    /**
     * 获取主体的授与路径分组列表
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public List<ResourceGroup> getUriGroupListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceGroupListBySubjects(subjectIds, resourceSpaceId, true);
    }

    /**
     * 获取主体的授与权限列表
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public List<ResourceEntity> getPermissionList(long subjectId, long resourceSpaceId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityListBySubjectsAndSpace(subjectIds, resourceSpaceId, false);
    }

    /**
     * 获取主体的角色列表
     *
     * @param subjectId 主体Id
     */
    @Override
    public List<SubjectGroup> getRoleList(long subjectId) throws SQLException {
        return GritClient.global().subjectLink().getSubjectGroupListByEntity(subjectId);
    }


    ////////////////////////


    /**
     * 获取主体的资源空间列表
     *
     * @param subjectId 主体Id
     */
    @Override
    public List<ResourceSpace> getSpaceList(long subjectId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceSpaceListBySubjects(subjectIds, true);

    }


    /**
     * 获取主体的首个资源空间
     *
     * @param subjectId 主体Id
     */
    @Override
    public ResourceSpace getSpaceFrist(long subjectId) throws SQLException {
        List<ResourceSpace> branchList = getSpaceList(subjectId);

        if (branchList.size() == 0) {
            return new ResourceSpace();
        } else {
            return branchList.get(0);
        }
    }

    ////////////////////


    /**
     * 获取实验验证时的所有主体Id
     * */
    private List<Long> getSubjectIdsByEntityOnAuth(long subjectEntityId) throws SQLException{
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.global().subjectLink()
                .getSubjectGroupListByEntity(subjectEntityId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectEntityId);

        return subjectIds;
    }
}
