package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

/**
 * <? extends A>のような型パラメータの使用を表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedExtendsTypeParameterUsageInfo extends UnresolvedTypeParameterUsageInfo {

    /**
     * 未解決基底クラス参照を与えてオブジェクトを初期化
     * 
     * @param extendsType 未解決基底クラス参照
     */
    public UnresolvedExtendsTypeParameterUsageInfo(final UnresolvedClassReferenceInfo extendsType) {

        if (null == extendsType) {
            throw new NullPointerException();
        }

        this.extendsType = extendsType;
    }

    /**
     * 未解決基底クラス参照を返す
     * 
     * @return 未解決基底クラス参照
     */
    public UnresolvedClassReferenceInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * 未解決基底クラス参照を保存するための変数
     */
    private UnresolvedClassReferenceInfo extendsType;
}
