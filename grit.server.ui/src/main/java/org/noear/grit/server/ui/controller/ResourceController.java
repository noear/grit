package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.type.ResourceType;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

import java.sql.SQLException;
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
    public Object inner(long group_id, String key, Integer state) throws SQLException {
        if (state == null) {
            state = 1;
        }

        List<Resource> list = GritClient.global().resourceAdmin().getSubResourceListByPid(group_id);

        viewModel.put("key", key);
        viewModel.put("state", state);
        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_entity_inner");
    }

    @Mapping("edit")
    public Object edit(long resource_id, long group_id, int type) throws SQLException {
        Resource m1 = GritClient.global().resourceAdmin().getResourceById(resource_id);

        if (m1.resource_id == null) {
            m1.resource_type = type;
            m1.is_fullview = false;
            m1.is_disabled = false;
            m1.is_visibled = true;

            if(group_id > 0) {
                Resource m2 = GritClient.global().resourceAdmin().getResourceById(group_id);
                m1.resource_pid = group_id;
                m1.resource_sid = m2.resource_sid; //传导sid
            }
        }

        viewModel.put("type", type);
        viewModel.put("m1", m1);

        return view("grit/ui/resource_entity_edit");
    }

    @Mapping("edit/ajax/save")
    public Object edit_ajax_save(long resource_id, Resource resource) throws SQLException {
        if (resource_id > 0) {
            GritClient.global().resourceAdmin()
                    .updResourceById(resource_id, resource);
        } else {
            GritClient.global().resourceAdmin()
                    .addResource(resource);
        }

        return Result.succeed();
    }

    @Mapping("edit/ajax/del")
    public Object edit_ajax_del(long resource_id) throws SQLException{

        GritClient.global().resourceAdmin().delResourceById(resource_id);

        return Result.succeed();
    }
}
