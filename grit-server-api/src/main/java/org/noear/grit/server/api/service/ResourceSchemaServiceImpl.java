package org.noear.grit.server.api.service;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.server.api.dso.AfterHandler;
import org.noear.grit.server.api.dso.BeforeHandler;
import org.noear.grit.server.ui.service.ResourceAdminService;
import org.noear.grit.server.api.utils.JsondEntity;
import org.noear.grit.server.api.utils.JsondUtils;
import org.noear.grit.service.ResourceSchemaService;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.*;
import org.noear.solon.data.annotation.Cache;

import java.util.List;

/**
 * 资源架构服务
 *
 * @author noear
 * @since 1.0
 */
@Addition({BeforeHandler.class, AfterHandler.class})
@Mapping("/grit/api/v1/ResourceSchemaService")
@Component
public class ResourceSchemaServiceImpl implements ResourceSchemaService {
    @Inject
    ResourceAdminService adminService;

    final String jsondTable = "grit_space";

    final String tag_space = "space";
    final String tag_meta = "meta";
    final String tag_groups = "groups";
    final String tag_engitys = "engitys";

    @Override
    public boolean importSchema(String data) throws Exception {
        if (Utils.isEmpty(data)) {
            return false;
        }

        //解析数据
        ONode oNode = null;
        if (data.startsWith("{")) { //支持 json
            //space
            oNode = ONode.loadStr(data);
        } else { //支持 jsond
            JsondEntity jsondEntity = JsondUtils.decode(data);

            if (jsondTable.equals(jsondEntity.table) == false) {
                throw new IllegalArgumentException("Invalid space schema json");
            }

            //space
            oNode = jsondEntity.data;
        }

        //开始导入
        ONode oSpace = oNode.getOrNull(tag_space);
        if (oSpace == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        ResourceDo spaceD = oSpace.get(tag_meta).toObject(ResourceDo.class);

        spaceD.resource_sid = 0L;
        spaceD.resource_pid = 0L;

        if (Utils.isEmpty(spaceD.guid)) {
            if (Utils.isNotEmpty(spaceD.resource_code)) {
                //如果没有 guid ，尝试用 resource_code 找到对应的 guid
                ResourceDo spaceTmp = adminService.getResourceByCode(spaceD.resource_code);
                if (spaceTmp.guid != null) {
                    spaceD.guid = spaceTmp.guid;
                }
            }
        }

        if (adminService.synResourceByGuid(spaceD) == false) {
            //同步失则，表示格式不对
            throw new IllegalArgumentException("Invalid space schema json");
        }


        // groups
        ONode oGroups = oSpace.getOrNull(tag_groups);
        if (oGroups == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        for (ONode oG1 : oGroups.ary()) {
            ResourceDo g1 = oG1.get(tag_meta).toObject(ResourceDo.class);

            g1.resource_pid = spaceD.resource_id;
            g1.resource_sid = spaceD.resource_id;

            if (adminService.synResourceByGuid(g1) == false) {
                //同步失则，表示格式不对
                throw new IllegalArgumentException("Invalid space schema json");
            }

            List<ResourceDo> engitys = oG1.get(tag_engitys).toObjectList(ResourceDo.class);

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

    @Override
    public String exportSchema(long resourceSpaceId, String fmt) throws Exception {
        if (resourceSpaceId == 0) {
            return "";
        }

        ONode oNode = new ONode();

        ResourceDo space = adminService.getResourceById(resourceSpaceId);
        List<ResourceGroup> groups = adminService.getResourceGroupListBySpace(resourceSpaceId);

        space.resource_id = null;
        space.resource_pid = null;
        space.resource_sid = null;

        ONode oSpace = oNode.getOrNew(tag_space);
        oSpace.getOrNew(tag_meta).fill(space);

        ONode oGroups = oSpace.getOrNew(tag_groups).asArray();
        for (ResourceGroup g1 : groups) {
            List<Resource> engitys = adminService.getSubResourceListByPid(g1.resource_id);

            g1.resource_id = null;
            g1.resource_pid = null;
            g1.resource_sid = null;
            ONode oG1 = oGroups.addNew();
            oG1.getOrNew(tag_meta).fill(g1);

            ONode oEngitys = oG1.getOrNew(tag_engitys);
            for (Resource e1 : engitys) {
                e1.resource_id = null;
                e1.resource_pid = null;
                e1.resource_sid = null;

                oEngitys.addNew().fill(e1);
            }
        }

        if ("json".equals(fmt)) {
            return oNode.toJson();
        } else {
            return JsondUtils.encode(jsondTable, oNode);
        }
    }
}
