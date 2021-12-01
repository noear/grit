package gritadmin.dso;

import org.noear.solon.Utils;
import org.noear.weed.DbContext;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author noear 2021/12/1 created
 */
public class InitUtils {
    /**
     * 检查一张表是否存在
     */
    private static boolean hasTable(DbContext db, String table) throws SQLException {
        Map map = db.sql("SHOW TABLES LIKE ?", table).getMap();

        if (map.size() > 0) {
            //说明有表
            if (db.table(table).selectCount() > 0) {
                //说明也有数据
                return true;
            }
        }

        return false;
    }

    public static void tryInitGrit(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/grit.sql");
        tryInitSchemaBySplitSql(db, sql);
    }


    private static void tryInitSchemaBySplitSql(DbContext db, String sql) throws Exception {
        if (Utils.isNotEmpty(sql)) {
            for (String sqlItem : sql.split(";")) {
                sqlItem = sqlItem.trim();

                if (Utils.isNotEmpty(sqlItem)) {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>: " + sqlItem);
                    db.exe(sqlItem);
                }
            }
        }
    }
}
