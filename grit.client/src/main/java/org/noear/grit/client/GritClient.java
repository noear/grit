package org.noear.grit.client;

import org.noear.grit.service.*;


/**
 * Grit 客户端
 *
 * @author noear
 * @since 1.0
 * */
public interface GritClient {
    /**
     * 约定Rpc路径
     */
    static final String RPC_PATH = "/grit/api/";
    /**
     * 约定Rpc路径和当前版本
     */
    static final String RPC_PATH_VER = "/grit/api/v1/";

    /**
     * 获取全局对象
     */
    static GritClient global() {
        if (GritUtil.client == null) {
            GritUtil.client = new GritClientRpcImpl();
        }

        return GritUtil.client;
    }


    /////////////////////////////////////////////

    /**
     * 设置全局对象
     */
    static void setGlobal(GritClient client) {
        GritUtil.client = client;
    }

    /**
     * 设置当前资源空间
     */
    void setCurrentSpaceByCode(String resourceSpaceCode);

    /**
     * 获取当前资源空间代号
     */
    String getCurrentSpaceCode();

    /**
     * 获取当前资源空间Id
     */
    long getCurrentSpaceId();


    /////////////////////////////////////////////

    /**
     * 资源接口
     */
    ResourceService resource();

    /**
     * 资源关联接口
     */
    ResourceLinkService resourceLink();

    /**
     * 资源架构接口
     */
    ResourceSchemaService resourceSchema();


    /////////////////////////////////////////////

    /**
     * 主体接口
     */
    SubjectService subject();

    /**
     * 主体关联接口
     */
    SubjectLinkService subjectLink();


    /////////////////////////////////////////////

    /**
     * 签权接口
     */
    AuthService auth();
}
