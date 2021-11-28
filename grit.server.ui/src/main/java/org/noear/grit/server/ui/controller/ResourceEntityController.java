package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/entity")
@Controller
public class ResourceEntityController extends BaseController {
    @Mapping
    public Object home(long space_id, Long group_id) throws SQLException {
        List<ResourceSpace> spaceList = GritClient.global().resourceAdmin().getSpaceList();
        spaceList.sort(ResourceComparator.instance);
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        List<ResourceGroup> groupList = GritClient.global().resourceAdmin().getResourceGroupListBySpace(space_id);
        groupList.sort(ResourceComparator.instance);
        if (group_id == null) {
            if (groupList.size() > 0) {
                group_id = groupList.get(0).resource_id;
            }
        }

        viewModel.put("space_id", space_id);
        viewModel.put("group_id", group_id);
        viewModel.put("spaceList", spaceList);
        viewModel.put("groupList", groupList);

        return view("grit/ui/resource_entity");
    }

    @Mapping("inner")
    public Object inner(long group_id) throws SQLException {
        List<Resource> list = GritClient.global().resourceAdmin().getSubResourceListByPid(group_id);

        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_entity_inner");
    }
}
