package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/entity")
@Controller
public class ResourceEntityController extends BaseController {
    @Inject
    ResourceAdminService resourceAdminService;
    
    @Mapping
    public ModelAndView home(long space_id, Long group_id) throws SQLException {
        List<ResourceSpace> spaceList = resourceAdminService.getSpaceList();
        space_id = ResourceSpaceCookie.build(space_id, spaceList);
        ResourceSpaceCookie.set(space_id);

        List<ResourceGroup> groupList = resourceAdminService.getResourceGroupListBySpace(space_id);
        groupList = ResourceTreeUtils.build(groupList, space_id);

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
    public ModelAndView inner(long group_id) throws SQLException {
        List<Resource> list = resourceAdminService.getSubResourceListByPid(group_id);
        List<Resource> list2 = list.stream().filter(r->r.resource_type == 0)
                .sorted(ResourceComparator.instance)
                .collect(Collectors.toList());

        viewModel.put("group_id", group_id);
        viewModel.put("list", list2);

        return view("grit/ui/resource_entity_inner");
    }
}
