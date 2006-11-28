package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッドの情報を保有するクラス． 以下の情報を持つ．
 * <ul>
 * <li>メソッド名</li>
 * <li>修飾子</li>
 * <li>返り値の型</li>
 * <li>引数のリスト</li>
 * <li>行数</li>
 * <li>コントロールグラフ（しばらくは未実装）</li>
 * <li>ローカル変数</li>
 * <li>所属しているクラス</li>
 * <li>呼び出しているメソッド</li>
 * <li>呼び出されているメソッド</li>
 * <li>オーバーライドしているメソッド</li>
 * <li>オーバーライドされているメソッド</li>
 * <li>参照しているフィールド</li>
 * <li>代入しているフィールド</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class MethodInfo implements Comparable<MethodInfo> {

    /**
     * メソッドオブジェクトを初期化する． 以下の情報が引数として与えられなければならない．
     * <ul>
     * <li>メソッド名</li>
     * <li>修飾子</li>
     * <li>シグネチャ</li>
     * <li>所有しているクラス</li>
     * <li>コンストラクタかどうか</li>
     * </ul>
     * 
     * @param name メソッド名
     * @param returnType 返り値の型．コンストラクタの場合は，そのクラスの型を与える．
     * @param ownerClass 所有しているクラス
     * @param constructor コンストラクタかどうか．コンストラクタの場合は true,そうでない場合は false．
     */
    public MethodInfo(final String name, final TypeInfo returnType, final ClassInfo ownerClass,
            final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.ownerClass = ownerClass;
        this.returnType = returnType;
        this.constructor = constructor;

        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * このメソッドで定義されているローカル変数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param localVariable 追加する引数
     */
    public void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * このメソッドの引数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param parameter 追加する引数
     */
    public void addParameter(final ParameterInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * このメソッドが呼び出しているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param callee 追加する呼び出されるメソッド
     */
    public void addCallee(final MethodInfo callee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == callee) {
            throw new NullPointerException();
        }

        this.callees.add(callee);
    }

    /**
     * このメソッドを呼び出しているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param caller 追加する呼び出すメソッド
     */
    public void addCaller(final MethodInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            throw new NullPointerException();
        }

        this.callers.add(caller);
    }

    /**
     * このメソッドがオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overridee 追加するオーバーライドされているメソッド
     */
    public void addOverridee(final MethodInfo overridee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overridee) {
            throw new NullPointerException();
        }

        this.overridees.add(overridee);
    }

    /**
     * このメソッドをオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overrider 追加するオーバーライドしているメソッド
     * 
     */
    public void addOverrider(final MethodInfo overrider) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overrider) {
            throw new NullPointerException();
        }

        this.overriders.add(overrider);
    }

    /**
     * このメソッドが参照している変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param referencee 追加する参照されている変数
     */
    public void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * このメソッドが代入を行っている変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param assignmentee 追加する代入されている変数
     */
    public void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
    }

    /**
     * メソッド間の順序関係を定義するメソッド．以下の順序で順序を決める．
     * <ol>
     * <li>メソッドを定義しているクラスの名前空間名</li>
     * <li>メソッドを定義しているクラスのクラス名</li>
     * <li>メソッド名</li>
     * <li>メソッドの引数の個数</li>
     * <li>メソッドの引数の型（第一引数から順番に）</li>
     */
    public int compareTo(final MethodInfo method) {

        if (null == method) {
            throw new NullPointerException();
        }

        // クラスオブジェクトの compareTo を用いる．
        // クラスの名前空間名，クラス名が比較に用いられている．
        ClassInfo ownerClass = this.getOwnerClass();
        ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        } else {

            // メソッド名で比較
            String name = this.getName();
            String correspondName = method.getName();
            final int methodNameOrder = name.compareTo(correspondName);
            if (methodNameOrder != 0) {
                return methodNameOrder;
            } else {

                // 引数の個数で比較
                final int parameterNumber = this.getParameterNumber();
                final int correspondParameterNumber = method.getParameterNumber();
                if (parameterNumber < correspondParameterNumber) {
                    return 1;
                } else if (parameterNumber > correspondParameterNumber) {
                    return -1;
                } else {

                    // 引数の型で比較．第一引数から順番に．
                    Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
                    Iterator<ParameterInfo> correspondParameterIterator = method.getParameters().iterator();
                    while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                        ParameterInfo parameter = parameterIterator.next();
                        ParameterInfo correspondParameter = correspondParameterIterator.next();
                        String typeName = parameter.getName();
                        String correspondTypeName = correspondParameter.getName();
                        final int typeOrder = typeName.compareTo(correspondTypeName);
                        if (typeOrder != 0) {
                            return typeOrder;
                        }
                    }

                    return 0;
                }

            }
        }
    }

    /**
     * このメソッドがコンストラクタかどうかを返す．
     * 
     * @return コンストラクタである場合は true，そうでない場合は false
     */
    public boolean isConstuructor() {
        return this.constructor;
    }

    /**
     * このメソッドの名前を返す
     * 
     * @return メソッド名
     */
    public String getName() {
        return this.name;
    }

    /**
     * このメソッドの引数の数を返す
     * 
     * @return このメソッドの引数の数
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * このメソッドの返り値の型を返す
     * 
     * @return 返り値の型
     */
    public TypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * このメソッドで定義されているローカル変数の SortedSet を返す．
     * 
     * @return このメソッドで定義されているローカル変数の SortedSet
     */
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * このメソッドの引数の List を返す．
     * 
     * @return このメソッドの引数の List
     */
    public List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * このメソッドの行数を返す
     * 
     * @return このメソッドの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * このメソッドを定義しているクラスを返す．
     * 
     * @return このメソッドを定義しているクラス
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このメソッドが呼び出しているメソッドの SortedSet を返す．
     * 
     * @return このメソッドが呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        return Collections.unmodifiableSortedSet(this.callees);
    }

    /**
     * このメソッドを呼び出しているメソッドの SortedSet を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

    /**
     * このメソッドがオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドがオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverridees() {
        return Collections.unmodifiableSortedSet(this.overridees);
    }

    /**
     * このメソッドをオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドをオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverriders() {
        return Collections.unmodifiableSortedSet(this.overriders);
    }

    /**
     * このメソッドが参照しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが参照しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getReferencees() {
        return Collections.unmodifiableSortedSet(this.referencees);
    }

    /**
     * このメソッドが代入しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが代入しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getAssignmentees() {
        return Collections.unmodifiableSortedSet(this.assignmentees);
    }

    /**
     * メソッド名を保存するための変数
     */
    private final String name;

    /**
     * 修飾子を保存するための変数
     */
    // TODO 修飾子を保存するための変数を定義する
    /**
     * 返り値の型を保存するための変数
     */
    private TypeInfo returnType;

    /**
     * 引数のリストの保存するための変数
     */
    private final List<ParameterInfo> parameters;

    /**
     * 行数を保存するための変数
     */
    private int loc;

    /**
     * 所属しているクラスを保存するための変数
     */
    private final ClassInfo ownerClass;

    /**
     * このメソッドの内部で定義されているローカル変数
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * このメソッドが呼び出しているメソッド一覧を保存するための変数
     */
    private final SortedSet<MethodInfo> callees;

    /**
     * このメソッドを呼び出しているメソッド一覧を保存するための変数
     */
    private final SortedSet<MethodInfo> callers;

    /**
     * このメソッドがオーバーライドしているメソッド一覧を保存するための変数
     */
    private final SortedSet<MethodInfo> overridees;

    /**
     * オーバーライドされているメソッドを保存するための変数
     */
    private final SortedSet<MethodInfo> overriders;

    /**
     * 参照しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> referencees;

    /**
     * 代入しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> assignmentees;

    /**
     * このメソッドがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;
}
