package org.noear.grit.server.controller;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.server.dso.AfterHandler;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.server.utils.JsondEntity;
import org.noear.grit.server.utils.JsondUtils;
import org.noear.grit.service.ResourceSchemaService;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.*;
import org.noear.solon.data.annotation.Cache;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源架构服务
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@After(AfterHandler.class)
@Mapping("/grit/api/v1/ResourceSchemaService")
@Remoting
public class ResourceSchemaServiceImpl implements ResourceSchemaService {
    @Inject
    ResourceAdminService adminService;

    final String jsondTable = "grit_schema";

    @Override
    public boolean importSchema(String jsond) throws Exception {
        if (Utils.isEmpty(jsond)) {
            return false;
        }

        JsondEntity jsondEntity = JsondUtils.decode(jsond);

        if (jsondTable.equals(jsondEntity.table) == false) {
            throw new IllegalArgumentException("Invalid space schema json");
        }


        //space
        ONode oNode = jsondEntity.data;

        ONode oSpace = oNode.getOrNull("space");
        if (oSpace == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        ResourceDo spaceD = oSpace.get("meta").toObject(ResourceDo.class);
        if (Utils.isEmpty(spaceD.guid)) {
            throw new IllegalArgumentException("Invalid space schema json");
        }
        spaceD.resource_sid = 0L;
        spaceD.resource_pid = 0L;
        if (adminService.synResourceByGuid(spaceD) == false) {
            //同步失则，表示格式不对
            throw new IllegalArgumentException("Invalid space schema json");
        }

        // groups
        ONode oGroups = oSpace.getOrNull("groups");
        if (oGroups == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        for (ONode oG1 : oGroups.ary()) {
            ResourceDo g1 = oG1.get("meta").toObject(ResourceDo.class);

            g1.resource_pid = spaceD.resource_id;
            g1.resource_sid = spaceD.resource_id;

            if (adminService.synResourceByGuid(g1) == false) {
                //同步失则，表示格式不对
                throw new IllegalArgumentException("Invalid space schema json");
            }

            List<ResourceDo> engitys = oG1.get("engitys").toObjectList(ResourceDo.class);

            for (ResourceDo e1 : engitys) {
                e1.resource_sid = g1.resource_sid;
                e1.resource_pid = g1.resource_id;

                if (adminService.synResourceByGuid(e1) == false) {
                    //同步失则，表示格式不对
                    throw new IllegalArgumentException("Invalid space schema json");
                }
            }
        }

        return false;
    }

    @Cache(seconds = 10)
    @Override
    public String exportSchema(long resourceSpaceId) throws Exception {
        if (resourceSpaceId == 0) {
            return "";
        }

        ONode oNode = new ONode();

        ResourceDo space = adminService.getResourceById(resourceSpaceId);
        List<ResourceGroup> groups = adminService.getResourceGroupListBySpace(resourceSpaceId);

        space.resource_id = null;
        space.resource_pid = null;
        space.resource_sid = null;

        ONode oSpace = oNode.getOrNew("space");
        oSpace.getOrNew("meta").fill(space);

        ONode oGroups = oSpace.getOrNew("groups").asArray();
        for (ResourceGroup g1 : groups) {
            List<Resource> engitys = adminService.getSubResourceListByPid(g1.resource_id);

            g1.resource_id = null;
            g1.resource_pid = null;
            g1.resource_sid = null;
            ONode oG1 = oGroups.addNew();
            oG1.getOrNew("meta").fill(g1);

            ONode oEngitys = oG1.getOrNew("engitys");
            for (Resource e1 : engitys) {
                e1.resource_id = null;
                e1.resource_pid = null;
                e1.resource_sid = null;

                oEngitys.addNew().fill(e1);
            }
        }

        return JsondUtils.encode(jsondTable, oNode);
    }
}
