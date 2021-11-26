package org.noear.grit.client.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.*;
import org.noear.grit.service.AuthService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 签权服务实现
 *
 * @author noear
 * @since 1.0
 */
public class AuthServiceImpl implements AuthService {
    @Override
    public Subject login(String loginName, String loginPassword) throws SQLException {
        return GritClient.subject().getSubjectByLoginNameAndPassword(loginName, loginPassword);
    }

    @Override
    public boolean hasUri(long subjectId, long resourceSpaceId, String uri) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.resource().getResourceByUri(resourceSpaceId, uri);

        if (resource.resource_id == null) {
            return false;
        }

        return GritClient.resourceLink().hasResourceLink(resource.resource_id, subjectId);
    }

    @Override
    public boolean hasPermission(long subjectId, long resourceSpaceId, String permission) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.resource().getResourceByCode(resourceSpaceId, permission);

        if (resource.resource_id == null) {
            return false;
        }

        return GritClient.resourceLink().hasResourceLink(resource.resource_id, subjectId);
    }

    @Override
    public boolean hasRole(long subjectId, String roleSubjectCode) throws SQLException {

        Subject subjectGroup = GritClient.subject().getSubjectByCode(roleSubjectCode);

        return GritClient.subjectLink().hasSubjectLink(subjectId, subjectGroup.subject_id);
    }

    @Override
    public List<ResourceEntity> getUriListByGroup(long subjectId, long resourceGroupId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceEntityListBySubjectsAndGroup(subjectIds, resourceGroupId, true);
    }

    @Override
    public List<ResourceEntity> getUriListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceEntityListBySubjectsAndSpace(subjectIds, resourceSpaceId, true);
    }

    @Override
    public ResourceEntity getUriFristBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceEntityFristBySubjectsAndSpace(subjectIds, resourceSpaceId, true);
    }

    @Override
    public ResourceEntity getUriFristByGroup(long subjectId, long resourceGroupId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceEntityFristBySubjectsAndGroup(subjectIds, resourceGroupId, true);
    }

    @Override
    public List<ResourceGroup> getUriGroupListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceGroupListBySubjects(subjectIds, resourceSpaceId, true);
    }

    @Override
    public List<ResourceEntity> getPermissionList(long subjectId, long resourceSpaceId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.resourceLink().getResourceEntityListBySubjectsAndSpace(subjectIds, resourceSpaceId, false);
    }

    @Override
    public List<SubjectGroup> getRoleList(long subjectId) throws SQLException {
        return GritClient.subjectLink().getSubjectGroupListByEntity(subjectId);
    }
}
