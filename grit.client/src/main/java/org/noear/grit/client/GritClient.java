package org.noear.grit.client;

import org.noear.grit.client.impl.*;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.service.*;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;

/**
 * Grit 客户端
 *
 * @author noear
 * @since 1.0
 * */
public class GritClient {
    private static SubjectService subjectService;
    private static SubjectLinkService subjectLinkService;

    private static ResourceService resourceService;
    private static ResourceLinkService resourceLinkService;
    private static ResourceSpaceService resourceSpaceService;

    private static AuthService authService;

    private static long currentResourceSpaceId;
    private static String currentResourceSpaceCode;

    /**
     * 初始化
     */
    public static void init(DbContext db, ICacheService cache) {
        resourceService = new ResourceServiceImpl(db, cache);
        resourceSpaceService = new ResourceSpaceServiceImpl(db, cache);
        resourceLinkService = new ResourceLinkServiceImpl(db, cache);

        subjectService = new SubjectServiceImpl(db, cache);
        subjectLinkService = new SubjectLinkServiceImpl(db, cache);

        authService = new AuthServiceImpl();
    }

    /**
     * 隔离分组
     */
    public static void switchResourceSpace(String resourceSpaceCode) {
        if (resourceSpaceCode == null || resourceSpaceCode.equals(currentResourceSpaceCode)) {
            return;
        }

        try {
            ResourceSpace space = resourceSpace().getSpaceByCode( resourceSpaceCode);
            currentResourceSpaceId = space.resource_id;
            currentResourceSpaceCode = resourceSpaceCode;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前资源空间Id
     */
    public static long currentResourceSpaceId() {
        return currentResourceSpaceId;
    }

    /**
     * 当前资源空间代号
     */
    public static String currentResourceSpaceCode() {
        return currentResourceSpaceCode;
    }

    /////////////////////////////////////////////

    /**
     * 主体接口
     */
    public static SubjectService subject() {
        return subjectService;
    }

    /**
     * 主体关联接口
     */
    public static SubjectLinkService subjectLink() {
        return subjectLinkService;
    }


    /**
     * 资源接口
     */
    public static ResourceService resource() {
        return resourceService;
    }

    /**
     * 资源关联接口
     */
    public static ResourceLinkService resourceLink() {
        return resourceLinkService;
    }

    /**
     * 资源空间接口
     */
    public static ResourceSpaceService resourceSpace() {
        return resourceSpaceService;
    }

    /**
     * 签权接口
     */
    public static AuthService auth() {
        return authService;
    }

}
