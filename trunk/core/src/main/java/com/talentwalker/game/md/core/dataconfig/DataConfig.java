/**
 * @Title: DataConfigManager.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月25日  占志灵
 */

package com.talentwalker.game.md.core.dataconfig;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @ClassName: DataConfigManager
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月25日 下午4:14:38
 */

public class DataConfig implements Serializable {

    private static final long serialVersionUID = 970908319686644961L;
    private static Logger logger = LoggerFactory.getLogger(DataConfig.class);
    private static FilenameFilter xlsFileFilter = new XlsFileFilter();
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private boolean isArray;
    private Long version;

    public DataConfig() {
        this(false);
    }

    public DataConfig(boolean isArray) {
        if (isArray) {
            jsonArray = new JSONArray();
        } else {
            jsonObject = new JSONObject();
        }
        this.isArray = isArray;
    }

    public DataConfig(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        isArray = false;
    }

    public DataConfig(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        isArray = true;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public static DataConfig fromFilePath(String filePath) {
        DataConfig config = new DataConfig();
        File[] files = scanDataConfigFiles(filePath);
        for (File file : files) {
            appendFile(config, file);
        }

        return config;
    }

    public Object put(String key, Object value) {
        assertNotArray();
        if (value instanceof DataConfig) {
            DataConfig valueTmp = (DataConfig) value;
            if (valueTmp.isArray) {
                value = valueTmp.getJsonArray();
            } else {
                value = valueTmp.getJsonObject();
            }
        }
        return jsonObject.put(key, value);
    }

    public void add(int index, Object value) {
        assertArray();
        if (value instanceof DataConfig) {
            DataConfig valueTmp = (DataConfig) value;
            if (valueTmp.isArray) {
                value = valueTmp.getJsonArray();
            } else {
                value = valueTmp.getJsonObject();
            }
        }
        jsonArray.add(index, value);
    }

    public Boolean getBoolean(String key) {
        assertNotArray();
        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public Boolean getBoolean(int index) {
        assertArray();
        return jsonArray.getBoolean(index);
    }

    public Double getDouble(String key) {
        assertNotArray();
        try {
            return jsonObject.getDouble(key);
        } catch (JSONException e1) {
            return null;
        } catch (Exception e2) {
            return Double.parseDouble(jsonObject.getString(key));
        }
    }

    public Double getDouble(int index) {
        assertArray();
        return jsonArray.getDouble(index);
    }

    public Integer getInteger(String key) {
        assertNotArray();
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e1) {
            return null;
        } catch (Exception e2) {
            return Integer.parseInt(jsonObject.getString(key));
        }
    }

    public Integer getInteger(int index) {
        assertArray();
        return jsonArray.getInt(index);
    }

    public String getString(String key) {
        assertNotArray();
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public String getString(int index) {
        assertArray();
        return jsonArray.getString(index);
    }

    public DataConfig get(String key) {
        assertNotArray();
        DataConfig r = null;
        Object o = jsonObject.get(key);
        if (o == null) {
            return null;
        } else if (o instanceof JSONObject) {
            r = new DataConfig((JSONObject) o);
        } else if (o instanceof JSONArray) {
            r = new DataConfig((JSONArray) o);
        } else {
            GameExceptionUtils.throwException("不能获取DataConfig对象");
        }
        return r;
    }

    public int size() {
        assertArray();
        return this.jsonArray.size();
    }

    private void assertArray() {
        if (!isArray) {
            GameExceptionUtils.throwException("dataConfig is not a array");
        }
    }

    private void assertNotArray() {
        if (isArray) {
            GameExceptionUtils.throwException("dataConfig is a array");
        }
    }

    private static void appendFile(DataConfig config, File file) {
        DataConfig configThis = new DataConfig();
        String fileName = file.getName();
        fileName = fileName.substring(0, fileName.indexOf("."));

        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(file, null, true);
        } catch (Exception e) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, e.getMessage(), e);
        }
        Sheet sheet = wb.getSheetAt(0);

        List<Cell> titles = null;
        for (Row row : sheet) {
            if (titles != null) {
                appendRow(configThis, row, titles, fileName);
            } else {
                titles = findTitle(row, fileName);
            }
        }
        try {
            wb.close();
        } catch (Exception e) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, e.getMessage(), e);
        }

        config.put(fileName, configThis);
        logger.info("配置文件[" + fileName + "]加载完毕");
    }

    private static void appendRow(DataConfig config, Row row, List<Cell> titles, String fileName) {
        DataConfig configThis = new DataConfig();

        Cell keyCell = titles.get(0);
        Cell nameCell = row.getCell(keyCell.getColumnIndex());
        if (nameCell == null) {
            return;
        }
        int nameCellType = nameCell.getCellType();
        String name = null;
        if (nameCellType == Cell.CELL_TYPE_STRING) {
            name = nameCell.getRichStringCellValue().getString().trim();
            if (name.startsWith("//")) {
                return;
            }
        } else if (nameCellType == Cell.CELL_TYPE_NUMERIC) {
            name = StringUtils.subZeroAndDot((String.valueOf(nameCell.getNumericCellValue())));
        } else if (nameCellType == Cell.CELL_TYPE_BLANK) {
            return;
        } else if (nameCellType == Cell.CELL_TYPE_FORMULA) {
            try {
                name = nameCell.getRichStringCellValue().getString().trim();
            } catch (Exception e) {
                name = StringUtils.subZeroAndDot((String.valueOf(nameCell.getNumericCellValue())));
            }
        } else {
            GameExceptionUtils.throwException("配置文件[" + fileName + "]数据行[" + (row.getRowNum() + 1) + "]单元格["
                    + (nameCell.getColumnIndex() + 1) + "]格式不正确");
        }

        for (int i = 1; i < titles.size(); i++) {
            Cell cell = titles.get(i);
            int cellType = cell.getCellType();
            String key = null;
            if (cellType == Cell.CELL_TYPE_STRING) {
                key = cell.getRichStringCellValue().getString().trim();
            } else if (cellType == Cell.CELL_TYPE_NUMERIC) {
                key = StringUtils.subZeroAndDot(String.valueOf(cell.getNumericCellValue()));
            }
            String[] keyArray = key.split("\\.");
            String keyFinal = keyArray[keyArray.length - 1];// 最底层的key

            DataConfig configFinal = configThis;
            for (int j = 0; j < keyArray.length - 1; j++) {
                String keyTmp = keyArray[j];
                DataConfig configNext = configFinal.get(keyTmp);
                if (configNext == null) {
                    configNext = new DataConfig();
                    configFinal.put(keyTmp, configNext);
                }
                configFinal = configFinal.get(keyTmp);
            }

            Cell valueCell = row.getCell(cell.getColumnIndex());
            if (valueCell == null) {
                configFinal.put(keyFinal, null);
            } else {
                int valueCellType = valueCell.getCellType();
                if (valueCellType == Cell.CELL_TYPE_BLANK) {
                    configFinal.put(keyFinal, null);
                } else if (valueCellType == Cell.CELL_TYPE_BOOLEAN) {
                    configFinal.put(keyFinal, valueCell.getBooleanCellValue());
                } else if (valueCellType == Cell.CELL_TYPE_NUMERIC) {
                    configFinal.put(keyFinal, valueCell.getNumericCellValue());
                } else if (valueCellType == Cell.CELL_TYPE_FORMULA) {
                    Object value = null;
                    try {
                        value = valueCell.getRichStringCellValue().getString().trim();
                        configFinal.put(keyFinal, value);
                    } catch (Exception e) {
                        value = StringUtils.subZeroAndDot((String.valueOf(valueCell.getNumericCellValue())));
                        configFinal.put(keyFinal, value);
                    }
                } else if (valueCellType == Cell.CELL_TYPE_STRING) {
                    String value = valueCell.getRichStringCellValue().getString().trim();
                    if (value.startsWith("{")) {
                        try {
                            configFinal.put(keyFinal, new DataConfig(JSONObject.fromObject(value)));
                        } catch (Exception e) {
                            configFinal.put(keyFinal, value);
                        }
                    } else if (value.startsWith("[")) {
                        try {
                            configFinal.put(keyFinal, new DataConfig(JSONArray.fromObject(value)));
                        } catch (Exception e) {
                            configFinal.put(keyFinal, value);
                        }
                    } else {
                        configFinal.put(keyFinal, value);
                    }
                } else {
                    GameExceptionUtils.throwException("配置文件[" + fileName + "]数据行[" + (row.getRowNum() + 1) + "]单元格["
                            + (cell.getColumnIndex() + 1) + "]格式不正确");
                }
            }
        }

        config.put(name, configThis);
    }

    private static List<Cell> findTitle(Row row, String fileName) {
        List<Cell> titles = null;
        for (Cell cell : row) {
            int cellType = cell.getCellType();
            if (titles == null) {
                if (cellType == Cell.CELL_TYPE_STRING) {
                    String text = cell.getRichStringCellValue().getString().trim();
                    if (text.startsWith("//")) {
                        break;
                    } else if (text.equals("key")) {
                        titles = new ArrayList<Cell>();
                        titles.add(cell);
                        continue;
                    } else {
                        GameExceptionUtils
                                .throwException("配置文件[" + fileName + "]标题行[" + (row.getRowNum() + 1) + "]不是以key开头");
                    }
                } else if (cellType == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    GameExceptionUtils.throwException("配置文件[" + fileName + "]标题行[" + (row.getRowNum() + 1) + "]单元格["
                            + (cell.getColumnIndex() + 1) + "]格式不正确");
                }
            } else {
                if (cellType == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else if (cellType == Cell.CELL_TYPE_STRING) {
                    titles.add(cell);
                } else if (cellType == Cell.CELL_TYPE_NUMERIC) {
                    titles.add(cell);
                } else {
                    GameExceptionUtils.throwException("配置文件[" + fileName + "]标题行[" + (row.getRowNum() + 1) + "]单元格["
                            + (cell.getColumnIndex() + 1) + "]格式不正确");
                }
            }
        }
        return titles;
    }

    private static File[] scanDataConfigFiles(String filePath) {
        File path = new File(filePath);
        if (path.isDirectory()) {
            return path.listFiles(xlsFileFilter);
        } else {
            File pathDefault = new File("../../doc/dataconfig/test");
            logger.warn("配置文件目录[" + path.getAbsolutePath() + "]不正确，将加载默认路径[" + pathDefault.getAbsolutePath() + "]");
            if (pathDefault.isDirectory()) {
                return pathDefault.listFiles(xlsFileFilter);
            } else {
                throw new IllegalArgumentException("配置文件目录[" + pathDefault.getAbsolutePath() + "]不正确");
            }
        }
    }

    /**.
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isArray) {
            return jsonArray.toString();
        }
        return jsonObject.toString();
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    static class XlsFileFilter implements FilenameFilter, Serializable {

        private static final long serialVersionUID = -1322516903047039679L;

        /**.
         * <p>Title: accept</p>
         * <p>Description: </p>
         * @param dir
         * @param name
         * @return
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith("xls") ? true : false;
        }

    }

    public static void main(String[] args) {
        File pathDefault = new File("");
        System.out.println(pathDefault.getAbsoluteFile().getParent());
        // 读取配置目录，变成DataConfig对象
        DataConfig dataConfig = DataConfig.fromFilePath("D:\\日常处理\\MAID项目\\配置");
        System.out.println(dataConfig);

        // 取某一配置值
        String heroId = dataConfig.get("模板").get("1").get("1").get("1").getString("1");
        System.out.println(heroId);

    }
}
