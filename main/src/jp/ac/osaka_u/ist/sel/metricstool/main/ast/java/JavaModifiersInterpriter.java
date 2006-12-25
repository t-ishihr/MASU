package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ModifiersInterpriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.VisualizableSetting;

public class JavaModifiersInterpriter implements ModifiersInterpriter{
    public void interprit(ModifierInfo[] modifiers, VisualizableSetting element) {
        analyze(modifiers);
        
        element.setPublicVisible(isPublicAccessible());
        element.setInheritanceVisible(isInheritanceAccessible());
        element.setNamespaceVisible(isNameSpaceAccessible());
        element.setPrivateVibible(isOnlyClassInsideAccessible());
    }
    
    private void analyze(ModifierInfo[] modifiers){
        reset();
        
        for(ModifierInfo info : modifiers){
            if (info.equals(publicModifier)){
                this.publicAccess = true;
            } else if (info.equals(protectedModifier)){
                this.inheritanceAccess = true;
            } else if (info.equals(privateModifier)){
                this.nameSpaceAccess = false;
            } else if (info.equals(staticModifier)){
                this.staticMember = true;
            } else if (info.equals(finalModifier)){
                this.reassignable = false;
            } else if (info.equals(abstractModifier)){
                this.pureVirtual = true;
            }
        }
    }
    
    private boolean isInheritanceAccessible() {
        return inheritanceAccess || publicAccess;
    }

    private boolean isNameSpaceAccessible() {
        return nameSpaceAccess || publicAccess;
    }

    private boolean isPublicAccessible() {
        return publicAccess;
    }

    private boolean isModifiable() {
        //Javaは常にデータ自体の編集操作は可能
        return true;
    }

    private boolean isStaticMember() {
        return staticMember;
    }
    
    private boolean isReassignable() {
        return reassignable;
    }
    
    private boolean isInheritable(){
        return isReassignable();
    }
    
    private boolean isOnlyClassInsideAccessible() {
        return !publicAccess && ! nameSpaceAccess && !inheritanceAccess;
    }

    private boolean isPureVirtual() {
        return pureVirtual;
    }
    
    private void reset(){
        publicAccess = false;
        nameSpaceAccess = true;
        inheritanceAccess = false;
        staticMember = false;
        reassignable = true;
    }
    
    private boolean publicAccess = false;
    private boolean nameSpaceAccess = true;
    private boolean inheritanceAccess = false;
    private boolean staticMember = false;
    private boolean reassignable = true;
    private boolean pureVirtual = false;
    
    private static final ModifierInfo finalModifier = ModifierInfo.getModifierInfo("final");
    private static final ModifierInfo publicModifier = ModifierInfo.getModifierInfo("public");
    private static final ModifierInfo protectedModifier = ModifierInfo.getModifierInfo("protected");
    private static final ModifierInfo privateModifier = ModifierInfo.getModifierInfo("private");
    private static final ModifierInfo staticModifier = ModifierInfo.getModifierInfo("static");
    private static final ModifierInfo abstractModifier = ModifierInfo.getModifierInfo("abstract");
    
}
