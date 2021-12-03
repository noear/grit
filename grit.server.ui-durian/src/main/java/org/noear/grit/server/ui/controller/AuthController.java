package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.*;
import org.noear.grit.model.type.SubjectType;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.server.dso.service.SubjectAdminService;
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
@Mapping("/grit/ui/auth")
@Controller
public class AuthController extends BaseController {
    @Inject
    ResourceAdminService resourceAdminService;
    @Inject
    SubjectAdminService subjectAdminService;


    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = subjectAdminService.getGroupList();
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

    @Mapping("s")
    public Object auth_s() throws SQLException {
        List<SubjectEntity> enityList = subjectAdminService.getSubjectEntityListByAll();
        enityList.sort(SubjectComparator.instance);

        viewModel.put("group_id", 0L);
        viewModel.put("enityList", enityList);

        return view("grit/ui/auth_s");
    }

    @Mapping("subject.entity.get")
    public Object entity_get(long group_id) throws SQLException {
        List<SubjectEntity> enityList = null;
        if (group_id == 0) {
            enityList = subjectAdminService.getSubjectEntityListByAll();
        } else {
            enityList = subjectAdminService.getSubjectEntityListByGroup(group_id);
        }

        enityList.sort(SubjectComparator.instance);

        return Result.succeed(enityList);
    }


    @Mapping("inner")
    public Object inner(long subject_id, long space_id) throws SQLException {
        if (subject_id == 0) {
            return null;
        }

        Subject subject = subjectAdminService.getSubjectById(subject_id);

        //获取授权资源Ids
        StringBuilder authRes = new StringBuilder();
        if (subject.subject_type == SubjectType.group.code) {
            resourceAdminService.getResourceLinkListBySubjectSlf(subject_id)
                    .stream().forEach(r -> {
                        authRes.append(r.resource_id).append(",");
                    });
        } else {
            resourceAdminService.getResourceLinkListBySubjectAll(subject_id)
                    .stream().forEach(r -> {
                        authRes.append(r.resource_id).append(",");
                    });
        }

        if (authRes.length() > 0) {
            authRes.setLength(authRes.length() - 1);
        }


        //获取资源空间列表
        List<ResourceSpace> spaceList = resourceAdminService.getSpaceList();
        spaceList.sort(ResourceComparator.instance);
        if (space_id == 0 && GritClient.global().getCurrentSpaceId() > 0) {
            //尝试用当前系统的空间id
            space_id = GritClient.global().getCurrentSpaceId();
        }

        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        //获取当前空间的资源组列表
        List<ResourceGroup> groupList = resourceAdminService.getResourceGroupListBySpace(space_id);
        groupList = ResourceTreeUtils.build(groupList, space_id);

        //获取当前空间的资源列表（供资源组获取子节点）
        List<Resource> resourceList = resourceAdminService.getResourceListBySpace(space_id);
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


    @Mapping("ajax/save")
    public Result auth_save(long subject_id, int subject_type, long space_id, String authRes) throws SQLException {
        if (subject_id == 0) {
            return Result.failure();
        }

        //先清
        resourceAdminService
                .delResourceLinkBySubjectBySpace(subject_id, space_id);

        //批插
        if (Utils.isNotEmpty(authRes)) {
            List<Long> resIds = Arrays.stream(authRes.split(",")).map(s -> Long.parseLong(s))
                    .collect(Collectors.toList());

            resourceAdminService
                    .addResourceLinkBySubject(subject_id, subject_type, resIds);
        }

        return Result.succeed();
    }
}
