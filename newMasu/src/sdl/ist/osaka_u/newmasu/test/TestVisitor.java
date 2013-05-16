package sdl.ist.osaka_u.newmasu.test;import java.util.HashSet;import java.util.List;import java.util.Set;import com.sun.tools.javac.util.Pair;import org.eclipse.jdt.core.dom.*;import org.eclipse.jdt.internal.compiler.ast.Argument;import sdl.ist.osaka_u.newmasu.accessor.MethodInfoAccessor;import sdl.ist.osaka_u.newmasu.accessor.VariableInfoAccessor;import sdl.ist.osaka_u.newmasu.data.dataManager.BindingManager;import sdl.ist.osaka_u.newmasu.util.NodeFinder;import sdl.ist.osaka_u.newmasu.util.NodeFinder2;public class TestVisitor extends ASTVisitor {	String name;    public boolean visit(Block node){        List<Block> res =                NodeFinder2.get(node, ASTNode.BLOCK, NodeFinder2.simpleParent(), NodeFinder2.simpleJudge());//        System.out.println(node.toString());        for( Block b : res){            System.out.println(b.toString());        }        System.out.println("---------------------------------------------");        List<Block> res2 =                NodeFinder2.get(node, ASTNode.BLOCK, NodeFinder2.simpleParent(),                        new NodeFinder2.Delegate<Pair<ASTNode, Integer>, Boolean>() {                            @Override                            public Boolean invoke(Pair<ASTNode, Integer> arg) {                                if( arg.fst.getNodeType() == arg.snd ){                                    Block b = (Block)arg.fst;                                    if( b.getLength() > 100 )                                        return true;                                }                                return false;                            }                        }                );        for( Block b : res2){            System.out.println(b.toString());        }        return true;    }	// public boolean visit(TypeDeclaration node) {	//	// name = node.getName().toString();	// Set<ASTNode> set = new HashSet<ASTNode>();	//	// System.out.println("******************************");	// System.out.println("-------extend " + name + " ------");	//	// set = BindingManager.getExtendedClass(node);	//	// for (ASTNode ast : set) {	// System.out.println(ast.toString());	// }	//	// System.out.println();	//	// System.out.println("******************************");	// System.out.println("-------" + name + " extends------");	//	// set = BindingManager.getExtendingClass(node);	//	// for (ASTNode ast : set) {	// System.out.println(ast.toString());	// }	//	// System.out.println();	//	// System.out.println("******************************");	// System.out.println("-------inner " + name + "------");	//	// set = BindingManager.getInnerClass(node);	//	// for (ASTNode ast : set) {	// System.out.println(ast.toString());	// }	//	// System.out.println();	//	//	// return true;	// }	public boolean visit(MethodDeclaration node) {//		System.out.println("******************************");		// StringBuilder sb = new StringBuilder();		// sb.append(node.getName().toString());		// sb.append("(");		// if (node.parameters() != null) {		// for (Object o : node.parameters()) {		// sb.append(o.toString());		// sb.append(",");		// }		// if (node.parameters().size() > 0)		// sb.deleteCharAt(sb.length() - 1);		// }		// sb.append(")");		// name = sb.toString();		//		// System.out.println("-------call " + name + "------");		//		// for (ASTNode ast : BindingManager.getCalleeMethods(node)) {		// System.out.println(ast.toString());		// }		//		// System.out.println("-------" + name + " call------");		//		// for (ASTNode ast : BindingManager.getCallerMethods(node)) {		// System.out.println(ast.toString());		// }		//		// System.out.println("-------" + name + "------");		// System.out.println("++overriding++");		// for (ASTNode ast : BindingManager.getOverridingMethod(node)) {		// System.out.println(ast.toString());		// }		//		// System.out.println("++overrided++");		// for (ASTNode ast : BindingManager.getOverridedMethod(node)) {		// System.out.println(ast.toString());		// }//		System.out.println("-------" + node.getName() + "------");//		for (ASTNode ast : MethodInfoAccessor.getAssignedFields(node)){//			SimpleName vname = (SimpleName) ast;//			System.out.println(VariableInfoAccessor.getDeclaringStatement(vname));//		}//		List<IVariableBinding> list = (List<IVariableBinding>) node//				.getProperty("Variable");//		if (list != null)//			for (Object o : list) {//				IVariableBinding vb = (IVariableBinding) o;//				if (vb.isField()) {//					for (ASTNode a : BindingManager.getRef().get(vb)) {//						System.out.println(NodeFinder.getDeclaringNode(a));//					}//				} else {//					ASTNode a = BindingManager.getDec().get(vb);//					System.out.println(NodeFinder.getDeclaringNode(a));////				}////				System.out.println(vb + " : " + vb.isField());//			}//		System.out.println();		return true;	}	// public boolean visit(SimpleName node) {	//	// IBinding binding = node.resolveBinding();	// if (binding != null && binding.getKind() == IBinding.VARIABLE) {	//	// if (node.isDeclaration()) {	// System.out.println("******************************");	// System.out.println("-------use " + node.toString() + "------");	// for (ASTNode ast : BindingManager.getCalleeVariable(node)) {	// System.out.println(ast);	// }	// } else {	// System.out.println("******************************");	// System.out.println("-------" + node.toString()	// + " declared------");	// ASTNode ast = BindingManager.getCallerVariable(node);	// System.out.println(ast);	// }	//	// System.out.println();	// }	// return true;	// }}