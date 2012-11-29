package sdl.ist.osaka_u.newmasu.AST;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import sdl.ist.osaka_u.newmasu.dataManager.AnonymousIDManager;
import sdl.ist.osaka_u.newmasu.dataManager.CallHierachy;
import sdl.ist.osaka_u.newmasu.util.Output;

/**
 * Visits elements of the AST tree
 * 
 * @author s-kimura
 * 
 */
public class ASTVisitorImpl extends ASTVisitor {

	private CompilationUnit unit = null;
	private String filePath = null;
	private CallHierachy callHierachy = null;

	public ASTVisitorImpl(String path, CallHierachy callHierachy) {
		this.filePath = path;
		this.callHierachy = callHierachy;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.unit = node;

		// System.out.println("[[[ " + node.);

		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {

		IMethodBinding binding = node.resolveMethodBinding();

		if (binding == null) {
			System.err.println("Unresolved bindings : " + node);
		} else {
			callHierachy.addRelation(getFullQualifiedName(node),
					getFullQualifiedName(binding));
		}
		System.out.println("    to:  " + getFullQualifiedName(binding));
		System.out.println("  from:  " + getFullQualifiedName(node));

		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		//
		// final ITypeBinding bind = node.resolveBinding();
		// System.out.println(bind.getQualifiedName());

		return super.visit(node);
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// ITypeBinding binding = node.resolveBinding();
		// IMethodBinding mBind = null;
		//
		// String name = "";
		// while (binding != null) {
		// String className = binding.getQualifiedName().equals("") ? "."
		// + binding.getName() : binding.getQualifiedName();
		// if (binding.isAnonymous()) {
		// className = "." + AnonymousIDManager.getID(binding);
		// }
		// name = className + name;
		//
		// mBind = binding.getDeclaringMethod();
		// if (mBind != null)
		// name = "#" + mBind.getName() + name;
		//
		// binding = binding.getDeclaringClass();
		// }
		//
		// System.out.println(name);
		// //
		// // final ITypeBinding bind = node.resolveBinding();
		// // System.out.println(bind.getQualifiedName());

		// String str = getFullQualifiedName(node, "");
		// System.out.println(str);

		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		String str = getFullQualifiedName(node);
		System.out.println(str);

		return super.visit(node);
	}

	private String getFullQualifiedName(final ASTNode __node) {

		ASTNode tmpNode = __node;

		// 内部クラスなどの問題を解決するため，一度リストに格納する
		final ArrayList<IBinding> bindList = new ArrayList<IBinding>();

		while (true) {

			if (tmpNode == null)
				break;

			switch (tmpNode.getNodeType()) {
			case ASTNode.ANONYMOUS_CLASS_DECLARATION: {
				final AnonymousClassDeclaration anon = (AnonymousClassDeclaration) tmpNode;
				final ITypeBinding binding = anon.resolveBinding();
				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve("Anonymous");

				break;
			}

			case ASTNode.METHOD_DECLARATION: {
				final MethodDeclaration md = (MethodDeclaration) tmpNode;
				final IMethodBinding binding = md.resolveBinding();
				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(md.getName().getFullyQualifiedName());

				break;
			}

			case ASTNode.TYPE_DECLARATION: {
				final TypeDeclaration type = (TypeDeclaration) tmpNode;
				final ITypeBinding binding = type.resolveBinding();

				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(type.getName().getFullyQualifiedName());

				break;
			}

			case ASTNode.VARIABLE_DECLARATION_FRAGMENT: {
				final VariableDeclarationFragment type = (VariableDeclarationFragment) tmpNode;
				final IVariableBinding binding = type.resolveBinding();

				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(type.getName().getFullyQualifiedName());

				break;
			}

			default:
				// None
				break;
			}

			tmpNode = tmpNode.getParent();
		}

		return getNameFromBindingList(bindList);
	}

	private String getFullQualifiedName(final IBinding bind) {
		// 内部クラスなどの問題を解決するため，一度リストに格納する
		final ArrayList<IBinding> bindList = new ArrayList<IBinding>();
		recursiveName(bind, bindList);

		return getNameFromBindingList(bindList);
	}

	private void recursiveName(final IBinding bind,
			final ArrayList<IBinding> bindList) {
		if (bind == null)
			return;

		switch (bind.getKind()) {
		case IBinding.TYPE: {
			final ITypeBinding type = (ITypeBinding) bind;
			if (type != null) {
				bindList.add(type);
				recursiveName(type.getDeclaringClass(), bindList);
				recursiveName(type.getDeclaringMethod(), bindList);
			} else
				Output.cannotResolve(bind.getName());
			break;
		}

		case IBinding.METHOD: {
			final IMethodBinding mbind = (IMethodBinding) bind;
			if (mbind != null) {
				bindList.add(mbind);
				recursiveName(mbind.getDeclaringClass(), bindList);
			} else
				Output.cannotResolve(bind.getName());
			break;
		}

		case IBinding.VARIABLE: {
			final IVariableBinding vbind = (IVariableBinding) bind;

			if (vbind != null) {
				bindList.add(vbind);
				recursiveName(vbind.getDeclaringClass(), bindList);
				recursiveName(vbind.getDeclaringMethod(), bindList);
			} else
				Output.cannotResolve(bind.getName());
			break;
		}

		default:
			// None
			break;
		}

	}

	private String getNameFromBindingList(ArrayList<IBinding> bindList) {
		String str = "";
		for (int i = bindList.size() - 1; 0 <= i; i--) {
			final IBinding bind = bindList.get(i);
			if (i == bindList.size() - 1) { // トップレベルの要素のみ，getQualifiedNameする
				if (bind.getKind() != IBinding.TYPE)
					Output.err("Invalid ASTNode order " + bind.getName());
				else {
					final ITypeBinding type = (ITypeBinding) bind;
					str += type.getQualifiedName();
				}
			} else {
				switch (bind.getKind()) {
				case IBinding.TYPE:
					str += ".";

					final ITypeBinding type = (ITypeBinding) bind;
					if (type.isAnonymous())
						str += AnonymousIDManager.getID(type);
					else
						str += bind.getName();
					break;
				case IBinding.METHOD:
					str += "#";

					final IMethodBinding method = (IMethodBinding) bind;
					str += getMethodNameWithParams(method);
					break;
				case IBinding.VARIABLE:
					str += "$";
					str += bind.getName();
					break;
				}
			}
		}
		return str;
	}

	private String getMethodNameWithParams(IMethodBinding binding) {
		StringBuilder buf = new StringBuilder();
		// メソッド名を追加
		buf.append(binding.getName());
		// メソッド名と引数一覧の区切り文字を追加 ("(")
		buf.append('(');
		// 引数の型一覧を追加
		ITypeBinding[] params = binding.getParameterTypes();
		if (params.length > 0) {
			buf.append(params[0].getErasure().getQualifiedName());
			for (int i = 1; i < params.length; i++) {
				buf.append(',');
				buf.append(params[i].getErasure().getQualifiedName());
			}
		}
		buf.append(')');

		return buf.toString();
	}

}