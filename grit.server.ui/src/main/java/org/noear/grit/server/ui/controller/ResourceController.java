package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.utils.ResourceComparator;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
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
public class ResourceController extends BaseController {
    @Mapping
    public Object home(Long space_id, Long group_id) throws SQLException {
        List<ResourceSpace> spaceList = GritClient.global().resourceAdmin().getSpaceList();

        if (space_id == null) {
            if (spaceList.size() > 0) {
                space_id = spaceList.get(0).resource_id;
            }
        }

        List<ResourceGroup> groupList = GritClient.global().resourceAdmin().getResourceGroupListBySpace(space_id);

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
    public Object inner(long group_id, String key, int state) throws SQLException {
        List<Resource> list = GritClient.global().resourceAdmin().getSubResourceListByPid(group_id);

        viewModel.put("key", key);
        viewModel.put("state", state);
        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_entity_inner");
    }
}
