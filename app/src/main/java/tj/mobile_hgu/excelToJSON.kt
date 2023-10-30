import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

fun excelToJSON(inputStream: InputStream): JSONObject {
    val jsonData = JSONObject()
    val groupsArray = JSONArray()

    try {
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0) // Предполагается, что данные находятся в первом листе

        val groupData = mutableMapOf<String, JSONObject>()

        for (row in sheet) {
            if (row.rowNum == 0) {
                // Пропустить строку заголовка (предполагая, что она находится в первой строке)
                continue
            }

            val groupCell = row.getCell(0)
            val groupnameCell = row.getCell(1)
            val teacherCell = row.getCell(2)
            val lessonCell = row.getCell(3)
            val timeCell = row.getCell(4)
            val dayCell = row.getCell(5)

            val group = if (groupCell != null) {
                when (groupCell.cellType) {
                    CellType.STRING -> groupCell.stringCellValue
                    CellType.NUMERIC -> groupCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val groupname = if (groupnameCell != null) {
                when (groupnameCell.cellType) {
                    CellType.STRING -> groupnameCell.stringCellValue
                    CellType.NUMERIC -> groupnameCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val teacher = if (teacherCell != null) {
                when (teacherCell.cellType) {
                    CellType.STRING -> teacherCell.stringCellValue
                    CellType.NUMERIC -> teacherCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val lesson = if (lessonCell != null) {
                when (lessonCell.cellType) {
                    CellType.STRING -> lessonCell.stringCellValue
                    CellType.NUMERIC -> lessonCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val time = if (timeCell != null) {
                when (timeCell.cellType) {
                    CellType.STRING -> timeCell.stringCellValue
                    CellType.NUMERIC -> timeCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val day = if (dayCell != null) {
                when (dayCell.cellType) {
                    CellType.STRING -> dayCell.stringCellValue
                    CellType.NUMERIC -> dayCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            // Проверить, существует ли уже группа с указанным именем в groupData
            if (groupData.containsKey(group)) {
                val groupObject = groupData[group]!!
                val daysArray = groupObject.getJSONArray("days")
                val dayObject = daysArray.getJSONObject(daysArray.length() - 1)

                if (day == dayObject.getString("day")) {
                    // Тот же день, добавить сессию
                    val sessionsArray = dayObject.getJSONArray("sessions")
                    val sessionObject = JSONObject()
                    sessionObject.put("lesson", lesson)
                    sessionObject.put("time", time)
                    sessionObject.put("teacher", teacher)
                    sessionsArray.put(sessionObject)
                } else {
                    // Новый день, создать новый объект для дня
                    val newDayObject = JSONObject()
                    newDayObject.put("day", day)
                    val sessionsArray = JSONArray()
                    val sessionObject = JSONObject()
                    sessionObject.put("lesson", lesson)
                    sessionObject.put("time", time)
                    sessionObject.put("teacher", teacher)
                    sessionsArray.put(sessionObject)
                    newDayObject.put("sessions", sessionsArray)
                    daysArray.put(newDayObject)
                }
            } else {
                // Новая группа, создать новый объект группы
                val groupObject = JSONObject()
                groupObject.put("groupname", group)
                val daysArray = JSONArray()
                val dayObject = JSONObject()
                dayObject.put("day", day)
                val sessionsArray = JSONArray()
                val sessionObject = JSONObject()
                sessionObject.put("lesson", lesson)
                sessionObject.put("time", time)
                sessionObject.put("teacher", teacher)
                sessionsArray.put(sessionObject)
                dayObject.put("sessions", sessionsArray)
                daysArray.put(dayObject)
                groupObject.put("days", daysArray)
                groupData[group] = groupObject
            }
        }

        // Добавить объекты групп в массив groupsArray
        for (groupObject in groupData.values) {
            groupsArray.put(groupObject)
        }

        workbook.close()

        if (groupsArray.length() > 0) {
            jsonData.put("groups", groupsArray)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonData
}
