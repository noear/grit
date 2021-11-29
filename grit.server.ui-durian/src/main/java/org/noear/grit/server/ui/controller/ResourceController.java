package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.type.ResourceType;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource")
@Controller
public class ResourceController extends BaseController {
    @Inject
    GritClient gritClient;
    
    @Mapping("edit")
    public Object edit(long resource_id, long group_id, int type) throws SQLException {
        Resource m1 = gritClient.resourceAdmin().getResourceById(resource_id);

        if (m1.resource_id == null) {
            m1.resource_type = type;
            m1.is_fullview = false;
            m1.is_disabled = false;
            m1.is_visibled = true;

            if(group_id > 0) {
                Resource m2 = gritClient.resourceAdmin().getResourceById(group_id);
                m1.resource_pid = group_id;
                m1.resource_sid = m2.resource_sid; //传导sid

                if(m2.resource_type == ResourceType.space.code){
                    m1.resource_sid = m2.resource_id;
                }
            }
        }

        viewModel.put("m1", m1);

        return view("grit/ui/resource_edit");
    }

    @Mapping("edit/ajax/save")
    public Object edit_ajax_save(long resource_id, ResourceDo resource) throws SQLException {
        if(resource.is_disabled == null){
            resource.is_disabled = false;
        }

        if(resource.is_visibled == null){
            resource.is_visibled = false;
        }

        if(resource.is_fullview == null){
            resource.is_fullview = false;
        }

        if(resource.order_index == null){
            resource.order_index = 0;
        }

        //必填
        if(Utils.isEmpty(resource.display_name)){
            return Result.failure("The display name cannot be empty");
        }

        if (resource_id > 0) {
            gritClient.resourceAdmin()
                    .updResourceById(resource_id, resource);
        } else {
            gritClient.resourceAdmin()
                    .addResource(resource);
        }

        return Result.succeed();
    }

    @Mapping("edit/ajax/del")
    public Object edit_ajax_del(long resource_id) throws SQLException{
        gritClient.resourceAdmin().delResourceById(resource_id);

        return Result.succeed();
    }
}