package org.litespring.util;

public abstract class MethodInvoker {
    /**
     * Algorithm that judges the match between the declared parameter types of a candidate method
     * and a specific list of arguments that this method is supposed to be invoked with.
     * <p>Determines a weight that represents the class hierarchy difference between types and
     * arguments. A direct match, i.e. type Integer -> arg of class Integer, does not increase
     * the result - all direct matches means weight 0. A match between type Object and arg of
     * class Integer would increase the weight by 2, due to the superclass 2 steps up in the
     * hierarchy (i.e. Object) being the last one that still matches the required type Object.
     * Type Number and class Integer would increase the weight by 1 accordingly, due to the
     * superclass 1 step up the hierarchy (i.e. Number) still matching the required type Number.
     * Therefore, with an arg of type Integer, a constructor (Integer) would be preferred to a
     * constructor (Number) which would in turn be preferred to a constructor (Object).
     * All argument weights get accumulated.
     * <p>Note: This is the algorithm used by MethodInvoker itself and also the algorithm
     * used for constructor and factory method selection in Spring's bean container (in case
     * of lenient constructor resolution which is the default for regular bean definitions).
     * @param paramTypes the parameter types to match
     * @param args the arguments to match
     * @return the accumulated weight for all arguments
     */
    public static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
        int result = 0;
        for(int i = 0; i < paramTypes.length; i ++){
            Class<?> paramType = paramTypes[i];
            Object arg = args[i];

            if(!ClassUtils.isAssignableValue(paramType, arg)){
                return Integer.MAX_VALUE;
            }

            Class<?> superClass = arg.getClass().getSuperclass();
            while(superClass != null){
                if(paramType.equals(superClass)){
                    result += 2;
                    superClass = null;
                }else if(ClassUtils.isAssignable(paramType, superClass)){
                    result += 2;
                    superClass = superClass.getSuperclass();
                }else {
                    superClass = null;
                }
            }
            if(paramType.isInterface()){
                result += 1;
            }
        }
        return result;
    }
}
