
package com.talentwalker.game.md.core.sensitiveword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentwalker.game.md.core.util.UnicodeReader;

/**
 * @ClassName: SensitiveWord
 * @Description: 
 * 将敏感词库构建成一个DFA算法模型<br>
 * 假设敏感词库为：<br>
 * 共产、共产党、共产主义、社会、社会主义<br>
 * 则词库为：<br>
 * 共(isEnd=false)
 * |---产(isEnd=true)
 *     |---党(isEnd=true)
 *     |---主(isEnd=false)
 *         |---义(isEnd=true)
 * 社(isEnd=false)
 * |---会(isEnd=true)
 *     |---主(isEnd=false)
 *         |---义(isEnd=true)
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月5日 下午4:26:58
 */
public class SensitiveWord {
    private static Logger logger = LoggerFactory.getLogger(SensitiveWord.class);
    private static String ENCODING = "UTF-8";
    public static SensitiveWordElement sensitiveWordElement;

    /**
     * @Description:查看一个字符串是否包含敏感词
     * @param txt
     * @return
     * @throws
     */
    public static boolean isContaintSensitiveWord(String txt) {
        SensitiveWordElement nowElement = null;
        for (int i = 0; i < txt.length(); i++) {
            nowElement = sensitiveWordElement;
            for (int j = i; j < txt.length(); j++) {
                char word = txt.charAt(j);
                SensitiveWordElement eTmp = nowElement.get(word);
                if (eTmp == null) {
                    break;
                } else if (eTmp.isEnd()) {
                    return true;
                }
                nowElement = eTmp;
            }
        }
        return false;
    }

    /**
     * @Description:将敏感词替换成hold
     * @param txt
     * @param hold
     * @return
     * @throws
     */
    public static String replaceSensitiveWord(String txt, char hold) {
        StringBuilder sb = new StringBuilder();
        SensitiveWordElement nowElement = null;
        for (int i = 0; i < txt.length(); i++) {
            nowElement = sensitiveWordElement;
            int l = 0;
            for (int j = i; j < txt.length(); j++) {
                char word = txt.charAt(j);
                SensitiveWordElement eTmp = nowElement.get(word);
                if (eTmp == null) {
                    break;
                } else if (eTmp.isEnd()) {
                    l = j - i + 1;
                    break;
                }
                nowElement = eTmp;
            }
            if (l > 0) {
                for (int k = 0; k < l; k++) {
                    sb.append(hold);
                }
                i += l - 1;
            } else {
                sb.append(txt.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * @Description:将目录下的文件解析成敏感词库
     * @param filePath
     * @throws Exception
     * @throws
     */
    public static void fromFilePath(String filePath) throws Exception {
        File[] files = scanFiles(filePath);
        Set<String> wordSet = new HashSet<>();
        for (File file : files) {
            appendFileToSet(file, wordSet);
        }
        create(wordSet);
    }

    private static void create(Set<String> wordSet) {
        sensitiveWordElement = new SensitiveWordElement();
        Iterator<String> iterator = wordSet.iterator();
        String word = null;
        SensitiveWordElement nowElement = null;
        char keyChar;
        SensitiveWordElement eTmp = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            nowElement = sensitiveWordElement;
            for (int i = 0; i < word.length(); i++) {
                keyChar = word.charAt(i);
                eTmp = nowElement.get(keyChar);
                if (eTmp == null) {
                    eTmp = new SensitiveWordElement();
                    nowElement.add(keyChar, eTmp);
                }
                nowElement = eTmp;
            }
            nowElement.setEnd(true);
        }
    }

    private static void appendFileToSet(File file, Set<String> set) throws Exception {
        UnicodeReader read = new UnicodeReader(new FileInputStream(file), ENCODING);
        try {
            if (file.isFile() && file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {
                    set.add(txt);
                }
                bufferedReader.close();
            } else {
                throw new Exception("敏感词库文件[" + file + "]不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();
        }
    }

    private static File[] scanFiles(String filePath) {
        File path = new File(filePath);
        if (path.isDirectory()) {
            return path.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("txt") ? true : false;
                }
            });
        } else {
            File pathDefault = new File("../../doc/sensitiveWord");
            logger.warn("敏感词库目录[" + path.getAbsolutePath() + "]不正确，将加载默认路径[" + pathDefault.getAbsolutePath() + "]");
            if (pathDefault.isDirectory()) {
                return pathDefault.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith("txt") ? true : false;
                    }
                });
            } else {
                throw new IllegalArgumentException("敏感词库目录[" + pathDefault.getAbsolutePath() + "]不正确");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SensitiveWord.fromFilePath("D:\\eclipse-jee-mars-2-win32-x86_64\\workspace\\game-md\\doc\\sensitiveWord");
        System.out.println(SensitiveWord.isContaintSensitiveWord("第一代领导"));
        System.out.println(SensitiveWord.replaceSensitiveWord("第二代领导", '*'));
    }
}
