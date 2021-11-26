package org.noear.grit.server.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.domain.TagCounts;
import org.noear.grit.model.type.ResourceType;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.ResourceSpaceService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源空间服务实现
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/ResourceSpaceService")
@Remoting
public class ResourceSpaceServiceImpl implements ResourceSpaceService {
    @Inject("grit.db")
    private DbContext db;
    @Inject("grit.cache")
    private ICacheService cache;

    @Override
    public ResourceSpace getSpaceByCode(String resourceSpaceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceSpaceCode)) {
            return new ResourceSpace();
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceSpaceCode)
                .limit(1)
                .caching(cache)
                .selectItem("*", ResourceSpace.class);
    }

    @Override
    public List<ResourceSpace> getSpaceList() throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_type", ResourceType.space.code)
                .andEq("is_visibled", 1)
                .andEq("is_disabled", 0)
                .caching(cache)
                .selectList("*", ResourceSpace.class);
    }


    @Override
    public List<ResourceSpace> getSpaceListByUser(long subjectId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = GritClient.global().subjectLink()
                .getSubjectGroupListByEntity(subjectId)
                .stream()
                .map(s -> s.subject_id)
                .collect(Collectors.toList());

        //加上自己的主体id
        subjectIds.add(subjectId);

        return GritClient.global().resourceLink().getResourceSpaceListBySubjects(subjectIds, true);

    }


    @Override
    public ResourceSpace getSpaceFristByUser(long subjectId) throws SQLException {
        List<ResourceSpace> branchList = getSpaceListByUser(subjectId);
        if (branchList.size() == 0) {
            return new ResourceSpace();
        } else {
            return branchList.get(0);
        }
    }


    @Override
    public List<ResourceSpace> getAdminSpaceList() throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_type", ResourceType.space.code)
                .selectList("*", ResourceSpace.class);
    }

    @Override
    public List<Resource> getAdminResourceListBySpace(long resourceId) throws SQLException {
        if(resourceId == 0){
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .selectList("*", Resource.class);
    }

    @Override
    public List<Resource> getAdminSubResourceListByPid(long resourceId) throws SQLException {
        if(resourceId == 0){
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .selectList("*", Resource.class);
    }
}
