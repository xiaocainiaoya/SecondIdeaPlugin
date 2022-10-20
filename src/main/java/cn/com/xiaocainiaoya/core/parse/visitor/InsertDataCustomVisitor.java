package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.InsertDataVisitorVo;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:27
 */
public class InsertDataCustomVisitor extends MySqlASTVisitorAdapter {

    InsertDataVisitorVo insertDataVisitorVo = new InsertDataVisitorVo();


    @Override
    public boolean visit(MySqlInsertStatement x) {

        insertDataVisitorVo = InsertDataVisitorVo.builder()
                .id(x.getValuesList().get(0).getValues().get(0).toString())
                .fullTableName(x.getTableSource().toString())
                .tableName(x.getTableName().toString())
                .build();

        return true;
    }

    public InsertDataVisitorVo getInsertDataInfo() {
        return insertDataVisitorVo;
    }

}
