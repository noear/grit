package org.noear.grit.server.api.integration;

import org.noear.solon.Solon;
import org.noear.wood.DbContext;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.1
 */
public class GritServerUpdate {
    protected static void tryUpdate() {
        DbContext db = Solon.context().getBean("grit.db");
        if (db == null) {
            return;
        }

        try {
            db.initMetaData();

            update_20220307(db);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void update_20220307(DbContext db) throws Throwable {
        if (existsColumn(db, "grit_resource", "guid") == false) {
            db.exe("ALTER TABLE `grit_resource` ADD COLUMN `guid` varchar(40) NULL AFTER `attrs`;");
            db.exe("UPDATE `grit_resource` SET `guid`= UUID() WHERE `guid` IS NULL;");
            db.exe("ALTER TABLE `grit_resource` MODIFY COLUMN `guid` varchar(40) NOT NULL COMMENT 'guid' AFTER `attrs`;");
            db.exe("ALTER TABLE `grit_resource` ADD UNIQUE INDEX `IX_grit_resource__guid`(`guid`) USING BTREE;");

        }

        if (existsColumn(db, "grit_subject", "guid") == false) {
            db.exe("ALTER TABLE `grit_subject` ADD COLUMN `guid` varchar(40) NULL AFTER `attrs`;");
            db.exe("UPDATE `grit_subject` SET `guid`= UUID() WHERE `guid` IS NULL;");
            db.exe("ALTER TABLE `grit_subject` MODIFY COLUMN `guid` varchar(40) NOT NULL COMMENT 'guid' AFTER `attrs`;");
            db.exe("ALTER TABLE `grit_subject` ADD UNIQUE INDEX `IX_grit_subject__guid`(`guid`) USING BTREE;");
        }
    }

    private static boolean existsColumn(DbContext db, String table, String column) throws SQLException {
        return db.table("information_schema.columns")
                .whereEq("TABLE_SCHEMA", db.schema())
                .andEq("table_name", table)
                .andEq("column_name", column)
                .selectExists();
    }
}
