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
import java.util.ArrayList;
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
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        List<ResourceGroup> groupList = GritClient.global().resourceAdmin().getResourceGroupListBySpace(space_id);
        List<ResourceGroup> groupList2 = new ArrayList<>(groupList.size());
        long space_id2 = space_id;
        groupList.stream().filter(r -> r.resource_pid == space_id2)
                .sorted(ResourceComparator.instance)
                .forEachOrdered(r -> {
                    r.level = 0;
                    groupList2.add(r);
                    groupList.stream().filter(r2 -> r2.resource_pid == r.resource_id)
                            .sorted(ResourceComparator.instance)
                            .forEachOrdered(r2 -> {
                                r2.level = 1;
                                groupList2.add(r2);
                            });
                });

        if (group_id == null) {
            if (groupList2.size() > 0) {
                group_id = groupList2.get(0).resource_id;
            }
        }

        viewModel.put("space_id", space_id);
        viewModel.put("group_id", group_id);
        viewModel.put("spaceList", spaceList);
        viewModel.put("groupList", groupList2);

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
