package cn.com.xiaocainiaoya.core.parse;

import cn.com.xiaocainiaoya.core.parse.visitor.*;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.*;
import cn.com.xiaocainiaoya.core.parse.vo.TableDetailInfo;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 14:53
 */
public class ParseHelper {

    public static MySqlSchemaStatVisitor createVisitor(String sql) {
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        return visitor;
    }

    public static TableDetailInfo getTableNameAndColumns(String sql){
        MySqlSchemaStatVisitor visitor = createVisitor(sql);

        Collection<TableStat.Column> columns =  visitor.getColumns();
        List<String> columnNames = new ArrayList<>();
        for(TableStat.Column column : columns){
            columnNames.add(column.getName());
        }


        Map<TableStat.Name, TableStat> tables = visitor.getTables();

        return TableDetailInfo.builder()
                .table(tables.keySet().iterator().next().getName())
                .columnNames(columnNames)
                .build();
    }

    public static List<IndexVisitorVo> getIndex(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        IndexCustomVisitor visitor = new IndexCustomVisitor();
        statement.accept(visitor);
        return visitor.getIndex();
    }

    public static List<IndexVisitorVo> getAlterIndex(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        IndexCustomVisitor visitor = new IndexCustomVisitor();
        statement.accept(visitor);
        return visitor.getIndex();
    }

    public static List<FieldVisitorVo> getField(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);

        SQLStatement statement = parser.parseStatement();
        FieldCustomVisitor visitor = new FieldCustomVisitor();
        statement.accept(visitor);
        return visitor.getFieldVisitors();
    }

    public static CreateTableVo createTable(String sql, String defaultDataBaseName){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        CreateTableCustomVisitor visitor = new CreateTableCustomVisitor(defaultDataBaseName);
        statement.accept(visitor);
        return visitor.getCreateTableVo();
    }

    public static NewCreateTableVo newCreateTable(String sql, String defaultDataBaseName){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        NewCreateTableCustomVisitor visitor = new NewCreateTableCustomVisitor(defaultDataBaseName);
        statement.accept(visitor);
        return visitor.getCreateTableVo();
    }

    public static List<FieldVisitorVo> getAlterField(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        AlterFieldCustomVisitor visitor = new AlterFieldCustomVisitor();
        statement.accept(visitor);
        return visitor.getFieldVisitors();
    }

    public static List<DeleteFieldVo> getDeleteFiled(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        DeleteFieldCustomVisitor visitor = new DeleteFieldCustomVisitor();
        statement.accept(visitor);
        return visitor.getFieldVisitors();
    }

    public static InsertDataVisitorVo getInsertInfo(String sql){
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        InsertDataCustomVisitor visitor = new InsertDataCustomVisitor();
        statement.accept(visitor);

        return visitor.getInsertDataInfo();
    }

    public static void main(String[] args) {
        String sql = "ALTER TABLE `gpx_bid`.`bid_acquisition` \n" +
                "MODIFY COLUMN `id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT " +
                "'主键' FIRST,\n" +
                "MODIFY COLUMN `project_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL " +
                "DEFAULT '' COMMENT '项目ID' AFTER `id`;";

        String sql2 = "ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel`\n" +
                "ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`,\n" +
                "ADD COLUMN `cc` varchar(255) NULL COMMENT '2' AFTER `upda`;";

        String sql3 = "INSERT INTO `gpx_auction`.`online_auction_quotation` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, " +
                "`SUPPLIER_ID`, `SUPPLIER_NAME`, `TOTAL_PRICE`, `FLOATING_RATE`, `BID_NUMBERS`, `IS_STATUS`, " +
                "`INVALID_REASON`, `ZONE_CODE`, `ZONE_NAME`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, " +
                "`UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `QUOTATION_TIME`) VALUES " +
                "('2c9180827723fdb40178159e68920177', '8a80838077d4542c0177f0f87b6a27d9', " +
                "'8a80838077d4542c0177f0f87c1127df', " +
                "'375df9ebe71b9f226229b3589d77d82286384dc2e83219650e9dfa593f6cb80be09fb262a02b76e931530702dd7f2d44', " +
                "'2e364010136d754970cfe8d4c16ad7b2', 'cde06bebfee42f5cfa8061880c1d5d4b', 0.0000, 1, 1, NULL, " +
                "'350199001', '海峡纵横供应链采购服务平台', " +
                "'9394dc75ac7ecdff12eac4a2593b8153ef2e8c2a1dd2bc11dbd4308101ded502e09fb262a02b76e931530702dd7f2d44', " +
                "'15a8b7819e41a4714d9181fb1efcc99f', '2021-03-09 14:13:26.290', '2021-03-09 14:13:35', " +
                "'65321a16defd986afa8b71571d1ed0650a5310651f56b6b7b99008d93fc105fce09fb262a02b76e931530702dd7f2d44', " +
                "'7c17bf1190d957b44a359fd5bfcc9053', '2021-03-09 14:13:35.366');\n" +
                "INSERT INTO `gpx_auction`.`online_auction_quotation` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, " +
                "`SUPPLIER_ID`, `SUPPLIER_NAME`, `TOTAL_PRICE`, `FLOATING_RATE`, `BID_NUMBERS`, `IS_STATUS`, " +
                "`INVALID_REASON`, `ZONE_CODE`, `ZONE_NAME`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, " +
                "`UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `QUOTATION_TIME`) VALUES " +
                "('2c9180827723fdb40178159e953c018d', '8a80838077d4542c0177f0f87b6a27d9', " +
                "'8a80838077d4542c0177f0f87c1127df', " +
                "'375df9ebe71b9f226229b3589d77d82286384dc2e83219650e9dfa593f6cb80be09fb262a02b76e931530702dd7f2d44', " +
                "'2e364010136d754970cfe8d4c16ad7b2', '83b9ba45f6fdb4d07c7c9889f9f3725b', 26121.8900, 2, 1, NULL, " +
                "'350199001', '海峡纵横供应链采购服务平台', " +
                "'9394dc75ac7ecdff12eac4a2593b8153ef2e8c2a1dd2bc11dbd4308101ded502e09fb262a02b76e931530702dd7f2d44', " +
                "'15a8b7819e41a4714d9181fb1efcc99f', '2021-03-09 14:13:37.724', '2021-03-09 15:39:11', " +
                "'65321a16defd986afa8b71571d1ed0650a5310651f56b6b7b99008d93fc105fce09fb262a02b76e931530702dd7f2d44', " +
                "'7c17bf1190d957b44a359fd5bfcc9053', '2021-03-09 15:39:11.255');\n" +
                "INSERT INTO `gpx_auction`.`online_auction_quotation` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, " +
                "`SUPPLIER_ID`, `SUPPLIER_NAME`, `TOTAL_PRICE`, `FLOATING_RATE`, `BID_NUMBERS`, `IS_STATUS`, " +
                "`INVALID_REASON`, `ZONE_CODE`, `ZONE_NAME`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, " +
                "`UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `QUOTATION_TIME`) VALUES " +
                "('2c9180827723fdb4017815ed0fcf01a8', '8a80838077d4542c0177f0f87b6a27d9', " +
                "'8a80838077d4542c0177f0f87c1127df', " +
                "'375df9ebe71b9f226229b3589d77d82286384dc2e83219650e9dfa593f6cb80be09fb262a02b76e931530702dd7f2d44', " +
                "'2e364010136d754970cfe8d4c16ad7b2', NULL, NULL, 3, 0, NULL, '350199001', '海峡纵横供应链采购服务平台', " +
                "'9394dc75ac7ecdff12eac4a2593b8153ef2e8c2a1dd2bc11dbd4308101ded502e09fb262a02b76e931530702dd7f2d44', " +
                "'15a8b7819e41a4714d9181fb1efcc99f', '2021-03-09 15:39:20.911', '2021-03-09 15:39:21', " +
                "'65321a16defd986afa8b71571d1ed0650a5310651f56b6b7b99008d93fc105fce09fb262a02b76e931530702dd7f2d44', " +
                "'7c17bf1190d957b44a359fd5bfcc9053', NULL);";

        InsertDataVisitorVo fields = getInsertInfo(sql3);

        //List<IndexVisitorVo> indexVisitorVos = getIndex(sql2);

        //TableDetailInfo tableDetailInfo = getTableNameAndColumns(sql);;
        System.out.println(sql2);
    }

}
