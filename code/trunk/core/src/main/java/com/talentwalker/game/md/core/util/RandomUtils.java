/**
 * @Title: RandomUtils.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月14日  占志灵
 */

package com.talentwalker.game.md.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RandomUtils
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月14日 下午6:47:41
 */

public class RandomUtils {

    /**
     * @Description:随机获得种子
     * @return 种子，0-999
     * @throws
     */
    public static int randomSeed() {
        return (int) (Math.random() * 1000);

    }

    /**
     * @Description:伪随机获得范围内的一个整数 
     * @param start
     * @param end
     * @param cursor 种子，0-999
     * @return 随机整数
     * @throws
     */
    public static int fakeRandomInt(int start, int end, int cursor) {
        int fakeRand = seedArray[cursor];
        int range = end - start + 1;
        fakeRand = (int) Math.floor(fakeRand * range / 1000) + start;
        cursor = (cursor + 1) % 1000;
        return fakeRand;
    }

    /**
    * @Description:随机获得范围内的一个整数
    * @param start
    * @param end
    * @return 随机整数
    * @throws
    */
    public static int randomInt(int start, int end) {
        return fakeRandomInt(start, end, randomSeed());
    }

    /**
     * @Description:伪随机圆桌算法
     * @param weightList 权重列表
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int fakeRandomTable(List<Integer> weightList, int cursor) {
        int result = 0;
        int sum = 0;
        for (Integer integer : weightList) {
            sum += integer;
        }
        int fakeRand = fakeRandomInt(1, sum, cursor);
        int offSet = 0;
        for (int i = 0; i < weightList.size(); i++) {
            Integer value = weightList.get(i);
            if (fakeRand <= value + offSet) {
                result = i;
                break;
            } else {
                offSet += value;
            }
        }

        return result;
    }

    /**
     * @Description:随机圆桌算法
     * @param weightList 权重列表
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static String randomTableDouble(Map<String, Integer> weightMap) {
        String result = "";
        int sum = 0;
        Iterator entIt = weightMap.entrySet().iterator();
        while (entIt.hasNext()) {
            Map.Entry entrymap = (Map.Entry) entIt.next();
            sum += (int) Double.parseDouble(entrymap.getValue() + "");
        }
        int fakeRand = fakeRandomInt(1, sum, randomSeed());
        int offSet = 0;
        Iterator its = weightMap.entrySet().iterator();
        while (its.hasNext()) {
            Map.Entry entrymap = (Map.Entry) its.next();
            String key = entrymap.getKey() + "";
            int value = (int) Double.parseDouble(entrymap.getValue() + "");
            if (fakeRand <= value + offSet) {
                result = key;
                break;
            } else {
                offSet += value;
            }
        }
        return result;
    }

    /**
     * @Description:随机圆桌算法
     * @param weightList 权重列表
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int randomTable(List<Integer> weightList) {
        return fakeRandomTable(weightList, randomSeed());
    }

    public static String randomTable(Map<String, Integer> weightMap) {
        String result = "";
        int sum = 0;
        Iterator<String> it = weightMap.keySet().iterator();
        while (it.hasNext()) {
            sum += weightMap.get(it.next());
        }
        int fakeRand = fakeRandomInt(1, sum, randomSeed());
        int offSet = 0;
        Iterator<String> its = weightMap.keySet().iterator();
        while (its.hasNext()) {
            String key = its.next();
            Integer value = weightMap.get(key);
            if (fakeRand <= value + offSet) {
                result = key;
                break;
            } else {
                offSet += value;
            }
        }
        return result;
    }

    /**
     * @Description:伪随机圆桌算法；根据权重范围限制，权重数组越靠前的优先级越高
     * @param weightList 权重列表
     * @param limit 权重最大范围限制
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int fakeRandomTableWithPriorityAndLimit(List<Integer> weightList, int limit, int cursor) {
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < weightList.size(); i++) {
            Integer value = weightList.get(i);
            if (limit > 0) {
                newList.add(Math.min(value, limit));
                limit = limit - value;
            }
        }
        return fakeRandomTable(newList, cursor);
    }

    /**
     * @Description:随机圆桌算法；根据权重范围限制，权重数组越靠前的优先级越高
     * @param weightList 权重列表
     * @param limit 权重最大范围限制
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int randomTableWithPriorityAndLimit(List<Integer> weightList, int limit) {
        return fakeRandomTableWithPriorityAndLimit(weightList, limit, randomSeed());
    }

    /**
     * @Description:伪随机圆桌算法；根据权重范围限制，权重数组越靠前的优先级越高
     * @param weightList 权重列表
     * @param limit 权重最大范围限制
     * @param needSize 必参与权重运算的权重个数，会影响权重范围限制
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int fakeRandomTableWithPriorityAndLimitAndNeedSize(List<Integer> weightList, int limit, int needSize,
            int cursor) {
        int sum = 0;
        for (int i = 0; i < needSize; i++) {
            sum += weightList.get(i);
        }
        limit = Math.max(limit, sum);
        return fakeRandomTableWithPriorityAndLimit(weightList, limit, cursor);
    }

    /**
     * @Description:随机圆桌算法；根据权重范围限制，权重数组越靠前的优先级越高
     * @param weightList 权重列表
     * @param limit 权重最大范围限制
     * @param needSize 必参与权重运算的权重个数，会影响权重范围限制
     * @param cursor 种子
     * @return 权重数组的下标
     * @throws
     */
    public static int randomTableWithPriorityAndLimitAndNeedSize(List<Integer> weightList, int limit, int needSize) {
        return fakeRandomTableWithPriorityAndLimitAndNeedSize(weightList, limit, needSize, randomSeed());
    }

    public static void main(String[] args) {
        int seed = RandomUtils.randomSeed();
        System.out.println("种子：" + seed);
        List<Integer> weightList = new ArrayList<Integer>();
        weightList.add(50);
        weightList.add(5000);
        weightList.add(10000);
        System.out.println("权重列表：" + weightList);
        System.out.println("fakeRandomInt：" + RandomUtils.fakeRandomInt(1, 3, seed));
        System.out.println("fakeRandomTable：" + RandomUtils.fakeRandomTable(weightList, seed));
        System.out.println("fakeRandomTableWithPriorityAndLimit："
                + RandomUtils.fakeRandomTableWithPriorityAndLimit(weightList, 100, seed));
        System.out.println("fakeRandomTableWithPriorityAndLimit："
                + RandomUtils.fakeRandomTableWithPriorityAndLimitAndNeedSize(weightList, 100, 2, seed));

    }

    /**
     * 伪随机种子数组；1000个数字，随机打乱的0-999
     */
    public static int[] seedArray = new int[] {543, 650, 385, 147, 497, 51, 64, 80, 715, 982, 378, 604, 137, 88, 891,
            195, 669, 805, 566, 657, 912, 732, 612, 289, 453, 784, 258, 328, 208, 764, 875, 570, 653, 479, 874, 310, 4,
            102, 910, 689, 8, 327, 647, 445, 181, 515, 288, 986, 207, 65, 131, 361, 188, 619, 770, 458, 329, 890, 5,
            156, 166, 93, 893, 14, 466, 217, 621, 881, 824, 769, 297, 521, 697, 838, 782, 565, 240, 334, 441, 597, 461,
            801, 533, 706, 214, 308, 99, 386, 76, 408, 464, 381, 974, 447, 83, 150, 979, 683, 981, 916, 349, 50, 631,
            947, 777, 779, 44, 720, 548, 124, 169, 394, 726, 501, 592, 89, 748, 815, 675, 454, 860, 672, 199, 443, 760,
            538, 237, 1, 100, 229, 132, 739, 606, 639, 663, 567, 46, 898, 816, 267, 107, 256, 393, 892, 261, 138, 160,
            40, 223, 398, 651, 431, 756, 703, 176, 360, 562, 293, 498, 161, 507, 114, 773, 978, 2, 220, 346, 242, 457,
            880, 519, 70, 708, 958, 929, 69, 292, 472, 337, 855, 918, 58, 247, 781, 790, 179, 201, 282, 430, 234, 968,
            332, 531, 352, 345, 204, 244, 932, 617, 933, 126, 627, 917, 358, 287, 101, 39, 618, 317, 405, 806, 320, 699,
            315, 915, 991, 148, 171, 186, 275, 175, 322, 788, 167, 813, 654, 485, 583, 596, 295, 850, 643, 414, 210,
            953, 82, 298, 861, 286, 608, 367, 865, 944, 456, 182, 153, 532, 628, 630, 743, 232, 868, 691, 745, 251, 528,
            778, 658, 954, 173, 906, 803, 224, 513, 374, 553, 839, 509, 301, 163, 649, 19, 517, 291, 768, 572, 164, 23,
            366, 555, 239, 575, 560, 170, 609, 15, 844, 598, 882, 344, 59, 755, 687, 225, 943, 250, 602, 712, 540, 236,
            200, 834, 753, 594, 206, 611, 265, 993, 252, 397, 79, 330, 312, 492, 674, 971, 939, 273, 607, 679, 424, 757,
            281, 135, 388, 869, 448, 211, 496, 889, 879, 665, 633, 72, 564, 77, 508, 318, 384, 221, 792, 216, 911, 127,
            486, 444, 700, 489, 676, 746, 123, 442, 249, 690, 851, 215, 524, 68, 845, 542, 151, 248, 907, 767, 671, 305,
            133, 529, 711, 62, 709, 103, 440, 936, 351, 180, 490, 7, 830, 913, 144, 737, 85, 55, 75, 998, 129, 259, 718,
            535, 183, 847, 197, 474, 110, 629, 554, 130, 989, 355, 673, 775, 849, 314, 776, 717, 516, 451, 842, 644,
            380, 872, 967, 887, 558, 425, 25, 219, 483, 255, 733, 48, 24, 121, 407, 353, 514, 871, 758, 941, 370, 12,
            274, 469, 561, 601, 713, 227, 996, 143, 973, 972, 771, 620, 284, 527, 902, 952, 450, 937, 134, 467, 802,
            404, 735, 499, 835, 326, 426, 91, 648, 433, 961, 896, 637, 47, 185, 190, 983, 797, 364, 701, 165, 482, 599,
            139, 396, 6, 858, 423, 823, 876, 406, 53, 766, 862, 710, 369, 873, 810, 593, 52, 894, 28, 422, 999, 586,
            412, 313, 877, 17, 410, 731, 338, 568, 280, 13, 763, 203, 35, 235, 108, 976, 460, 640, 886, 716, 33, 42,
            828, 707, 54, 759, 818, 730, 26, 481, 582, 853, 228, 995, 964, 470, 809, 600, 680, 277, 67, 257, 463, 843,
            962, 556, 226, 145, 930, 29, 61, 383, 254, 94, 402, 299, 340, 120, 796, 846, 854, 194, 678, 331, 785, 615,
            230, 60, 772, 812, 605, 723, 656, 172, 613, 749, 786, 888, 285, 3, 491, 418, 914, 63, 904, 371, 307, 624,
            819, 791, 45, 417, 390, 795, 510, 625, 793, 655, 95, 963, 957, 81, 452, 109, 537, 302, 557, 622, 905, 949,
            681, 73, 86, 468, 789, 729, 544, 294, 744, 359, 750, 382, 300, 852, 741, 921, 158, 511, 306, 191, 695, 427,
            668, 419, 800, 634, 56, 935, 487, 266, 27, 446, 177, 391, 196, 922, 436, 36, 589, 449, 484, 432, 253, 573,
            500, 550, 505, 590, 341, 821, 389, 965, 189, 959, 580, 923, 563, 946, 927, 18, 820, 576, 316, 635, 660, 319,
            233, 661, 416, 401, 549, 193, 115, 84, 272, 354, 632, 955, 988, 574, 455, 376, 804, 724, 794, 520, 495, 588,
            807, 692, 74, 811, 57, 761, 546, 399, 395, 503, 184, 105, 721, 357, 734, 178, 884, 682, 202, 379, 685, 342,
            276, 104, 488, 702, 975, 994, 662, 899, 309, 837, 323, 10, 677, 948, 403, 638, 704, 415, 942, 241, 37, 512,
            951, 585, 969, 439, 476, 960, 31, 956, 646, 642, 377, 829, 429, 822, 938, 740, 30, 245, 9, 400, 990, 311,
            475, 885, 684, 335, 920, 719, 783, 243, 551, 471, 526, 348, 587, 238, 534, 931, 525, 997, 504, 945, 925,
            696, 264, 747, 591, 864, 840, 980, 375, 992, 428, 142, 859, 459, 92, 303, 754, 901, 438, 950, 205, 727, 96,
            926, 970, 321, 493, 571, 149, 664, 246, 603, 296, 263, 798, 411, 187, 465, 836, 325, 985, 584, 742, 268,
            530, 333, 174, 578, 623, 670, 522, 350, 559, 480, 362, 752, 363, 856, 343, 32, 934, 878, 705, 209, 90, 897,
            420, 365, 494, 787, 903, 848, 20, 738, 116, 977, 826, 518, 155, 336, 372, 262, 502, 966, 924, 536, 539, 595,
            506, 614, 71, 652, 722, 192, 919, 111, 636, 279, 780, 645, 714, 579, 260, 857, 97, 908, 218, 659, 141, 159,
            22, 409, 883, 157, 368, 21, 113, 34, 616, 762, 547, 831, 569, 751, 870, 437, 477, 347, 198, 987, 140, 373,
            833, 698, 728, 290, 66, 270, 213, 940, 212, 119, 688, 832, 387, 392, 106, 825, 154, 774, 581, 356, 473, 478,
            49, 269, 909, 112, 641, 11, 552, 765, 278, 435, 841, 231, 162, 38, 863, 545, 87, 626, 168, 434, 324, 686,
            693, 43, 666, 122, 78, 0, 421, 283, 271, 462, 339, 117, 827, 128, 694, 16, 808, 118, 304, 867, 725, 98, 413,
            136, 610, 222, 814, 667, 984, 152, 799, 900, 895, 41, 146, 736, 866, 541, 125, 577, 928, 817, 523 };
}
