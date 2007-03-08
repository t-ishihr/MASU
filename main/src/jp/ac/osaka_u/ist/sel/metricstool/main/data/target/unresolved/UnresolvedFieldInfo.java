package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ASTパースで取得したフィールド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo<TargetFieldInfo> implements
        VisualizableSetting, MemberSetting {

    /**
     * Unresolvedフィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type,
            final UnresolvedClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;
    }

    public TargetFieldInfo resolveUnit(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == fieldInfoManager)) {
            throw new NullPointerException();
        }

        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // 修飾子，名前，型，可視性，インスタンスメンバーかどうかを取得
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String fieldName = this.getName();
        final UnresolvedTypeInfo unresolvedFieldType = this.getType();
        TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType, usingClass, null,
                classInfoManager, fieldInfoManager, null, null);
        assert fieldType != null : "resolveTypeInfo returned null!";
        if (fieldType instanceof UnknownTypeInfo) {
            if (unresolvedFieldType instanceof UnresolvedClassReferenceInfo) {
                fieldType = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedFieldType);
                classInfoManager.add((ExternalClassInfo) fieldType);
            } else if (unresolvedFieldType instanceof UnresolvedArrayTypeInfo) {
                final UnresolvedEntityUsageInfo unresolvedArrayElement = ((UnresolvedArrayTypeInfo) unresolvedFieldType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedFieldType).getDimension();
                final ExternalClassInfo elementType = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedArrayElement);
                classInfoManager.add(elementType);
                fieldType = ArrayTypeInfo.getType(new ClassReferenceInfo(elementType), dimension);
            } else {
                assert false : "Can't resolve field type : " + unresolvedFieldType.toString();
            }
        }
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // フィールドオブジェクトを生成
        this.resolvedInfo = new TargetFieldInfo(modifiers, fieldName, fieldType, usingClass,
                privateVisible, namespaceVisible, inheritanceVisible, publicVisible, instance,
                fromLine, fromColumn, toLine, toColumn);
        fieldInfoManager.add(this.resolvedInfo);
        usingClass.addDefinedField(this.resolvedInfo);
        return this.resolvedInfo;
    }

    /**
     * このフィールドを定義している未解決クラス情報を返す
     * 
     * @return このフィールドを定義している未解決クラス情報
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを定義している未解決クラス情報をセットする
     * 
     * @param ownerClass このフィールドを定義している未解決クラス情報
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * 子クラスから参照可能かどうかを設定する
     * 
     * @param inheritanceVisible 子クラスから参照可能な場合は true，そうでない場合は false
     */
    public void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * 同じ名前空間内から参照可能かどうかを設定する
     * 
     * @param namespaceVisible 同じ名前空間から参照可能な場合は true，そうでない場合は false
     */
    public void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを設定する
     * 
     * @param privateVisible クラス内からのみ参照可能な場合は true，そうでない場合は false
     */
    public void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを設定する
     * 
     * @param publicVisible どこからでも参照可能な場合は true，そうでない場合は false
     */
    public void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * このフィールドを定義しているクラスを保存するための変数
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * クラス内からのみ参照可能かどうか保存するための変数
     */
    private boolean privateVisible;

    /**
     * 同じ名前空間から参照可能かどうか保存するための変数
     */
    private boolean namespaceVisible;

    /**
     * 子クラスから参照可能かどうか保存するための変数
     */
    private boolean inheritanceVisible;

    /**
     * どこからでも参照可能かどうか保存するための変数
     */
    private boolean publicVisible;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private boolean instance;
}
