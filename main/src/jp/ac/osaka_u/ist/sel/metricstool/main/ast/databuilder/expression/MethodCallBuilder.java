package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class MethodCallBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public MethodCallBuilder(ExpressionElementManager expressionManager, BuildDataManager buildDataManager) {
        super(expressionManager);
        this.buildDataManager = buildDataManager;
    }

    protected void afterExited(AstVisitEvent event){
        AstToken token = event.getToken();
        if (token.isMethodCall()){
            buildMethodCall();
        }
    }
    
    protected void buildMethodCall(){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length > 0){
            if (elements[0] instanceof IdentifierElement){
                IdentifierElement callee = (IdentifierElement)elements[0];
                
                callee = callee.resolveAsCalledMethod(buildDataManager);
                
                UnresolvedMethodCall methodCall = new UnresolvedMethodCall(callee.getOwnerType(),callee.getName(),false);
                for(int i=1; i < elements.length; i++){
                    ExpressionElement argment = elements[i];
                    UnresolvedTypeInfo argmentType;
                    if (argment instanceof IdentifierElement){
                        argmentType = ((IdentifierElement)argment).resolveAsReferencedVariable(buildDataManager);
                    } else if (argment.equals(InstanceSpecificElement.THIS)){
                        argmentType = InstanceSpecificElement.getThisInstanceType(buildDataManager);
                    } else {
                        argmentType = argment.getType();
                    }
                     
                    methodCall.addParameterType(argmentType);
                }
                
                pushElement(new MethodCallElement(methodCall));
                buildDataManager.addMethodCall(methodCall);
            }
            
        } else {
            assert(false) : "Illegal state: callee element was not found.";
        }
    }
    
    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isMethodCall();
    }
    
    private final BuildDataManager buildDataManager;

}
