package cn.chengzhiya.mhdftools.util.random;

import io.netty.util.internal.ThreadLocalRandom;

public final class RandomUtil {

    /**
     * 生成指定范围内的随机整数（包含最小值，不包含最大值）
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     * @return [min, max) 区间内的随机整数
     * @throws IllegalArgumentException 如果最小值大于等于最大值
     * <p>
     * int dice = RandomUtil.randomInt(1, 7); // 生成1-6的随机数
     * }
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * 生成随机长整型数值
     *
     * @return 均匀分布的long类型随机数（可能为负数）
     * <p>
     * long id = RandomUtil.randomLong(); // 例如-2345234523452345
     * }
     */
    public static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    /**
     * 生成[0.0, 1.0)区间的随机双精度浮点数
     *
     * @return 0.0（包含）到1.0（不包含）之间的随机数
     * <p>
     * double percent = RandomUtil.randomDouble(); // 例如0.8345234523
     * }
     */
    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 生成[0.0f, 1.0f)区间的随机单精度浮点数
     *
     * @return 0.0f（包含）到1.0f（不包含）之间的随机数
     * <p>
     * float ratio = RandomUtil.randomFloat(); // 例如0.45f
     * }
     */
    public static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    /**
     * 生成随机布尔值
     *
     * @return true或false，各有50%概率
     * <p>
     * boolean flag = RandomUtil.randomBoolean(); // 随机返回true/false
     * }
     */
    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
