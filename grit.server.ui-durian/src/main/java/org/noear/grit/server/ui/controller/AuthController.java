package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.*;
import org.noear.grit.model.type.SubjectType;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/auth")
@Controller
public class AuthController extends BaseController {
    @Inject
    GritClient gritClient;

    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = gritClient.subjectAdmin().getGroupList();
        list = SujectTreeUtils.build(list, 0);

        if (group_id == null) {
            if (list.size() > 0) {
                group_id = list.get(0).subject_id;
            }
        }

        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/auth");
    }

    @Mapping("subject.entity.get")
    public Object entity_get(long group_id) throws SQLException {
        List<SubjectEntity> list = gritClient.subjectAdmin().getSubjectEntityListByGroup(group_id);
        list.sort(SubjectComparator.instance);

        return Result.succeed(list);
    }

    @Mapping("ajax/save")
    public Object auth_save(long subject_id, int subject_type, String authRes) throws SQLException {
        if (subject_id == 0) {
            return Result.failure();
        }

        //先清
        gritClient.resourceAdmin()
                .delResourceLinkBySubject(subject_id);

        //批插
        if (Utils.isNotEmpty(authRes)) {
            List<Long> resIds = Arrays.stream(authRes.split(",")).map(s -> Long.parseLong(s))
                    .collect(Collectors.toList());

            gritClient.resourceAdmin()
                    .addResourceLinkBySubject(subject_id, subject_type, resIds);
        }

        return Result.succeed();
    }

    @Mapping("inner")
    public Object inner(long subject_id, long space_id) throws SQLException {
        Subject subject = gritClient.subjectAdmin().getSubjectById(subject_id);

        //获取授权资源Ids
        StringBuilder authRes = new StringBuilder();
        if(subject.subject_type == SubjectType.group.code) {
            gritClient.resourceAdmin().getResourceLinkListBySubjectSlf(subject_id)
                    .stream().forEach(r -> {
                        authRes.append(r.resource_id).append(",");
                    });
        }else{
            gritClient.resourceAdmin().getResourceLinkListBySubjectAll(subject_id)
                    .stream().forEach(r -> {
                        authRes.append(r.resource_id).append(",");
                    });
        }

        if (authRes.length() > 0) {
            authRes.setLength(authRes.length() - 1);
        }


        //获取资源空间列表
        List<ResourceSpace> spaceList = gritClient.resourceAdmin().getSpaceList();
        spaceList.sort(ResourceComparator.instance);
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        //获取当前空间的资源组列表
        List<ResourceGroup> groupList = gritClient.resourceAdmin().getResourceGroupListBySpace(space_id);
        groupList = ResourceTreeUtils.build(groupList, space_id);

        //获取当前空间的资源列表（供资源组获取子节点）
        List<Resource> resourceList = gritClient.resourceAdmin().getResourceListBySpace(space_id);
        resourceList = ResourceTreeUtils.build(resourceList, space_id);

        viewModel.put("subject_id", subject_id);
        viewModel.put("subject", subject);
        viewModel.put("authRes", authRes);

        viewModel.put("space_id", space_id);
        viewModel.put("spaceList", spaceList);
        viewModel.put("groupList", groupList);
        viewModel.put("resourceList", resourceList);

        return view("grit/ui/auth_inner");
    }
}
