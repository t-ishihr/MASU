package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if文やwhile文などのメソッド内の構造（ブロック）を表すためのクラス
 * 
 * @author higo
 * @param <T> 解決済みのブロックの型
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * このブロックの外側に位置するブロックを与えて，オブジェクトを初期化
     * 
     * @param outerSpace このブロックの外側に位置するブロック
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super();

        if (null == outerSpace) {
            throw new IllegalArgumentException("outerSpace is null");
        }

        this.outerSpace = outerSpace;
    }

    public void initBody() {

        this.statements.clear();
    }

    /**
     * このブロックが属する空間を返す
     * @return このブロックが属する空間
     */
    public UnresolvedLocalSpaceInfo<?> getOuterSpace() {
        return this.outerSpace;
    }

    /**
     * このブロックが属する空間を保存するための変数
     */
    private final UnresolvedLocalSpaceInfo<?> outerSpace;

}
