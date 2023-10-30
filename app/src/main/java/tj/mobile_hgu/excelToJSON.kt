import android.content.res.AssetManager
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONArray
import org.json.JSONObject

fun excelToJSON(assetManager: AssetManager, fileName: String): JSONObject {
    val jsonData = JSONObject()
    val facultiesArray = JSONArray()
    try {
        val inputStream = assetManager.open(fileName)
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0) // Предполагается, что данные находятся в первом листе

        var currentFaculty = "Физика" // Факультет по умолчанию

        val facultyData = mutableMapOf<String, JSONObject>()

        for (row in sheet) {
            if (row.rowNum == 0) {
                // Пропустить строку заголовка (предполагая, что она находится в первой строке)
                continue
            }

            val codeCell = row.getCell(0)
            val groupNameCell = row.getCell(1)
            val lessonCell = row.getCell(2)
            val timeCell = row.getCell(3)
            val teacherCell = row.getCell(4)
            val dayCell = row.getCell(5)
            val facultyCell = row.getCell(6)

            val code = if (codeCell != null) {
                when (codeCell.cellType) {
                    CellType.STRING -> codeCell.stringCellValue
                    CellType.NUMERIC -> codeCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            val groupName = if (groupNameCell != null) {
                val code = if (codeCell != null) {
                    when (codeCell.cellType) {
                        CellType.STRING -> codeCell.stringCellValue
                        CellType.NUMERIC -> codeCell.numericCellValue.toString()
                        else -> ""
                    }
                } else {
                    ""
                }
                val name = when (groupNameCell.cellType) {
                    CellType.STRING -> groupNameCell.stringCellValue
                    CellType.NUMERIC -> groupNameCell.numericCellValue.toString()
                    else -> ""
                }
                val fullString = code
                val result = fullString.substringBefore(".")
                "$result-$name"
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

            val teacher = if (teacherCell != null) {
                when (teacherCell.cellType) {
                    CellType.STRING -> teacherCell.stringCellValue
                    CellType.NUMERIC -> teacherCell.numericCellValue.toString()
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

            val faculty = if (facultyCell != null) {
                when (facultyCell.cellType) {
                    CellType.STRING -> facultyCell.stringCellValue
                    CellType.NUMERIC -> facultyCell.numericCellValue.toString()
                    else -> ""
                }
            } else {
                ""
            }

            if (faculty.isNotEmpty()) {
                currentFaculty = faculty
            }

            if (facultyData.containsKey(currentFaculty)) {
                val groupObject = facultyData[currentFaculty]!!
                val groupArray = groupObject.getJSONArray("groups")
                var groupExists = false
                for (groupIndex in 0 until groupArray.length()) {
                    val group = groupArray.getJSONObject(groupIndex)
                    if (group.getString("name") == groupName) {
                        val daysObject = group.getJSONArray("schedule")
                        var dayExists = false
                        for (dayIndex in 0 until daysObject.length()) {
                            val dayObject = daysObject.getJSONObject(dayIndex)
                            if (dayObject.getString("day") == day) {
                                val sessionsArray = dayObject.getJSONArray("sessions")
                                val sessionObject = JSONObject()
                                sessionObject.put("time", time)
                                sessionObject.put("teacher", teacher)
                                sessionObject.put("lesson", lesson)
                                sessionsArray.put(sessionObject)
                                dayExists = true
                            }
                        }
                        if (!dayExists) {
                            val dayObject = JSONObject()
                            dayObject.put("day", day)
                            val sessionsArray = JSONArray()
                            val sessionObject = JSONObject()
                            sessionObject.put("time", time)
                            sessionObject.put("teacher", teacher)
                            sessionObject.put("lesson", lesson)
                            sessionsArray.put(sessionObject)
                            dayObject.put("sessions", sessionsArray)
                            daysObject.put(dayObject)
                        }
                        groupExists = true
                    }
                }
                if (!groupExists) {
                    val groupObject = JSONObject()
                    groupObject.put("name", groupName)
                    val daysArray = JSONArray()
                    val dayObject = JSONObject()
                    dayObject.put("day", day)
                    val sessionsArray = JSONArray()
                    val sessionObject = JSONObject()
                    sessionObject.put("time", time)
                    sessionObject.put("teacher", teacher)
                    sessionObject.put("lesson", lesson)
                    sessionsArray.put(sessionObject)
                    dayObject.put("sessions", sessionsArray)
                    daysArray.put(dayObject)
                    groupObject.put("schedule", daysArray)
                    groupArray.put(groupObject)
                }
            } else {
                val groupObject = JSONObject()
                groupObject.put("name", currentFaculty)
                val groupsArray = JSONArray()
                val groupArray = JSONObject()
                groupArray.put("name", groupName)
                val daysArray = JSONArray()
                val dayObject = JSONObject()
                dayObject.put("day", day)
                val sessionsArray = JSONArray()
                val sessionObject = JSONObject()
                sessionObject.put("time", time)
                sessionObject.put("teacher", teacher)
                sessionObject.put("lesson", lesson)
                sessionsArray.put(sessionObject)
                dayObject.put("sessions", sessionsArray)
                daysArray.put(dayObject)
                groupArray.put("schedule", daysArray)
                groupsArray.put(groupArray)
                groupObject.put("groups", groupsArray)
                facultyData[currentFaculty] = groupObject
            }
        }
        for (facultyName in facultyData.keys) {
            facultiesArray.put(facultyData[facultyName])
        }
        jsonData.put("faculties", facultiesArray)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return jsonData
}
