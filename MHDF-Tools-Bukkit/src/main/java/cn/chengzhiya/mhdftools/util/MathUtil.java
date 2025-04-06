package cn.chengzhiya.mhdftools.util;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class MathUtil {
    /**
     * 计算数学表达式
     *
     * @param text 数学表达式文本
     * @return 结果
     */
    public static double calculate(String text) {
        Expression expression = new ExpressionBuilder(text).build();
        return expression.evaluate();
    }
}
