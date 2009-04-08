package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 * 変数宣言文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class VariableDeclarationStatementInfo extends SingleStatementInfo implements ConditionInfo {

    /**
     * 宣言されている変数，初期化式，位置情報を与えて初期化
     * 宣言されている変数が初期化されている場合，このコンストラクタを使用する
     * 
     * @param variableDeclaration 宣言されているローカル変数
     * @param initializationExpression 初期化式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public VariableDeclarationStatementInfo(final LocalVariableUsageInfo variableDeclaration,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(variableDeclaration.getUsedVariable().getDefinitionUnit(), fromLine, fromColumn,
                toLine, toColumn);

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.variableDeclaration = variableDeclaration;
        this.variableDeclaration.setOwnerExecutableElement(this);
        this.variableDeclaration.getUsedVariable().setDeclarationStatement(this);

        if (null != initializationExpression) {
            this.initializationExpression = initializationExpression;
        } else {

            final LocalSpaceInfo ownerSpace = variableDeclaration.getUsedVariable()
                    .getDefinitionUnit();

            // ownerSpaceInfoがメソッドまたはコンストラクタの時
            if (ownerSpace instanceof CallableUnitInfo) {
                this.initializationExpression = new EmptyExpressionInfo(
                        (CallableUnitInfo) ownerSpace, toLine, toColumn - 1, toLine, toColumn - 1);
            }

            // ownerSpaceInfoがブロック文の時
            else if (ownerSpace instanceof BlockInfo) {
                final CallableUnitInfo ownerMethod = ((BlockInfo) ownerSpace).getOwnerMethod();
                this.initializationExpression = new EmptyExpressionInfo(ownerMethod, toLine, toColumn - 1,
                        toLine, toColumn - 1);
            }
            
            // それ以外の時はエラー
            else{
                throw new IllegalStateException();
            }
        }

        this.initializationExpression.setOwnerExecutableElement(this);

    }

    /**
     * この宣言文で宣言されている変数を返す
     * 
     * @return この宣言文で宣言されている変数
     */
    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * 宣言時の変数使用を返す
     * @return 宣言時の変数使用
     */
    public final LocalVariableUsageInfo getDeclaration() {
        return this.variableDeclaration;
    }

    /**
     * 宣言されている変数の初期化式を返す
     * 
     * @return 宣言されている変数の初期化式．初期化されてい場合はnull
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * 宣言されている変数が初期化されているかどうかを返す
     * 
     * @return 宣言されている変数が初期化されていればtrue
     */
    public boolean isInitialized() {
        return !(this.initializationExpression instanceof EmptyExpressionInfo);
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        usages.add(this.variableDeclaration);
        if (this.isInitialized()) {
            usages.addAll(this.getInitializationExpression().getVariableUsages());
        }

        return Collections.unmodifiableSet(usages);
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getDeclaredLocalVariable());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.isInitialized() ? this.getInitializationExpression().getCalls()
                : CallInfo.EmptySet;
    }

    /**
     * この変数宣言文のテキスト表現（String型）を返す
     * 
     * @return この変数宣言文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final LocalVariableInfo variable = this.getDeclaredLocalVariable();
        final TypeInfo type = variable.getType();
        sb.append(type.getTypeName());

        sb.append(" ");

        sb.append(variable.getName());

        if (this.isInitialized()) {

            sb.append(" = ");
            final ExpressionInfo expression = this.getInitializationExpression();
            sb.append(expression.getText());
        }

        sb.append(";");

        return sb.toString();
    }

    /**
     * 宣言されている変数の型を返す
     * @return 宣言されている変数の型
     */
    public TypeInfo getType() {
        return this.variableDeclaration.getType();
    }

    /**
     * 宣言されている変数を表すフィールド
     */
    private final LocalVariableUsageInfo variableDeclaration;

    /**
     * 宣言されている変数の初期化式を表すフィールド
     */
    private final ExpressionInfo initializationExpression;

}
