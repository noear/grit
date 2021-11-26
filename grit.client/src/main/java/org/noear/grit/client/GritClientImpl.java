package org.noear.grit.client;

import org.noear.grit.client.impl.*;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.service.*;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.0
 */
public class GritClientImpl implements GritClient{
    private final SubjectService subjectService;
    private final SubjectLinkService subjectLinkService;

    private final ResourceService resourceService;
    private final ResourceLinkService resourceLinkService;
    private final ResourceSpaceService resourceSpaceService;

    private final AuthService authService;


    private long currentSpaceId;
    private String currentSpaceCode;

    /**
     * 初始化
     */
    public GritClientImpl(DbContext db, ICacheService cache) {
        resourceService = new ResourceServiceImpl(db, cache);
        resourceSpaceService = new ResourceSpaceServiceImpl(db, cache);
        resourceLinkService = new ResourceLinkServiceImpl(db, cache);

        subjectService = new SubjectServiceImpl(db, cache);
        subjectLinkService = new SubjectLinkServiceImpl(db, cache);

        authService = new AuthServiceImpl();
    }

    /**
     * 设置当前资源空间代号
     */
    public void setCurrentSpaceCode(String resourceSpaceCode) {
        if (resourceSpaceCode == null || resourceSpaceCode.equals(currentSpaceCode)) {
            return;
        }

        try {
            ResourceSpace space = resourceSpace().getSpaceByCode(resourceSpaceCode);
            currentSpaceId = space.resource_id;
            currentSpaceCode = resourceSpaceCode;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前资源空间代号
     */
    public String getCurrentSpaceCode() {
        return currentSpaceCode;
    }

    /**
     * 获取当前资源空间Id
     */
    public long getCurrentSpaceId() {
        return currentSpaceId;
    }


    /////////////////////////////////////////////

    /**
     * 主体接口
     */
    public SubjectService subject() {
        return subjectService;
    }

    /**
     * 主体关联接口
     */
    public SubjectLinkService subjectLink() {
        return subjectLinkService;
    }


    /**
     * 资源接口
     */
    public ResourceService resource() {
        return resourceService;
    }

    /**
     * 资源关联接口
     */
    public ResourceLinkService resourceLink() {
        return resourceLinkService;
    }

    /**
     * 资源空间接口
     */
    public ResourceSpaceService resourceSpace() {
        return resourceSpaceService;
    }

    /**
     * 签权接口
     */
    public AuthService auth() {
        return authService;
    }
}
