package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 for ブロックを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedForBlockInfo extends UnresolvedConditionalBlockInfo<ForBlockInfo> {

    /**
     * 外側のブロック情報を与えて，for ブロック情報を初期化
     * 
     * @param outerSpace 外側のブロック
     */
    public UnresolvedForBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.initializerExpressions = new TreeSet<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
        this.updateExpressions = new TreeSet<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * この未解決 for ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public ForBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この for文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo outerSpace = this.getOuterSpace().getResolved();

        this.resolvedInfo = new ForBlockInfo(usingClass, usingMethod, outerSpace, fromLine,
                fromColumn, toLine, toColumn);

        // 未解決ブロック文情報を解決し，解決済みオブジェクトに追加
        this.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // 未解決初期化式情報を解決し，解決済みオブジェクトに追加
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> initializerExpression : this.initializerExpressions) {
            this.resolvedInfo.addInitializerExpressions(initializerExpression.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager));
        }

        // 未解決更新式情報を追加し，解決済みオブジェクトに追加
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> updateExpression : this.updateExpressions) {
            this.resolvedInfo.addUpdateExpressions(updateExpression.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager));
        }

        // ローカル変数情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        this.resolveVariableUsages(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    /**
     * 初期化式を追加するメソッド
     * 
     * @param initializerExpression 追加する初期化式
     */
    public final void addInitializerExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initailizerExpression is null.");
        }

        this.initializerExpressions.add(initializerExpression);
    }

    /**
     * 更新式を追加するメソッド
     * 
     * @param updateExpression 追加する更新式
     */
    public final void addUpdateExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> updateExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == updateExpression) {
            throw new IllegalArgumentException("updateExpression is null.");
        }

        this.updateExpressions.add(updateExpression);
    }

    private final SortedSet<UnresolvedExpressionInfo<? extends ExpressionInfo>> initializerExpressions;

    private final SortedSet<UnresolvedExpressionInfo<? extends ExpressionInfo>> updateExpressions;

}
