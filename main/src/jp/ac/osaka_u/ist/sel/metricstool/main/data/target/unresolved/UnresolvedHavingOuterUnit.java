package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


/**
 * 未解決な，外側のユニットを持つことを表すインターフェース
 * 
 * @author higo
 *
 */
public interface UnresolvedHavingOuterUnit {

    UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit();
}
