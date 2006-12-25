package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class JavaEnumElementStateManager extends StackedAstVisitStateManager<JavaEnumElementStateManager.STATE>{
    public static enum ENUM_ELEMENT_STATE implements StateChangeEventType{
        ENTER_ENUM_ELEMENT,
        ENTER_ENUM_ANONYMOUS_CLASS,
        EXIT_ENUM_ANONYMOUS_CLASS,
        EXIT_ENUM_ELEMENT
    }
    
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.ENUM_CONSTANT)){
            this.state = STATE.ELEMENT;
            fireStateChangeEvent(ENUM_ELEMENT_STATE.ENTER_ENUM_ELEMENT,event);
        } else if (STATE.ELEMENT == this.state && token.isClassBlock()){
            this.state = STATE.ANONYMOUS_CLASS;
            fireStateChangeEvent(ENUM_ELEMENT_STATE.ENTER_ENUM_ANONYMOUS_CLASS,event);
        }
    }
    
    public void exited(AstVisitEvent event){
        super.exited(event);
        
        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.ENUM_CONSTANT)){
            fireStateChangeEvent(ENUM_ELEMENT_STATE.EXIT_ENUM_ELEMENT,event);
        } else if (STATE.ELEMENT == this.state && token.isClassBlock()){
            fireStateChangeEvent(ENUM_ELEMENT_STATE.EXIT_ENUM_ANONYMOUS_CLASS,event);
        }
    }
    
    protected static enum STATE{
        NOT,ELEMENT,ANONYMOUS_CLASS,
    }
    
    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.equals(JavaAstToken.ENUM_CONSTANT) || token.isClassBlock();
    }

    @Override
    protected STATE getState() {
        return this.state;
    }

    @Override
    protected void setState(STATE state) {
        this.state = state;
        
    }
    
    private STATE state = STATE.NOT;

}
