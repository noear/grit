package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.*;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/auth")
@Controller
public class AuthController extends BaseController {
    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = GritClient.global().subjectAdmin().getGroupList();
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
        List<SubjectEntity> list = GritClient.global().subjectAdmin().getSubjectEntityListByGroup(group_id);
        list.sort(SubjectComparator.instance);

        return Result.succeed(list);
    }

    @Mapping("ajax/save")
    public Object auth_save(){
        return Result.succeed();
    }

    @Mapping("inner")
    public Object inner(long subject_id, long space_id) throws SQLException {
        Subject subject = GritClient.global().subjectAdmin().getSubjectById(subject_id);

        List<ResourceSpace> spaceList = GritClient.global().resourceAdmin().getSpaceList();
        spaceList.sort(ResourceComparator.instance);
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        List<ResourceGroup> groupList = GritClient.global().resourceAdmin().getResourceGroupListBySpace(space_id);
        groupList = ResourceTreeUtils.build(groupList, space_id);

        List<Resource> resourceList = GritClient.global().resourceAdmin().getResourceListBySpace(space_id);
        resourceList = ResourceTreeUtils.build(resourceList, space_id);

        viewModel.put("subject_id", subject_id);
        viewModel.put("subject", subject);

        viewModel.put("space_id", space_id);
        viewModel.put("spaceList", spaceList);
        viewModel.put("groupList", groupList);
        viewModel.put("resourceList", resourceList);

        return view("grit/ui/auth_inner");
    }
}
