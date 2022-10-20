package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.FieldVisitorVo;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:27
 */
public class AlterFieldCustomVisitor extends MySqlASTVisitorAdapter {

    List<FieldVisitorVo> fieldVisitorVos = new ArrayList<>();

    private static String SQL_TEMPLATE = "ALTER TABLE {} MODIFY COLUMN {} {};";


    @Override
    public boolean visit(SQLAlterTableAlterColumn x) {
        SQLAlterTableStatement parent = (SQLAlterTableStatement) x.getParent();

        String tableName = parent.getTableSource().toString();
//        String afterField = x.getAfterColumn() == null ? "" : "AFTER " + x.getAfterColumn().toString();
//        String sql = StrUtil.format(SQL_TEMPLATE, tableName, x.getColumns().get(0).toString(), afterField);
//
//        FieldVisitorVo fieldVisitorVo = FieldVisitorVo.builder()
//                .fieldSql(sql)
//                .fullTableName(tableName)
//                .fieldName(x.getColumns().get(0).getName().toString())
//                .tableName(parent.getTableName())
//                .build();
//        indexVisitors.add(fieldVisitorVo);
        return true;
    }

    @Override
    public boolean visit(MySqlAlterTableModifyColumn x) {
        SQLAlterTableStatement parent = (SQLAlterTableStatement) x.getParent();

        String tableName = parent.getTableSource().toString();
        String afterField = x.getAfterColumn() == null ? "" : "AFTER " + x.getAfterColumn().toString();
        String sql = StrUtil.format(SQL_TEMPLATE, tableName, x.getNewColumnDefinition(), afterField);

        FieldVisitorVo fieldVisitorVo = FieldVisitorVo.builder()
                .fieldSql(sql)
                .fullTableName(tableName)
                .fieldName(x.getNewColumnDefinition().getName().toString())
                .tableName(parent.getTableName())
                .build();
        fieldVisitorVos.add(fieldVisitorVo);
        return true;
    }

    public List<FieldVisitorVo> getFieldVisitors() {
        return fieldVisitorVos;
    }

}
