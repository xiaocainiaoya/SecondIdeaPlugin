package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.DeleteFieldVo;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:27
 */
public class DeleteFieldCustomVisitor extends MySqlASTVisitorAdapter {

    List<DeleteFieldVo> deleteFieldVos = new ArrayList<>();

    private static String SQL_TEMPLATE = "ALTER TABLE {} DROP COLUMN {} {};";

    //ALTER TABLE `gpe_archive`.`archive_attachment`
    //DROP COLUMN `update_user_name`,
    //DROP COLUMN `update_time`;

    @Override
    public boolean visit(SQLAlterTableDropColumnItem x) {
        String field = x.getColumns().get(0).toString();

        SQLAlterTableStatement parent = (SQLAlterTableStatement) x.getParent();
        String tableName = parent.getTableSource().toString();

        deleteFieldVos.add(DeleteFieldVo.builder()
                .field(field)
                .fullTableName(tableName)
                .tableName(parent.getTableName())
                .build());

        return true;
    }

    public List<DeleteFieldVo> getFieldVisitors() {
        return deleteFieldVos;
    }

}
