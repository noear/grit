package org.noear.grit.server.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.*;
import org.noear.grit.server.dso.AfterHandler;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.AuthService;
import org.noear.okldap.LdapClient;
import org.noear.okldap.LdapSession;
import org.noear.okldap.entity.LdapPerson;
import org.noear.solon.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 签权服务实现
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@After(AfterHandler.class)
@Mapping("/grit/api/v1/AuthService")
@Remoting
public class AuthServiceImpl implements AuthService {
    static Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Inject(required = false)
    LdapClient ldapClient;

    /**
     * 主体登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    @Override
    public Subject login(String loginName, String loginPassword) throws Exception {
        if (loginPassword == null || loginPassword.length() < 4) {
            //密码太短不让登录
            log.warn("Warn ... loginPassword == null || loginPassword.length() < 4");
            return new Subject();
        }

        if (ldapClient != null) {
            log.info("Using ldap account ...");

            //尝试用ldap登录
            LdapPerson person = null;
            try (LdapSession session = ldapClient.open()) {
                person = session.findPersonOne(loginName, loginPassword);
            }

            if (person != null) {
                log.info("Found ldap account : " + loginName);
                //ldap登录成功后，直接查出用户信息
                Subject subject = GritClient.global().subject().getSubjectByLoginName(loginName);

                if (subject.isEmpty()) {
                    //如果b没有这个账号，则创建一个
                    subject.subject_id = GritClient.global().subject().regSubject(loginName, loginPassword, person.getDisplayName());
                    subject.login_name = loginName;
                    subject.display_name = person.getDisplayName();
                }

                return subject;
            } else {
                return new Subject();
            }
        } else {
            log.info("Using grit account ...");
            //尝试用原生账号登录
            return GritClient.global().subject().getSubjectByLoginNameAndPassword(loginName, loginPassword);
        }
    }

    /**
     * 检测主体是否有Uri
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param uri             路径（例：/user/add）
     */
    @Override
    public boolean hasUriBySpace(long subjectId, long resourceSpaceId, String uri) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.global().resource().getResourceByUriAndSpace(resourceSpaceId, uri);

        if (Resource.isEmpty(resource)) {
            return false;
        }

        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().hasResourceLinkBySubjects(resource.resource_id, subjectIds);
    }

    /**
     * 检测主体是否有权限
     *
     * @param subjectId       主体Id
     * @param resourceSpaceId 资源空间Id
     * @param permission      权限（例：user:add）
     */
    @Override
    public boolean hasPermissionBySpace(long subjectId, long resourceSpaceId, String permission) throws SQLException {
        if (subjectId < 1) {
            return false;
        }

        Resource resource = GritClient.global().resource().getResourceByCodeAndSpace(resourceSpaceId, permission);

        if (Resource.isEmpty(resource)) {
            return false;
        }

        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().hasResourceLinkBySubjects(resource.resource_id, subjectIds);
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

    @Override
    public List<ResourceEntity> getResListByGroup(long subjectId, long resourceGroupId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        return GritClient.global().resourceLink().getResourceEntityListBySubjectsAndGroup(subjectIds, resourceGroupId, null);
    }

    @Override
    public List<ResourceEntity> getResListByGroupCode(long subjectId, String resourceGroupCode) throws SQLException {
        Resource group = GritClient.global().resource().getResourceByCode(resourceGroupCode);

        if (Resource.isEmpty(group)) {
            return new ArrayList<>();
        }

        return getResListByGroup(subjectId, group.resource_id);
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

        List<ResourceEntity> list = GritClient.global().resourceLink()
                .getResourceEntityListBySubjectsAndGroup(subjectIds, resourceGroupId, true);

        //排序
        list.sort(ResourceComparator.instance);

        return list;
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
     * 获取主体的授与路径分组列表（已排序）
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public List<ResourceGroup> getUriGroupListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        List<ResourceGroup> list = GritClient.global().resourceLink().getResourceGroupListBySubjects(subjectIds, resourceSpaceId, true);

        list.sort(ResourceComparator.instance);

        return list;
    }

    /**
     * 获取主体的授与权限列表
     *
     * @param subjectId 主体Id
     * @param resourceSpaceId 资源空间Id
     */
    @Override
    public List<ResourceEntity> getPermissionListBySpace(long subjectId, long resourceSpaceId) throws SQLException {
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
     * 获取主体的资源空间列表（已排序）
     *
     * @param subjectId 主体Id
     */
    @Override
    public List<ResourceSpace> getSpaceList(long subjectId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);

        List<ResourceSpace> list = GritClient.global().resourceLink().getResourceSpaceListBySubjects(subjectIds, true);
        list.sort(ResourceComparator.instance);

        return list;
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
                .getSubjectGroupIdListByEntity(subjectEntityId);

        //加上自己的主体id
        subjectIds.add(subjectEntityId);

        return subjectIds;
    }
}
