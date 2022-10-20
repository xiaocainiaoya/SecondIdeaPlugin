package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.IndexVisitorVo;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 15:34
 */
public class IndexCustomVisitor extends SQLASTVisitorAdapter {

    List<IndexVisitorVo> indexVisitors = new ArrayList<>();

    private static String SQL_TEMPLATE = "ALTER TABLE {} ADD INDEX {}({}) USING {}";

    @Override
    public boolean visit(SQLDropIndexStatement x) {

        return true;
    }

    @Override
    public boolean visit(SQLAlterTableDropIndex x) {
        return true;
    }

    @Override
    public boolean visit(SQLAlterTableAddIndex x) {

        StringBuilder columnNameSb = new StringBuilder();
        for(SQLSelectOrderByItem item : x.getItems()){
            columnNameSb.append(item.getExpr()).append(",");
        }
        String columnName = columnNameSb.substring(0,columnNameSb.length()-1);
        String tableFullName = ((SQLAlterTableStatement)x.getParent()).getTableSource().toString();

        String sql = StrUtil.format(SQL_TEMPLATE, tableFullName, x.getName().toString(), columnName, x.getType());


        IndexVisitorVo indexVisitorVo = IndexVisitorVo.builder()
                .indexName(x.getName().toString())
                .indexSql(sql)
                .tableName(((SQLAlterTableStatement)x.getParent()).getTableName())
                .fullTableName(tableFullName)
                .build();
        indexVisitors.add(indexVisitorVo);
        return true;
    }

    @Override
    public boolean visit(SQLAlterTableRenameIndex x) {
        return true;
    }

    @Override
    public boolean visit(SQLCreateIndexStatement x) {
        return true;
    }

    public List<IndexVisitorVo> getIndex(){
        return indexVisitors;
    }

}
